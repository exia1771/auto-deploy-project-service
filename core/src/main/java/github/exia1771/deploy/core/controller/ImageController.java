package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService service;

    @PostMapping("/find")
    public ResponseEntity<ResponseBody> find(@RequestBody DockerRemoteApiParam param) {
        return CommonResponse.success(service.findByParam(param));
    }

    @GetMapping("/inspect/{id}")
    public ResponseEntity<ResponseBody> inspect(@PathVariable("id") String id) {
        return CommonResponse.success(service.inspect(id));
    }
}
