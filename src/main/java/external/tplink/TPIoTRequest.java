package external.tplink;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TPIoTRequest<T> {
    private String method;
    private T params;
    private transient long requestID;
    private long requestTimeMils;
    private String terminalUUID;

}
