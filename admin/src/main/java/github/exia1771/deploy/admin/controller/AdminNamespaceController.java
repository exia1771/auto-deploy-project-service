package github.exia1771.deploy.admin.controller;

import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminNamespaceService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.Namespace;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/namespace")
@AllArgsConstructor
public class AdminNamespaceController {

	private final AdminNamespaceService service;

	@GetMapping("/all")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAllDTO());
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findPaged(@RequestBody SearchEntity entity) {
		return CommonResponse.success(service.findPagedDTO(entity));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody Namespace namespace) {
		service.save(namespace);
		return CommonResponse.success();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
		service.deleteById(id);
		return CommonResponse.success();
	}
}
