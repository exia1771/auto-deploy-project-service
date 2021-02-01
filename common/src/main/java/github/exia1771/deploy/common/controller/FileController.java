package github.exia1771.deploy.common.controller;

import github.exia1771.deploy.common.entity.dto.FileDTO;
import github.exia1771.deploy.common.service.FileService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseBody> upload(@RequestBody MultipartFile file) {
        return CommonResponse.success(fileService.upload(file));
    }

}
