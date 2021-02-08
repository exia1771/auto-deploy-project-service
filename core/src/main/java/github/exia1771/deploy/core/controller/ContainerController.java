package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/container")
@RestController
public class ContainerController {

    @Autowired
    private ContainerService service;

    @PostMapping("/find")
    public ResponseEntity<ResponseBody> find(@RequestBody DockerRemoteApiParam param) {
        return CommonResponse.success(service.findByParam(param));
    }

}
