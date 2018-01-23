package net.whistlingfish.harmony.protocol;

import com.google.common.collect.ImmutableMap;
import org.jivesoftware.smack.packet.IQ;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static net.whistlingfish.harmony.Jackson.OBJECT_MAPPER;

public class MessagePing {
    public static final String MIME_TYPE = "vnd.logitech.connect/vnd.logitech.ping";

    /*
     * Request
     */
    public static class PingRequest extends IrCommand {
        public PingRequest() {
            super(MIME_TYPE);
        }

        @Override
        protected Map<String, Object> getChildElementPairs() {
            return emptyMap();
        }
    }

    /*
     * Reply
     */
    public static class PingReply extends OAStanza {
        public PingReply() {
            super(MIME_TYPE);
        }

        @Override
        protected Map<String, Object> getChildElementPairs() {
            return ImmutableMap.<String, Object> builder() //
                    .build();
        }
    }

    /*
     * Parser
     */
    public static class PingReplyParser extends OAReplyParser {
        @Override
        public IQ parseReplyContents(String statusCode, String errorString, String contents) {
            return OBJECT_MAPPER.convertValue(parseKeyValuePairs(statusCode, errorString, contents),
                    PingReply.class);
        }
    }
}
