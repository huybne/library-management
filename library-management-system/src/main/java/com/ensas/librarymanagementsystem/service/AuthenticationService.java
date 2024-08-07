package com.ensas.librarymanagementsystem.service;

import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.AuthenticationRequest;
import com.ensas.librarymanagementsystem.dto.request.LogoutRequest;
import com.ensas.librarymanagementsystem.dto.request.RefreshRequest;
import com.ensas.librarymanagementsystem.dto.request.VerifyRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthenticationResponse;
import com.ensas.librarymanagementsystem.dto.response.VerifyResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
     VerifyResponse verify(VerifyRequest request) throws JOSEException, ParseException, ParseException;

     AuthenticationResponse authenticate(AuthenticationRequest request) ;


    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    String generateToken(User user);

    void logout(LogoutRequest request) throws ParseException, JOSEException;
}
