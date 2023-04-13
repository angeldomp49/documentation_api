package org.makechtec.api.documentation.components.project;

import lombok.extern.java.Log;
import org.makechtec.api.documentation.commons.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("/project")
public class ProjectController {


    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectController(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String index(){

        var response = new HttpResponse<>(HttpStatus.OK.value(), "all project names", projectMapper.allProjectNames());

        return response.toJson();
    }

    @GetMapping("/show/{projectName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String show(@PathVariable("projectName") String projectName){

        var project = projectMapper.byName(projectName);

        if(project.isEmpty()){
            var response = new HttpResponse<>(HttpStatus.NOT_FOUND.value(), "Not found project with specified name", projectName);
            return response.toJson();
        }

        var response = new HttpResponse<>(HttpStatus.OK.value(), "all projectNames", project.get());
        return response.toJson();
    }

    @GetMapping("/hydrated")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String indexHydrated(){

        var response = new HttpResponse<>(HttpStatus.OK.value(), "all hydrated projects", projectMapper.allHydratedProjects());

        return response.toJson();
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String create( @RequestParam("projectName") String projectName ){
        projectMapper.create(projectName);

        var project = projectMapper.byName(projectName);

        if(project.isEmpty()){
            var response = new HttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not created project", projectName);
            return response.toJson();
        }

        var response = new HttpResponse<>(HttpStatus.CREATED.value(), "created project", project.get());

        return response.toJson();
    }

    @DeleteMapping("/{projectName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable("projectName") String projectName){
        projectMapper.deleteProject(projectName);

        var response = new HttpResponse<>(HttpStatus.NO_CONTENT.value(), "deleted project", "{\"projectName\": " + projectName +" }");

        return response.toJson();
    }

}
