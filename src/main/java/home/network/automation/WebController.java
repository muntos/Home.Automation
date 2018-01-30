package home.network.automation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by amunteanu on 30.01.2018.
 */
@Controller
public class WebController {

    @GetMapping(value="/log")
    public String homepage(){
        return "log";
    }
}
