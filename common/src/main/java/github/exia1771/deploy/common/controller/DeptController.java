package github.exia1771.deploy.common.controller;

import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.service.DeptService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept")
@AllArgsConstructor
public class DeptController {

	private final DeptService service;

	@GetMapping("/all")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAll().stream().map(Dept::toDTO));
	}


}
