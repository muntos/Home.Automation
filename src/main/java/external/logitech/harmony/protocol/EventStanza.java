package external.logitech.harmony.protocol;

import external.logitech.harmony.config.Activity.Status;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StandardExtensionElement;
import org.jivesoftware.smack.packet.Stanza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static external.logitech.harmony.Jackson.OBJECT_MAPPER;

public class EventStanza  {

    private static final Logger logger = LoggerFactory.getLogger(EventStanza.class);

    // JSON tokens to parse events
    private String errorCode;
    private String activityId;
    private Integer activityStatus;
    
    private EventType eventType;
    
    public enum EventType {
        STATE_DIGEST,
        START_ACTIVITY_FINISHED,
    }
    
    private EventStanza() {
    }
    
    public Integer getActivityId() {
        if (activityId != null) {
            try {
                Integer id = Integer.parseInt(activityId);
                return id;
            } catch (NumberFormatException e)
            {
                return null;
            }
        }
        return null;
    }
    
    public Status getActivityStatus() {
        if (activityStatus != null) {
            switch (activityStatus)
            {  
                case 0: 
                    return Status.HUB_IS_OFF;
                case 1:
                    return Status.ACTIVITY_IS_STARTING;
                case 2:
                    return Status.ACTIVITY_IS_STARTED;
                case 3:
                    return Status.HUB_IS_TURNING_OFF;
            }
        }
        return Status.UNKNOWN;
    }
    
    public EventType getEventType() {
        return eventType;
    }

    public int getErrorCode() {
        if (errorCode != null) {
            try {
                return Integer.parseInt(errorCode);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static EventStanza create(Stanza stanza) {
        if (!(stanza instanceof Message)) {
            return null;
        }
        Message message = (Message) stanza;
        ExtensionElement element = message.getExtension("event", "connect.logitech.com");
        if (element == null || !(element instanceof StandardExtensionElement)) {
            return null;
        }
        StandardExtensionElement stdElement = (StandardExtensionElement) element;
        String type = stdElement.getAttributeValue("type");
        String content = stdElement.getText();

        if (type != null && type.equals("connect.stateDigest?notify"))
        {
            if (content != null) {
                try {
                    EventStanza event = OBJECT_MAPPER.readValue(content, EventStanza.class);
                    event.eventType = EventType.STATE_DIGEST;
                    return event;
                } catch (IOException e) {
                    logger.error("Exception parsing stateDigest: {}", e.getMessage());
                }
            }
        } else if (type.equals("harmony.engine?startActivityFinished")) {
            if (content != null) {
                try {
                    EventStanza event = OBJECT_MAPPER.convertValue(OAReplyParser.parseKeyValuePairs(null, null, content), 
                            EventStanza.class);
                    event.eventType = EventType.START_ACTIVITY_FINISHED;
                    return event;
                } catch (IllegalArgumentException e) {
                    logger.debug("Exception parsing startActivityFinished: {}", e.getMessage());
                }
            }
        }
        return null;
    }
}
