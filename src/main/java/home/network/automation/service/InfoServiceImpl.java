package home.network.automation.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import home.network.automation.model.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoServiceImpl implements InfoService {
    private List<ILoggingEvent> events = new ArrayList<>();

    @Override
    public List<Log> displayLog() {
        return events.stream()
                .sorted(Comparator.comparing(ILoggingEvent::getTimeStamp).reversed())
                .map(e -> new Log(e.getTimeStamp(), e.getLevel().toString(), e.getFormattedMessage()))
                .collect(Collectors.toList());

    }

    @Override
    public List<ILoggingEvent> getEvents() {
        return events;
    }
}
