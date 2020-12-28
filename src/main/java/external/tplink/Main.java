package external.tplink;

import com.google.gson.JsonObject;
import external.tplink.domain.HandshakeResponse;
import external.tplink.domain.KspKeyPair;
import external.tplink.helpers.KspDebug;
import home.network.automation.devices.generic.SmartPlug;
import home.network.automation.devices.tplink.TapoLogin;
import home.network.automation.devices.tplink.TapoP100Plug;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;


public class Main {

    public static void main(String[] args) throws Exception {//throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException {


/*
        Security.addProvider(new BouncyCastleProvider());
        KspDebug.out("Generating keypair...");
        TapoFlow tapoFlow = new TapoFlow("192.168.1.110");

        KspKeyPair kspKeyPair = KspEncryption.generateKeyPair();



        KspDebug.out("Sending handshake!");
        HandshakeResponse handshakeResponse = tapoFlow.makeHandshake(kspKeyPair);
        if (handshakeResponse == null) {
            System.exit(1);
        }


        String keyFromTapo = handshakeResponse.getResponse().getAsJsonObject("result").get("key").getAsString();
        KspDebug.out("Tapo's key is: " + keyFromTapo);
        KspDebug.out("Our session cookie is: " + handshakeResponse.getCookie());

        KspDebug.out("Will try to decode it!");
        C658a c658a = KspEncryption.decodeTapoKey(keyFromTapo, kspKeyPair);
        if (c658a == null) {
            System.exit(1);
        }
        KspDebug.out("Decoded!");

        KspDebug.out("Will try to login!");
        JsonObject resp = tapoFlow.loginRequest("alex.munteanu@gmail.com", "v8aD364VM6XWY_W", c658a, handshakeResponse.getCookie());
        String token = resp.getAsJsonObject("result").get("token").getAsString();
        KspDebug.out("Got token: "+token);

        tapoFlow.setPlugState(c658a, token, handshakeResponse.getCookie(), true);
        tapoFlow.getPlugState(c658a, token, handshakeResponse.getCookie());
*/

        TapoP100Plug tapoP100Plug = new TapoP100Plug("Tapo P100 connected to Hegel H80", "P100_H80", "192.168.1.110", new TapoLogin("alex.munteanu@gmail.com", "v8aD364VM6XWY_W"), 60, 60);
        tapoP100Plug.setStatusNow(SmartPlug.Status.ON);

    }
}
