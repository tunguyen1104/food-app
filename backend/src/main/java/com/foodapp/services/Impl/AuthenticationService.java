package com.foodapp.services.Impl;

import com.foodapp.components.JwtTokenUtils;
import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Role;
import com.foodapp.domain.User;
import com.foodapp.dto.requests.UserLogoutRequest;
import com.foodapp.dto.requests.UserRegistrationRequest;
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
    public TokenResponse authenticate(String phone, String password) {
        logger.debug("Entering authenticate, phone={}", phone);
        User user;
        try {
            user = myUserService.loadUserByUsername(phone);
        } catch (UsernameNotFoundException exception) {
            logger.debug("User not found, phone={}", phone, exception);
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Incorrect password attempt, phone={}", phone);
            throw new AppException(ErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
        }
        if (!user.getEnabled()) {
            logger.info("Event: Account disabled, phone={}, userId={}", phone, user.getId());
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
        var token = TokenResponse.builder()
                .accessToken(jwtTokenUtils.generateToken(user))
                .refreshToken(UUID.randomUUID().toString())
                .build();
        redisTemplate.opsForValue().set(String.valueOf(user.getId()), token.getRefreshToken(), 30, TimeUnit.DAYS);
        logger.info("Event: User login success, phone={}, userId={}", phone, user.getId());
        logger.debug("Exiting authenticate, phone={}", phone);
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
        logger.info("Event: User register success, phone={}, userId={}", user.getPhone(), user.getId());
        return RegisterResponse.builder()
                .phone(user.getPhone())
                .username(user.getUsername())
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        logger.debug("Entering refreshToken, userId={}",
                SecurityContextHolder.getContext().getAuthentication() != null
                        ? ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
                        : "unknown");

        // Check if there is an authenticated user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("No authenticated user found for token refresh attempt");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        var user = (User) authentication.getPrincipal();
        logger.debug("Processing refresh for userId={}", user.getId());

        // Retrieve user refresh token in Redis
        String userRefreshToken = (String) redisTemplate.opsForValue().get(user.getId().toString());
        if (userRefreshToken == null) {
            logger.warn("No refresh token found in Redis for userId={}", user.getId());
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (!userRefreshToken.equals(refreshToken)) {
            logger.warn("Invalid refresh token provided for userId={}", user.getId());
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Generate new tokens
        String newAccessToken = jwtTokenUtils.generateToken(user);
        String newRefreshToken = UUID.randomUUID().toString();

        // Store new refresh token in Redis (replace old one)
        redisTemplate.opsForValue().set(user.getId().toString(), newRefreshToken); // Fixed: Store newRefreshToken, not newAccessToken
        logger.debug("Stored new refresh token in Redis for userId={}", user.getId());

        // Return new tokens
        TokenResponse response = TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        logger.info("Event: Token refreshed successfully, userId={}", user.getId());
        logger.debug("Exiting refreshToken, userId={}", user.getId());
        return response;
    }

    @Override
    public Boolean logout(UserLogoutRequest request) {
        logger.debug("Entering logout, request={}", request);

        // Retrieve authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("No authenticated user found for logout attempt");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        var user = (User) authentication.getPrincipal();
        logger.debug("Processing logout for userId={}", user.getId());

        // Remove refresh token from Redis
        redisTemplate.delete(user.getId().toString());
        logger.debug("Deleted refresh token from Redis for userId={}", user.getId());

        // Clear authentication context
        SecurityContextHolder.clearContext();
        logger.info("Event: User logged out successfully, userId={}", user.getId());
        logger.debug("Exiting logout, userId={}", user.getId());
        return true;
    }
}
