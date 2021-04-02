package github.exia1771.deploy.admin.controller;

import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminProjectService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.Project;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/project")
@AllArgsConstructor
public class AdminProjectController {

	private final AdminProjectService service;

	@GetMapping("/all")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAllDTO());
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findPaged(@RequestBody SearchEntity entity) {
		return CommonResponse.success(service.findPagedDTO(entity));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody Project project) {
		service.save(project);
		return CommonResponse.success();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
		service.deleteById(id);
		return CommonResponse.success();
	}

}
