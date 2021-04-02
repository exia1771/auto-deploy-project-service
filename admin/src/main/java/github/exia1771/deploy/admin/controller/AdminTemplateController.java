package github.exia1771.deploy.admin.controller;

import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminTemplateService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ImageTemplate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/template")
@AllArgsConstructor
public class AdminTemplateController {

	private final AdminTemplateService service;

	@GetMapping("/all")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAllDTO());
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findPaged(@RequestBody SearchEntity entity) {
		return CommonResponse.success(service.findPaged(entity));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody ImageTemplate template) {
		service.save(template);
		return CommonResponse.success();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
		service.deleteById(id);
		return CommonResponse.success();
	}

	@GetMapping("/name")
	public ResponseEntity<ResponseBody> findNames() {
		return CommonResponse.success(service.findName());
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<ResponseBody> findByName(@PathVariable("name") String name) {
		return CommonResponse.success(service.findByName(name).stream().map(ImageTemplate::toDTO));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseBody> findById(@PathVariable("id") String id) {
		return CommonResponse.success(service.findById(id).toDTO());
	}
}
