package home.network.automation;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import home.network.automation.service.InfoService;
import home.network.automation.service.InfoServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LogbackFilter extends Filter<ILoggingEvent> {


    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (ApplicationContextProvider.getApplicationContext() != null) {
            InfoService infoService = ApplicationContextProvider.getApplicationContext().getBean("infoServiceImpl", InfoServiceImpl.class);

            if (event.getLoggerName().contains(getClass().getPackage().getName())) {
                infoService.getEvents().add(event);
            }

            return FilterReply.ACCEPT;
        }
        return FilterReply.ACCEPT;
    }

}
