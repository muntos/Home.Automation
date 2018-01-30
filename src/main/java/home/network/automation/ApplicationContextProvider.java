package home.network.automation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
    static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        setContext(ctx);
    }

    public static void setContext(ApplicationContext ctx){
        ApplicationContextProvider.context = ctx;
    }
}