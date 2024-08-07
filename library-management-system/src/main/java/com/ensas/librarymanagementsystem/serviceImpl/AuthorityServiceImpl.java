package com.ensas.librarymanagementsystem.serviceImpl;

import com.ensas.librarymanagementsystem.Model.security.Authority;
import com.ensas.librarymanagementsystem.dto.request.AuthorityRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthorityResponse;
import com.ensas.librarymanagementsystem.exceptions.NotFoundException;
import com.ensas.librarymanagementsystem.mapper.AuthorityMapper;
import com.ensas.librarymanagementsystem.repositories.AuthorityRepository;
import com.ensas.librarymanagementsystem.service.AuthorityService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorityServiceImpl implements AuthorityService {
    AuthorityRepository authorityRepository;
    AuthorityMapper authorityMapper;


    @Override
    public AuthorityResponse create(AuthorityRequest request){
        Authority authority = authorityMapper.toAuthority(request);
        authority = authorityRepository.save(authority);
        return authorityMapper.toAuthorityResponse(authority);
    }
    @Override
    public List<AuthorityResponse> getAll(){
        var authorities = authorityRepository.findAll();
        return authorities.stream().map(authorityMapper::toAuthorityResponse).toList();
    }
    @Override
    public void delete(UUID authorityId){
        authorityRepository.deleteById(authorityId);
    }
    @Override
    public AuthorityResponse getAuthorityById(UUID id){
        Authority authority = authorityRepository.findById(id).orElseThrow(() -> new NotFoundException("Authority not found"));
        return authorityMapper.toAuthorityResponse(authority);
    }
    @Override
    public AuthorityResponse getAuthorityByName(String name){
        Authority authority = authorityRepository.findByName(name).orElseThrow(() -> new NotFoundException("Authority not found"));
        return authorityMapper.toAuthorityResponse(authority);
    }
    @Override
    public AuthorityResponse updateAuthority(UUID AuthorityId, AuthorityRequest request){
        Authority authority = authorityRepository.findById(AuthorityId).orElseThrow(() -> new NotFoundException("Authority not found"));
        if(StringUtils.isNotBlank(request.getName())){
            authority.setName(request.getName());
        }
        if(StringUtils.isNotBlank(request.getPermission())){
            authority.setPermission(request.getPermission());
        }

        return authorityMapper.toAuthorityResponse(authorityRepository.save(authority));
    }
}
