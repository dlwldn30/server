package com.arom.polisee.global.login.contoller;

import com.arom.polisee.global.login.service.TokenService;
import com.arom.polisee.global.login.common.BaseException;
import com.arom.polisee.global.login.common.BaseResponse;
import com.arom.polisee.global.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;

    @GetMapping("/login")
    public ResponseEntity<Void> getKakaoAuthUrl() {
        String kakaoAuthUrl = tokenService.getKakaoAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", kakaoAuthUrl)
                .build();
    }

    @GetMapping(value = "/login/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam(required = false) String code) {
        try {
            return loginService.loginWithKakao(code); // 서비스에서 JWT 발급 & 쿠키 설정
        } catch (BaseException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse<>(exception.getStatus().getMessage())); //  오류 메시지 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>("서버 오류 발생")); //  예기치 못한 오류 처리
        }
    }


}