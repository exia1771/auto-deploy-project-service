package github.exia1771.deploy.common.controller;

import github.exia1771.deploy.common.entity.Password;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.CommonResponse;
import github.exia1771.deploy.common.util.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/check")
    public ResponseEntity<ResponseBody> isExistedName(@RequestParam("name") String name) {
        return CommonResponse.success(userService.isExistedName(name));
    }

    @PostMapping("/public/submit")
    public ResponseEntity<ResponseBody> submit(@RequestBody @Valid User user) throws UnsupportedEncodingException {
        return CommonResponse.success(userService.submit(user));
    }

    @PostMapping("/public/login")
    public ResponseEntity<ResponseBody> login(@RequestBody User user) throws UnsupportedEncodingException {
        return CommonResponse.success(userService.login(user));
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseBody> logout() {
        userService.logout();
        return CommonResponse.success();
    }

    @GetMapping("/login")
    public ResponseEntity<ResponseBody> login() {
        return CommonResponse.success(userService.login());
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseBody> updateBasicInfo(@RequestBody User user) {
        return CommonResponse.success(userService.updateBasicInfo(user));
    }

    @PostMapping("/avatar")
    public ResponseEntity<ResponseBody> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        return CommonResponse.success(userService.uploadAvatar(file));
    }

    @PutMapping("/password")
    public ResponseEntity<ResponseBody> changePassword(@RequestBody @Valid Password password) {
        userService.changePassword(password);
        return CommonResponse.success();
    }
}
