package home.network.automation.web;

import home.network.automation.model.CommandResult;
import home.network.automation.service.CommandsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "commands")
@Path("/commands")
public class DeviceController {
    @Autowired
    private CommandsService commandsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("press/{device}/{command}")
    public CommandResult pressRemoteButton(@PathParam("device") String deviceName, @PathParam("command") String commandName) {
        return commandsService.pressRemoteButton(deviceName, commandName);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lms/volume/{volumeValue}")
    public CommandResult changeLMSvolume(@PathParam("volumeValue") String value){
        //return commandsService.changeLMSvolume(value);
        return null;
    }
}