package external.tplink;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDeviceRequest {
    private String password;
    private String username;
}
