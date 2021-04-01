package github.exia1771.deploy.admin.controller;

import com.alibaba.fastjson.JSON;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminUserService;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@AllArgsConstructor
public class AdminUserController {

	private final AdminUserService service;

	@PostMapping("/public/login")
	public ResponseEntity<ResponseBody> login(@RequestBody User user) {
		return CommonResponse.success(service.login(user));
	}

	@GetMapping("/login")
	public ResponseEntity<ResponseBody> login() {
		return CommonResponse.success(service.login());
	}

	@GetMapping("/logout")
	public ResponseEntity<ResponseBody> logout() {
		service.logout();
		return CommonResponse.success();
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findUserBySearchTxt(@RequestBody SearchEntity entity) {
		return CommonResponse.success(service.findPagedDTO(entity));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseBody> deleteById(@PathVariable("id") String id) {
		service.deleteById(id);
		return CommonResponse.success();
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody User user) {
		service.save(user);
		return CommonResponse.success();
	}

	@GetMapping("/dept/{id}")
	public ResponseEntity<ResponseBody> findUsersByDeptId(@PathVariable("id") String id) {
		return CommonResponse.success(service.findUsersByDeptId(id));
	}

	@GetMapping("/dept")
	public ResponseEntity<ResponseBody> findUsersNotHasDept() {
		return CommonResponse.success(service.findUsersNotHaveDeptId());
	}

	@PostMapping("/dept/{id}")
	public ResponseEntity<ResponseBody> batchUpdateDeptId(@PathVariable("id") String deptId,
														  @RequestBody String userId) {
		service.batchUpdateDeptId(deptId, JSON.parseObject(userId).getJSONArray("userId").toJavaList(String.class));
		return CommonResponse.success();
	}
}
