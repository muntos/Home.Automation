package external.tplink.domain;

import lombok.Getter;

@Getter
public class Handshake {
    private final String method = "handshake";
    private final HandshakeParams params;

    public Handshake(HandshakeParams params) {
        this.params = params;
    }

}
