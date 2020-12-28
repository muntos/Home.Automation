package external.tplink;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceInfoParams {
    private String avatar;
    @JsonProperty("device_on")
    private Boolean deviceOn;
    private Integer latitude;
    @Deprecated
    private String location;
    private Integer longitude;
    private String nickname;

}
