package github.exia1771.deploy.admin.controller;

import com.alibaba.fastjson.JSON;
import github.exia1771.deploy.admin.service.AdminProjectUserService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ProjectUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/projectUser")
@AllArgsConstructor
public class AdminProjectUserController {

	private final AdminProjectUserService service;

	@PostMapping("/project/{id}")
	public ResponseEntity<ResponseBody> batchUpdate(@PathVariable("id") String projectId,
													@RequestBody String userIdList) {
		service.batchUpdate(projectId, JSON.parseArray(userIdList).toJavaList(String.class));
		return CommonResponse.success();
	}

	@GetMapping("/project/{id}")
	public ResponseEntity<ResponseBody> findByProjectId(@PathVariable("id") String id) {
		return CommonResponse.success(service.findUsersByProjectId(id).stream().map(ProjectUser::getUserId).collect(Collectors.toList()));
	}
}
