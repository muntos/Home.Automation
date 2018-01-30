package home.network.automation.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import home.network.automation.model.Log;

import java.util.List;

public interface InfoService {
    List<Log> displayLog();
    List<ILoggingEvent >getEvents();
}
