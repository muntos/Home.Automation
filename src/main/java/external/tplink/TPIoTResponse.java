package external.tplink;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TPIoTResponse {
    private int error_code;
    private String error_msg;
    private long responseTimeMils;
    private String result;

}
