package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Role;
import com.foodapp.domain.User;
import com.foodapp.dto.requests.CreateUserRequest;
import com.foodapp.dto.response.UserResponse;
import com.foodapp.dto.response.UserSettingsResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.UserMapper;
import com.foodapp.repositories.RoleRepository;
import com.foodapp.repositories.UserRepository;
import com.foodapp.utils.AuthenticationFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    AuthenticationFacade authenticationFacade;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String phone) throws UsernameNotFoundException {
        return userRepository.findUserByPhone(phone);
    }

    @PreAuthorize("hasRole('MANAGER')")
    public void enableEmployeeAccount(Long id) {
        if (id == null) throw new AppException(ErrorCode.INVALID_REQUEST);
        User loggedInUser = authenticationFacade.getAuthenticatedUser();

        if (!Role.MANAGER.equals(loggedInUser.getRole().getName())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setEnabled(true);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public void updateUserSettings(UserSettingsResponse request) {
        User user = authenticationFacade.getAuthenticatedUser();
        user.setTheme(request.getTheme());
        user.setNotification(request.getNotification());
        userRepository.save(user);
    }

    public UserSettingsResponse getUserSettings() {
        User user = authenticationFacade.getAuthenticatedUser();
        return new UserSettingsResponse(user.getTheme(), user.getNotification());
    }

    public UserResponse createUser(CreateUserRequest request) {
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
                .location(request.getLocation())
                .role(role)
                .enabled(true)
                .build();

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
