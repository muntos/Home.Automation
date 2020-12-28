package external.tplink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlugDeviceInfoParams extends DeviceInfoParams{
    @JsonProperty("led_enable")
    private Boolean ledEnable;
}
