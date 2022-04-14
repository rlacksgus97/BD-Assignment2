package com.bd.assignment2.user;

import com.bd.assignment2.user.dto.JoinReqDto;
import com.bd.assignment2.user.dto.JoinResDto;
import com.bd.assignment2.user.dto.LoginReqDto;
import com.bd.assignment2.user.dto.LoginResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<JoinResDto> join(@RequestBody JoinReqDto joinDto) {
        return ResponseEntity.ok(userService.join(joinDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Long> logout() {
        return ResponseEntity.ok(userService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResDto> refreshToken(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                                    @RequestHeader(value = "REFRESH-TOKEN") String refreshToken) {
        return ResponseEntity.ok(userService.refreshToken(accessToken, refreshToken));
    }

    @GetMapping("/test")
    public ResponseEntity<LoginResDto> test() {
        return ResponseEntity.ok(null);
    }
}
