package github.exia1771.deploy.admin.controller;

import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminDeptService;
import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/admin/dept")
@RestController
@AllArgsConstructor
public class AdminDeptController {

	private final AdminDeptService service;

	@GetMapping("/all")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAllDTO());
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findPaged(@RequestBody SearchEntity entity) {
		return CommonResponse.success(service.findPagedDTO(entity));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody Dept dept) {
		service.save(dept);
		return CommonResponse.success();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
		service.deleteById(id);
		return CommonResponse.success();
	}
}
