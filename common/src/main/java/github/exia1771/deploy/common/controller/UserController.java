package github.exia1771.deploy.common.controller;

import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseBody> isExistedName(@RequestParam("name") String name) {
        return CommonResponse.success(userService.isExistedName(name));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseBody> submit(@RequestBody User user) {
        return CommonResponse.success(userService.submit(user));
    }

}
