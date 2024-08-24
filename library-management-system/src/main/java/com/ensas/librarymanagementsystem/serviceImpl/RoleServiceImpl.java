package com.ensas.librarymanagementsystem.serviceImpl;


import com.ensas.librarymanagementsystem.Model.security.Authority;
import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.AddAuthoritiesToRole;
import com.ensas.librarymanagementsystem.dto.request.AddRoleToUser;
import com.ensas.librarymanagementsystem.dto.request.RoleRequest;
import com.ensas.librarymanagementsystem.dto.response.RoleResponse;
import com.ensas.librarymanagementsystem.exceptions.NotFoundException;
import com.ensas.librarymanagementsystem.mapper.RoleMapper;
import com.ensas.librarymanagementsystem.repositories.AuthorityRepository;
import com.ensas.librarymanagementsystem.repositories.RoleRepository;
import com.ensas.librarymanagementsystem.repositories.UserRepository;
import com.ensas.librarymanagementsystem.service.RoleService;
import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public RoleResponse create(RoleRequest request) {
        var authorities = authorityRepository.findAllByIdIn(request.getAuthorities());
        var role = roleMapper.toRole(request);
        role.setAuthorities(new HashSet<>(authorities));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }



    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public void delete(UUID roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public Map<String, Object> addAuthorityToRole(AddAuthoritiesToRole request) {
        String roleName = request.getRoles();
        List<String> authorityNames = request.getAuthorities();

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        // Sử dụng phương thức findAllByName với danh sách các tên riêng lẻ
        List<Authority> authorities = authorityRepository.findAllByName(authorityNames);

        // Lấy tên của các authorities thực tế từ cơ sở dữ liệu
        List<String> foundAuthorityNames = authorities.stream()
                .map(Authority::getName)
                .toList();

        // Tìm các authorities không tồn tại
        List<String> missingAuthorities = authorityNames.stream()
                .filter(name -> !foundAuthorityNames.contains(name))
                .collect(Collectors.toList());

        // Nếu có bất kỳ authorities nào không tồn tại, ném ngoại lệ
        if (!missingAuthorities.isEmpty()) {
            throw new NotFoundException("One or more authorities are not available: " + String.join(", ", missingAuthorities));
        }

        List<String> presentAuthorities = new ArrayList<>();
        List<String> unPresentAuthorities = new ArrayList<>();

        for (Authority authority : authorities) {
            if (!role.getAuthorities().contains(authority)) {
                role.getAuthorities().add(authority);
                presentAuthorities.add(authority.getName());
            } else {
                unPresentAuthorities.add(authority.getName());
            }
        }

        roleRepository.save(role);

        Map<String, Object> response = new HashMap<>();
        response.put("presentAuthorities", presentAuthorities);
        response.put("unPresentAuthorities", unPresentAuthorities);
        return response;
    }


    @Override
    public RoleResponse getRoleById(UUID roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
        return roleMapper.toRoleResponse(role);
    }
    @Override
    public RoleResponse getRoleByName(String name){
        Role role = roleRepository.findByName(name).orElseThrow(() -> new NotFoundException("Role not found"));
        return roleMapper.toRoleResponse(role);
    }
    @Override
    public RoleResponse updateRole(UUID roleId, RoleRequest request){
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
        if(StringUtils.isNotBlank(request.getName())){
            role.setName(request.getName());
        }
        if(StringUtils.isNotBlank(request.getPermission())){
            role.setPermission(request.getPermission());
        }

        return roleMapper.toRoleResponse(role);
    }
    
    @Override
    public RoleResponse addRoleToUser(AddRoleToUser request) {
        String roleName = request.getRoleName();
        Optional<Role> role = roleRepository.findByName(roleName);

        if (!role.isPresent()) {
            throw new NotFoundException("Role not found");
        }

        Optional<User> user = userRepository.findById(request.getUserId());

        if (!user.isPresent()) {
            throw new NotFoundException("User not found");
        }

        // Thêm role vào user
        User existingUser = user.get();
        existingUser.getRoles().add(role.get());

        // Lưu thay đổi vào database (nếu cần thiết)
        userRepository.save(existingUser);

        return roleMapper.toRoleResponse(role.get());
    }

}
