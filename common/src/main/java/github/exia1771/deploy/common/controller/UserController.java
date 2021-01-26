package github.exia1771.deploy.common.controller;

import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import org.springframework.http.HttpStatus;
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
        String USER_NAME_EXISTED = "用户名已存在!";
        if (userService.isExistedName(name)) {
            return CommonResponse.of(name, HttpStatus.CONFLICT, USER_NAME_EXISTED);
        } else {
            return CommonResponse.success(name);
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseBody> submit(@RequestBody User user) {
        if (userService.save(user)) {
            return CommonResponse.success(user);
        } else {
            return CommonResponse.error(user);
        }
    }


}
