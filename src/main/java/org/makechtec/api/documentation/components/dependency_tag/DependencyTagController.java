package org.makechtec.api.documentation.components.dependency_tag;


import org.makechtec.api.documentation.commons.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dependency-tag")
public class DependencyTagController {

    private final DependencyTagMapper dependencyTagMapper;

    public DependencyTagController(DependencyTagMapper dependencyTagMapper) {
        this.dependencyTagMapper = dependencyTagMapper;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String addDependencyTag(
            @RequestParam("projectName") String projectName,
            @RequestParam("groupId") String groupId,
            @RequestParam("artifactId") String artifactId
    ){
        dependencyTagMapper.create(projectName, groupId, artifactId);

        if(!dependencyTagMapper.existsDependencyTag(groupId, artifactId)){
            var response = new HttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error adding dependency tag", groupId + ":" + artifactId);
            return response.toJson();
        }

        var response = new HttpResponse<>(HttpStatus.CREATED.value(), "added dependency tag", groupId + ":" + artifactId );
        return response.toJson();
    }

}
