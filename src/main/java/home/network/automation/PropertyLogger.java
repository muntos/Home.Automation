package home.network.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PropertyLogger  {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLogger.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final Environment env = event.getApplicationContext().getEnvironment();
        LOGGER.info("===========================================");
        LOGGER.info("======= Active profiles: {} =======", Arrays.toString(env.getActiveProfiles()));
        LOGGER.info("===========================================");
    }
}
