package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ProjectUser;
import github.exia1771.deploy.core.service.ProjectUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projectUser")
@AllArgsConstructor
public class ProjectUserController {

	private final ProjectUserService service;

	@GetMapping("/project/{id}")
	public ResponseEntity<ResponseBody> findUsersByProjectId(@PathVariable("id") String projectId) {
		return CommonResponse.success(service.findByProjectId(projectId).stream().map(ProjectUser::getUserId).collect(Collectors.toList()));
	}

	@PostMapping("/project/{id}")
	public ResponseEntity<ResponseBody> batchUpdateMember(@PathVariable("id") String projectId,
														  @RequestBody List<String> userId) {
		return CommonResponse.success(service.batchUpdate(projectId, userId));
	}
}
