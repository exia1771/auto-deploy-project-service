package github.exia1771.deploy.core.controller;

import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.core.entity.Namespace;
import github.exia1771.deploy.core.service.NamespaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/namespace")
public class NamespaceController {

	private final NamespaceService service;

	@GetMapping("/list")
	public ResponseEntity<ResponseBody> findAll() {
		return CommonResponse.success(service.findAll().stream().map(Namespace::toDTO));
	}

	@GetMapping("/{keyword}")
	public ResponseEntity<ResponseBody> findByKeyword(@PathVariable("keyword") String keyword) {
		return CommonResponse.success(service.findByKeyword(keyword).stream().map(Namespace::toDTO));
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseBody> save(@RequestBody Namespace namespace) {
		return CommonResponse.success(service.save(namespace).toDTO());
	}
}
