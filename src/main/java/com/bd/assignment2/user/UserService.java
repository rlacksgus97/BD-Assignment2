package com.bd.assignment2.user;

import com.bd.assignment2.config.jwt.JwtService;
import com.bd.assignment2.user.dto.JoinReqDto;
import com.bd.assignment2.user.dto.JoinResDto;
import com.bd.assignment2.user.dto.LoginReqDto;
import com.bd.assignment2.user.dto.LoginResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResDto join(JoinReqDto joinDto) {
        Optional<User> user = userRepository.findByEmail(joinDto.getEmail());
        if (user.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        } else {
            User newUser = User.builder()
                    .email(joinDto.getEmail())
                    .password(passwordEncoder.encode(joinDto.getPassword()))
                    .build();
            return new JoinResDto(userRepository.save(newUser).getId());
        }
    }

    @Transactional
    public LoginResDto login(LoginReqDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            String refreshToken = jwtService.createRefreshToken();
            user.updateRefreshToken(refreshToken);
            return new LoginResDto(jwtService.createAccessToken(user.getId()), refreshToken);
        } else {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
    }

    @Transactional
    public Long logout() {
        Long userId = jwtService.getTokenInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        user.updateRefreshToken("invalidate");
        return userId;
    }

    @Transactional
    public LoginResDto refreshToken(String accessToken, String refreshToken) {
        // 유효기간이 만료되지 않은 토큰
        if (!jwtService.isValidExceptExp(accessToken)) {
            throw new RuntimeException("아직 유효기간이 만료되지 않은 토큰입니다.");
        }
        User user = userRepository.findById(jwtService.getTokenInfo())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        if (!jwtService.isValid(accessToken) || !refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 accessToken 또는 refreshToken입니다.");
        }
        user.updateRefreshToken(jwtService.createRefreshToken());
        return new LoginResDto(jwtService.createAccessToken(user.getId()), refreshToken);
    }
}
