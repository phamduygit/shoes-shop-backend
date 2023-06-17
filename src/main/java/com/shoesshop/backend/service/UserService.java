package com.shoesshop.backend.service;

import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.entity.UserRequest;
import com.shoesshop.backend.entity.UserResponse;
import com.shoesshop.backend.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByEmail(currentUserName).orElseThrow();

            return UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .avatar(user.getAvatar())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
        return null;
    }

    public UserResponse updateUserInfo(UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByEmail(currentUserName).orElseThrow();

            user.setAvatar(userRequest.getAvatar());
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setPhoneNumber(userRequest.getPhoneNumber());

            userRepository.save(user);

            return UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .avatar(user.getAvatar())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
        return null;
    }
}
