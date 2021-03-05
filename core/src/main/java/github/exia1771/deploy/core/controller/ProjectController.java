package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @PostMapping("/save")
    public ResponseEntity<ResponseBody> save(@RequestBody Project project) {
        return CommonResponse.success(service.save(project));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseBody> findList() {
        return CommonResponse.success(service.findProjectsByCurrentUser());
    }

}
