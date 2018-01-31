package home.network.automation.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kodi")
public class KodiController {

    @GetMapping
    public void playbackStarted(@RequestParam("title") String title){
       log.info("Received playback event for '{}'", title);
    }
}
