package external.logitech.harmony.protocol;

import com.google.common.collect.ImmutableMap;
import org.jivesoftware.smack.packet.IQ;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static external.logitech.harmony.Jackson.OBJECT_MAPPER;

public class MessageGetCurrentActivity {
    public static final String MIME_TYPE = "vnd.logitech.harmony/vnd.logitech.harmony.engine?getCurrentActivity";

    /*
     * Request
     */
    public static class GetCurrentActivityRequest extends IrCommand {
        public GetCurrentActivityRequest() {
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
    public static class GetCurrentActivityReply extends OAStanza {
        private int result;

        public GetCurrentActivityReply() {
            super(MIME_TYPE);
        }

        @Override
        protected Map<String, Object> getChildElementPairs() {
            return ImmutableMap.<String, Object> builder() //
                    .put("result", result)
                    .build();
        }

        public void setResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }
    }

    /*
     * Parser
     */
    public static class GetCurrentActivityReplyParser extends OAReplyParser {
        @Override
        public IQ parseReplyContents(String statusCode, String errorString, String contents) {
            return OBJECT_MAPPER.convertValue(parseKeyValuePairs(statusCode, errorString, contents),
                    GetCurrentActivityReply.class);
        }
    }
}
