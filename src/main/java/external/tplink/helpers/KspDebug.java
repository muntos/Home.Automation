package external.tplink.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KspDebug {
    private static final Logger LOG = LoggerFactory.getLogger(KspDebug.class);

    public static void out(String content)
    {
        System.out.println(String.format("[TAPO-PoC] %s", content));
    }
}
