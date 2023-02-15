package ru.doggo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.doggo.controller.dto.Credentials;
import ru.doggo.controller.dto.CredentialsResponse;
import ru.doggo.controller.dto.TokenDto;
import ru.doggo.exceptions.DoggoException;
import ru.doggo.model.SecurityUser;
import ru.doggo.model.User;
import ru.doggo.repository.UserRepository;
import ru.doggo.service.JwtProvider;
import ru.doggo.service.TokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Deprecated
    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/refresh")
    public CredentialsResponse refreshAccessToken(@RequestBody @Valid TokenDto token) {
        String refreshToken = token.getRefreshToken();
        SecurityUser user = (SecurityUser) userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DoggoException("Refresh token not found"));
        jwtProvider.validateToken(refreshToken);
        String login = jwtProvider.getLoginFromToken(refreshToken);
        if(!login.equals(user.getUsername())) {
            throw new DoggoException("Invalid refresh token");
        }
        String newAccessToken = jwtProvider.generateAccessToken(login);
        String newRefreshToken = jwtProvider.generateRefreshToken(login);
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return CredentialsResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtProvider.getTokenLifetime())
                .build();
    }

    @PostMapping("/sign-in")
    public CredentialsResponse signIn(@RequestBody @Valid Credentials credentials) {
        Authentication auth = new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword());
        authenticationManager.authenticate(auth);
        String accessToken = jwtProvider.generateAccessToken(credentials.getLogin());
        String refreshToken = jwtProvider.generateRefreshToken(credentials.getLogin());
        User user = userRepository.findByUsername(credentials.getLogin())
                .orElseThrow(() -> new DoggoException("User not found"));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return CredentialsResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtProvider.getTokenLifetime())
                .build();
    }

}
