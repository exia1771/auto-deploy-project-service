package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping("/save")
    public ResponseEntity<ResponseBody> save(@RequestBody Project project) {
        return CommonResponse.success(service.save(project).toDTO());
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseBody> findList() {
        return CommonResponse.success(service.findProjectsByCurrentUser());
    }

    @PostMapping("/list")
    public ResponseEntity<ResponseBody> findPagedList(@RequestBody Project project) {
        return CommonResponse.success(service.findProjectsByCurrentUser(project));
    }

    @PostMapping("/search/{keyword}")
    public ResponseEntity<ResponseBody> findPagedProjectsByKeyword(@RequestBody Project project,
                                                                   @PathVariable String keyword) {
        return CommonResponse.success(service.findPagedProjectsByKeyword(keyword, project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteProjectById(@PathVariable("id") String id) {
        return CommonResponse.success(service.deleteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateProjectById(@PathVariable("id") String id, @RequestBody Project project) {
        project.setId(id);
        return CommonResponse.success(service.save(project).toDTO());
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseBody> batchDeleteProjectsByIdList(@RequestBody List<String> id) {
        return CommonResponse.success(service.batchDeleteByIdList(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody> findProjectById(@PathVariable("id") String id) {
        return CommonResponse.success(service.findById(id).toDTO());
    }

}
