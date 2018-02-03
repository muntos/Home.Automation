package home.network.automation.web;

import home.network.automation.components.Kodi;
import home.network.automation.tasks.CurtainControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kodi")
public class KodiController {
    @Autowired
    private Kodi kodi;
    @Autowired
    private CurtainControl curtainControl;

    @GetMapping
    @ResponseBody
    public ResponseEntity playbackStarted(@RequestParam("ev") String event){
       log.info("Received Kodi event '{}'", event);
       Kodi.Event ev = Kodi.Event.of(event);
       if (ev == null){
          log.error("Could not find any event in Kodi mappings named {}", event);
          return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
       }
       kodi.eventReceived(ev);

       return new ResponseEntity(HttpStatus.OK);
    }
}
