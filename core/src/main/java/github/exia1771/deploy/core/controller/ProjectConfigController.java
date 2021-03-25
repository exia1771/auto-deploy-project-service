package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ProjectConfig;
import github.exia1771.deploy.core.service.ProjectConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config")
@AllArgsConstructor
public class ProjectConfigController {

	private final ProjectConfigService service;

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody ProjectConfig projectConfig) {
		return CommonResponse.success(service.save(projectConfig).toDTO());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseBody> findById(@PathVariable("id") String id) {
		return CommonResponse.success(service.findById(id).toDTO());
	}

	@GetMapping
	public ResponseEntity<ResponseBody> findByProjectIdAndNamespaceId(@RequestParam("projectId") String projectId,
																	  @RequestParam("namespaceId") String namespaceId) {
		return CommonResponse.success(service.findByProjectIdAndNamespaceId(projectId, namespaceId));
	}
}
