package home.network.automation.devices.tplink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TapoLogin {
    private String username;

    @ToString.Exclude
    private String password;
}
