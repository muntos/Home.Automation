package external.tplink.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
public class TapoResponse {
    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("result")
    private TapoResponseResult result;
}
