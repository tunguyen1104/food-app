package com.foodapp.services.Impl;

import com.foodapp.components.JwtTokenUtils;
import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Role;
import com.foodapp.domain.User;
import com.foodapp.dto.requests.UserLogoutRequest;
import com.foodapp.dto.requests.UserRegistrationRequest;
import com.foodapp.dto.response.LoginResponse;
import com.foodapp.dto.response.RegisterResponse;
import com.foodapp.dto.response.TokenResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.repositories.UserRepository;
import com.foodapp.services.IAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {
    static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    UserService myUserService;
    com.foodapp.repositories.RoleRepository roleRepository;
    UserRepository userRepository;
    JwtTokenUtils jwtTokenUtils;
    PasswordEncoder passwordEncoder;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginResponse authenticate(String phone, String password) {
        User user;
        try {
            user = myUserService.loadUserByUsername(phone);
        } catch (UsernameNotFoundException exception) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
        }
        if (!user.getEnabled()) {
            logger.info("User account disabled {}", phone);
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
        var token = LoginResponse.builder()
                .accessToken(jwtTokenUtils.generateToken(user))
                .refreshToken(UUID.randomUUID().toString())
                .build();
        // Save refresh token to redis with Key = refresh token, Value = userId
        redisTemplate.opsForValue().set(String.valueOf(user.getId()), token.getRefreshToken(), 30, TimeUnit.DAYS);
        logger.info("User logged in {}", phone);
        return token;
    }

    @Override
    public RegisterResponse register(UserRegistrationRequest request) {
        if (userRepository.existsMyUserByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.USED_PHONE);
        }
        var role = roleRepository.findByName(Role.EMPLOYEE).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        var user = User.builder()
                .userName(request.getUserName())
                .phone(request.getPhone())
                .fullName(String.format("%s %s", request.getFirstName(), request.getLastName()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(false)
                .build();
        userRepository.save(user);
        return RegisterResponse.builder()
                .phone(user.getPhone())
                .username(user.getUsername())
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        // Check if there is an authenticated user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        var user = (User) authentication.getPrincipal();
        // Retrieve user refresh token in Redis
        String userRefreshToken = (String) redisTemplate.opsForValue().get(user.getId().toString());
        if (userRefreshToken == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (!userRefreshToken.equals(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Generate new tokens
        String newAccessToken = jwtTokenUtils.generateToken(user);
        String newRefreshToken = UUID.randomUUID().toString();

        // Store new refresh token in Redis (replace old one)
        redisTemplate.opsForValue().set(user.getId().toString(), newAccessToken);

        // Return new tokens
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public Boolean logout(UserLogoutRequest request) {
        // Retrieve authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        var user = (User) authentication.getPrincipal();
        // Remove refresh token from Redis
        redisTemplate.delete(user.getId().toString());

        // Clear authentication context
        SecurityContextHolder.clearContext();

        return true;
    }
}
