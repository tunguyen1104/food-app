package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Role;
import com.foodapp.domain.User;
import com.foodapp.dto.response.UserSettingsResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.repositories.UserRepository;
import com.foodapp.utils.AuthenticationFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    AuthenticationFacade authenticationFacade;

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
}
