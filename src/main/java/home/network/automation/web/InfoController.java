package home.network.automation.web;

import home.network.automation.model.Log;
import home.network.automation.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/log")
    public  List<Log> displayLog(){
        return infoService.displayLog();
    }

    @DeleteMapping("/log")
    public void clearLog(){
        infoService.clearLog();
    }

}
