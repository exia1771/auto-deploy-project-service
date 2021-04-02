package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ProjectContainer;
import github.exia1771.deploy.core.entity.ProjectContainerSearch;
import github.exia1771.deploy.core.service.ProjectContainerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/project/container")
@AllArgsConstructor
public class ProjectContainerController {

	private final ProjectContainerService service;

	@GetMapping("/tags/{projectId}")
	public ResponseEntity<ResponseBody> findRepositoryTags(@PathVariable("projectId") String projectId,
														   @RequestParam("keyword") String keyword) {
		return CommonResponse.success(service.findRepositoryTags(projectId, keyword));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody ProjectContainer projectContainer) {
		return CommonResponse.success(service.save(projectContainer));
	}

	@PostMapping("/save/{id}")
	public ResponseEntity<ResponseBody> saveById(@PathVariable("id") String id) {
		ProjectContainer container = service.findById(id);
		container.setId(null);
		return CommonResponse.success(service.save(container));
	}

	@PostMapping("/find")
	public ResponseEntity<ResponseBody> findBySpecificFields(@RequestBody ProjectContainerSearch search) {
		return CommonResponse.success(service.findBySpecificFields(search));
	}

	@GetMapping("/buildLog")
	public ResponseEntity<ResponseBody> findBuildLogById(@RequestParam("id") String id) {
		return CommonResponse.success(service.getBuildLog(id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseBody> findById(@PathVariable("id") String id) {
		return CommonResponse.success(service.findById(id).toDTO());
	}

	@GetMapping("/stop/{id}")
	public ResponseEntity<ResponseBody> stopBuildByProjectId(@PathVariable("id") String id) {
		return CommonResponse.success(service.stopBuild(id));
	}

	@GetMapping("/log")
	public ResponseEntity<ResponseBody> findContainerLogById(@RequestParam("id") String id, @RequestParam("since") long timestamp) {
		return CommonResponse.success(service.getContainerLog(id, timestamp));
	}

	@PostMapping("/log")
	public ResponseEntity<ResponseBody> findContainerLogStream(@RequestParam("id") String id,
															   @RequestParam("since") long timestamp,
															   HttpServletResponse response) {
		service.getContainerLogStream(id, timestamp, response);
		return CommonResponse.success();
	}

	@PostMapping("/restart/{id}")
	public ResponseEntity<ResponseBody> restartContainer(@PathVariable("id") String id) {
		return CommonResponse.success(service.restartContainer(id).getDescribe());
	}
}
