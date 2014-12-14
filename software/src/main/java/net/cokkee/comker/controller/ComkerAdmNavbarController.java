package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;
import net.cokkee.comker.storage.ComkerNavbarStorage;
import net.cokkee.comker.service.ComkerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "comker_adm_navbar", description = "Administration Navbar API")
@Controller
@RequestMapping("/comker/adm/navbar")
public class ComkerAdmNavbarController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerNavbarStorage navbarStorage = null;

    @Autowired(required = false)
    public void setNavbarStorage(ComkerNavbarStorage navbarStorage) {
        this.navbarStorage = navbarStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "Full tree of navigation nodes",
        notes = "Returns full tree of navigation nodes",
            response = ComkerNavNodeViewDTO.class)
    @RequestMapping(method = RequestMethod.GET, 
            value = "/tree", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerNavNodeViewDTO getTreeFromRoot() {
        return navbarStorage.getTree();
    }
    
    @ApiOperation(
            value = "Tree of Navigation Node",
            notes = "Returns Tree of Navigation Node",
            response = ComkerNavNodeViewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Navbar not found")})
    @RequestMapping(method = RequestMethod.GET, 
            value = "/tree/exclude/{excludeId}", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerNavNodeViewDTO getTreeFromRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true) 
            @PathVariable String excludeId) {
        return navbarStorage.getTree(null, excludeId);
    }
    
    @ApiOperation(
            value = "Tree of Navigation Node",
            notes = "Returns Tree of Navigation Node",
            response = ComkerNavNodeViewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Navbar not found")})
    @RequestMapping(method = RequestMethod.GET, 
            value = "/tree/from/{id}", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerNavNodeViewDTO getTreeFromNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true) 
            @PathVariable String id) {
        return navbarStorage.getTree(id, null);
    }
    
    @ApiOperation(
            value = "Full list of navigation nodes",
            notes = "Returns full list of navigation nodes",
            response = ComkerNavNodeViewDTO.class)
    @RequestMapping(method = RequestMethod.GET, 
            value = "/list", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComkerNavNodeViewDTO> getListFromRoot() {
        return navbarStorage.getList();
    }
    
    @ApiOperation(
            value = "All of navigation node exclude descendants of a navigation node",
            notes = "Returns list of navigation node exclude descendants of a navigation node",
            response = ComkerNavNodeViewDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Navbar not found")})
    @RequestMapping(method = RequestMethod.GET, 
            value = "/list/exclude/{excludeId}", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComkerNavNodeViewDTO> getListFromRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true) 
            @PathVariable String excludeId) {
        return navbarStorage.getList(null, excludeId);
    }
    
    @ApiOperation(
            value = "Descendants of a navigation node",
            notes = "Returns list of descendants of a navigation node",
            response = ComkerNavNodeViewDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Navbar not found")})
    @RequestMapping(method = RequestMethod.GET, 
            value = "/list/from/{id}", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComkerNavNodeViewDTO> getListFromNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true) 
            @PathVariable String id) {
        return navbarStorage.getList(id, null);
    }
}
