package com.ensas.librarymanagementsystem.serviceImpl;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.ChangePasswordRequest;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;
import com.ensas.librarymanagementsystem.exceptions.NotFoundException;
import com.ensas.librarymanagementsystem.exceptions.UserNotFoundException;
import com.ensas.librarymanagementsystem.mapper.UserMapper;
import com.ensas.librarymanagementsystem.repositories.RoleRepository;
import com.ensas.librarymanagementsystem.repositories.UserRepository;
import com.ensas.librarymanagementsystem.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;
        private final UserMapper userMapper;
//    private final RoleService roleService;
//    private final RoleMapper roleMapper;

    @Override
        public UserResponse getUser(UUID id){
            return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found")));
        }
        @Transactional
        @Override
        public Page<UserResponse> getAllUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        String keywordWithWildcard = "%" + keyword.toLowerCase() + "%";
        Page<User> userPage = userRepository.findAllSort(keywordWithWildcard, pageable);

        Page<UserResponse> user = userPage.map(userMapper::toUserResponse);

        return user;
    }
        @Transactional
        @Override
        public List<UserResponse> getUsers() {
            return userRepository.findAll().stream()
                    .map(userMapper::toUserResponse)
                    .collect(Collectors.toList());
        }

        @Override
        public UserResponse createUser(UserCreationRequest request) {
            if (userRepository.existsByUsername(request.getUsername()))
                throw new RuntimeException("username already exists");

            User user= userMapper.toUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                Role defaultRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Default role not found"));
                Set<Role> roles = new HashSet<>();
                roles.add(defaultRole);
                user.setRoles(roles);
            }
            user = userRepository.save(user);
            return userMapper.toUserResponse(user);
        }

        @Override
        public User saveUser(User user) {
            if (checkIfUserExists(user.getUsername())) {
                throw new RuntimeException("User already exists");
            }
            return userRepository.save(hashPassword(user));
        }
        @Override
        public void deleteUser(UUID id){
            userRepository.deleteById(id);
        }


        @Override
        public UserResponse getMyInfo(){
            var context = SecurityContextHolder.getContext();
            String name = context.getAuthentication().getName();
            User user  = userRepository.findByUsername(name)
                    .orElseThrow(
                            ()-> new UserNotFoundException("User not found")
                    );
            return userMapper.toUserResponse(user);
        }
    @Override
    public UserResponse UpdateUser(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User does not exist"));


        if(StringUtils.isNotBlank(request.getFirstName())){
            user.setFirstName(request.getFirstName());
        }
        if(StringUtils.isNotBlank(request.getLastName())){
            user.setLastName(request.getLastName());
        }
        if(StringUtils.isNotBlank(request.getEmail())){
            user.setEmail(request.getEmail());
        }
        if(StringUtils.isNotBlank(request.getTelephone())){
            user.setTelephone(request.getTelephone());
        }
        if(StringUtils.isNotBlank(request.getAddress())){
            user.setAddress(request.getAddress());
        }
//        if(StringUtils.isNotBlank(request.getPassword())){
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
        if (request.getCreatedDate() != null) {
            user.setCreatedDate(request.getCreatedDate());
        }
        if(request.getLastModifiedDate() != null){
            user.setLastModifiedDate(request.getLastModifiedDate());
        }

        // Update roles if provided in the request
//        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
//            List<Role> roles = roleRepository.findAllById(request.getRoles().stream().map(UUID::fromString).collect(Collectors.toList()));
//            user.setRoles(new HashSet<>(roles));
//        }

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Override
    public void changePassword(UUID userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Xác nhận mật khẩu mới không trùng với mật khẩu cũ
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        // Đổi mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }
    @Override
        public void addRolesToUser(UUID uuid, List<Role> roles) {
            if (roles != null && !roles.isEmpty()) {
                User user = userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
                Set<Role> userRoles = user.getRoles();

                if (userRoles == null) {
                    userRoles = new HashSet<>();
                    user.setRoles(userRoles);
                }

                Set<String> roleNames = userRoles.stream().map(Role::getName).collect(Collectors.toSet());

                List<Role> rolesToAdd = roles.stream()
                        .filter(role -> !roleNames.contains(role.getName()))
                        .toList();

                userRoles.addAll(rolesToAdd);
                userRepository.save(user);
            }
        }


        @Override
        public User loadUserByUserName(String username) {
            return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"))    ;
        }
        public boolean checkIfUserExists(String username){
            return userRepository.findByUsername(username).isPresent();
        }
        private User hashPassword(User user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return  user;
        }
}
