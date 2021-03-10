package github.exia1771.deploy.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.service.ImageTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/template")
@AllArgsConstructor
public class ImageTemplateController {

    private final ImageTemplateService service;

    @PostMapping("/pageAll")
    public ResponseEntity<ResponseBody> pageAll(@RequestBody Page<ImageTemplate> page) {
        return CommonResponse.success(service.pageAll(page));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseBody> save(@RequestBody ImageTemplate template, BindingResult result) {
        return CommonResponse.success(service.save(template));
    }

    @PostMapping("/pageByName")
    public ResponseEntity<ResponseBody> pageByName(@RequestBody ImageTemplate template) {
        return CommonResponse.success(service.pageByName(template));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
        return CommonResponse.success(service.deleteById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<ResponseBody> findDistinctTemplateName() {
        return CommonResponse.success(service.findDistinctTemplateName());
    }

    @GetMapping("/name/{keyword}")
    public ResponseEntity<ResponseBody> findTemplateNameByKeyword(@PathVariable("keyword") String keyword) {
        return CommonResponse.success(service.findTemplateNameByKeyword(keyword));
    }

    @GetMapping("/tag/{templateName}")
    public ResponseEntity<ResponseBody> findTagsByTemplateName(@PathVariable("templateName") String templateName) {
        return CommonResponse.success(service.findTagsByTemplateName(templateName));
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseBody> findTemplateIdByTemplateNameAndTag(ImageTemplate template) {
        return CommonResponse.success(service.findIdByTemplateNameAndTag(template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody> findTemplateById(@PathVariable("id") String id) {
        return CommonResponse.success(service.findById(id).toDTO());
    }
}
