package home.network.automation.devices.tplink;

import com.google.gson.JsonObject;
import external.tplink.C658a;
import external.tplink.KspEncryption;
import external.tplink.TapoFlow;
import external.tplink.domain.HandshakeResponse;
import external.tplink.domain.KspKeyPair;
import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.model.CommandResult;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.joda.time.DateTime;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

@Slf4j
@ToString
public class TapoP100Plug extends SmartPlug {
    private String ipAddress;
    private TapoLogin tapoLogin;

    public TapoP100Plug(String name, String shortName, String ipAddress, TapoLogin tapoLogin,  int waitBeforeTurnOff, int minWaitBeforeStatesChange) {
        super(name, shortName, waitBeforeTurnOff, minWaitBeforeStatesChange);
        this.ipAddress = ipAddress;
        this.tapoLogin = tapoLogin;
    }

    public CommandResult setStatusNow(Status status) {
        Boolean success = setPlugState(value(status));
        log.info("'{}' (IP = {}) set status to '{}' returned {}", shortName, ipAddress, status, success);
        if (success){
            lastStatusChange = new DateTime();
        }
        return new CommandResult(success, String.format("Successfully set plug '%s' state to %s", shortName, status));
    }

    @Override
    public Status getStatus() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            TapoFlow tapoFlow = new TapoFlow(ipAddress);

            KspKeyPair kspKeyPair = KspEncryption.generateKeyPair();

            HandshakeResponse handshakeResponse = tapoFlow.makeHandshake(kspKeyPair);
            if (handshakeResponse == null) {
                log.error("No handshakeResponse for plug {} !", this);
                return Status.UNKNOWN;
            }

            String keyFromTapo = handshakeResponse.getResponse().getAsJsonObject("result").get("key").getAsString();
            log.debug("Tapo's key for {} is: {}", this, keyFromTapo);
            log.debug("Tapo session cookie for {} is: {}", this, handshakeResponse.getCookie());

            C658a c658a = KspEncryption.decodeTapoKey(keyFromTapo, kspKeyPair);
            if (c658a == null) {
                log.error("Could not decode Tapo key for {} !", this);
                return Status.UNKNOWN;
            }

            JsonObject resp = tapoFlow.loginRequest(tapoLogin.getUsername(), tapoLogin.getPassword(), c658a, handshakeResponse.getCookie());
            String token = resp.getAsJsonObject("result").get("token").getAsString();

            return tapoFlow.getPlugState(c658a, token, handshakeResponse.getCookie());
        } catch (NoSuchAlgorithmException ex) {
            log.error("Error trying to get plug {} state", this);
            return Status.UNKNOWN;
        }
    }

    private Boolean setPlugState(Boolean deviceOn) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            TapoFlow tapoFlow = new TapoFlow(ipAddress);

            KspKeyPair kspKeyPair = KspEncryption.generateKeyPair();

            HandshakeResponse handshakeResponse = tapoFlow.makeHandshake(kspKeyPair);
            if (handshakeResponse == null) {
                log.error("No handshakeResponse for plug {} !", this);
                return false;
            }

            String keyFromTapo = handshakeResponse.getResponse().getAsJsonObject("result").get("key").getAsString();
            log.debug("Tapo's key for {} is: {}", this, keyFromTapo);
            log.debug("Tapo session cookie for {} is: {}", this, handshakeResponse.getCookie());

            C658a c658a = KspEncryption.decodeTapoKey(keyFromTapo, kspKeyPair);
            if (c658a == null) {
                log.error("Could not decode Tapo key for {} !", this);
                return false;
            }

            JsonObject resp = tapoFlow.loginRequest(tapoLogin.getUsername(), tapoLogin.getPassword(), c658a, handshakeResponse.getCookie());
            String token = resp.getAsJsonObject("result").get("token").getAsString();

            tapoFlow.setPlugState(c658a, token, handshakeResponse.getCookie(), deviceOn);
            log.info("Set plug '{}' state to {}", this, deviceOn);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Error trying to set plug {} state to {}", this, deviceOn);
            return false;
        }

        return true;
    }




}
