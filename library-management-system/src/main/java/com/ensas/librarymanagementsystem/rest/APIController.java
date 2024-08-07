//package com.ensas.librarymanagementsystem.rest;
//
//
//import com.ensas.librarymanagementsystem.Model.security.User;
//import com.ensas.librarymanagementsystem.repositories.UserRepository;
//import com.ensas.librarymanagementsystem.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.List;
//
//@Controller
//public class APIController {
//
//    @Autowired
//    private UserService userService;
//    private BCryptPasswordEncoder passwordEncoder;
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/login")
//    public String showLoginForm() {
//
//        return "/login";
//    }
//
//
//
//}
