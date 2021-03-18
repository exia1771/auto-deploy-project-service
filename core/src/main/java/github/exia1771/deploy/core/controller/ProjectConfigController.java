package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.ProjectConfig;
import github.exia1771.deploy.core.service.ProjectConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
@AllArgsConstructor
public class ProjectConfigController {

	private final ProjectConfigService service;


	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody ProjectConfig projectConfig) {
		return CommonResponse.success(service.save(projectConfig));
	}

}
