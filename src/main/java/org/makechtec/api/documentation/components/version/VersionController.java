package org.makechtec.api.documentation.components.version;

import org.makechtec.api.documentation.commons.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/version")
public class VersionController {

    private final VersionMapper versionMapper;

    @Autowired
    public VersionController(VersionMapper versionMapper) {
        this.versionMapper = versionMapper;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String addVersion(
            @RequestParam("projectName") String projectName,
            @RequestParam("versionId") String versionId,
            @RequestParam("details") String details
    ){
        versionMapper.create(projectName, versionId, details);

        if(!versionMapper.existVersion(projectName, versionId)){
            var response = new HttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error adding version", projectName + ":" + versionId);
            return response.toJson();
        }

        var response = new HttpResponse<>(HttpStatus.CREATED.value(), "added version", projectName + ":" + versionId );
        return response.toJson();
    }

}
