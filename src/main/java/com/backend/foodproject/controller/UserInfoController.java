package com.backend.foodproject.controller;

import com.backend.foodproject.dto.ApiResponse;
import com.backend.foodproject.dto.auth.LoginRequestDto;
import com.backend.foodproject.dto.auth.UserRegisterDto;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.service.auth.JwtService;
import com.backend.foodproject.service.auth.UserInfoService;
import com.backend.foodproject.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;
    private final ResponseUtils responseUtils;
    private final JwtService jwtService;

    @GetMapping("/index")
    public String getWelcomePage(){
        return "...................Coming Soon...............................";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody UserRegisterDto registerDto
    ){
        String message = userInfoService.register(registerDto);
        return responseUtils.created(message);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<String>> registerAdmin(
            @Valid @RequestBody UserRegisterDto registerDto
    ){
        String message = userInfoService.registerAdmin(registerDto);
        return responseUtils.created(message);
    }

    @PostMapping("/login")
    public String login (@RequestBody LoginRequestDto requestDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword())
        );
        if(auth.isAuthenticated()){
            return jwtService.generateToken(requestDto.getEmail());
        }
        else {
            throw new CustomExceptionHandling("Invalid Email Request",
                    HttpStatus.NO_CONTENT.value());
        }

    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyUser(@RequestParam("token") String token){
        userInfoService.verifyUser(token);
        return responseUtils.ok("Email verified Successfully", null);
    }

}
