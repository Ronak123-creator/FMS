package com.backend.foodproject.service.auth;

import com.backend.foodproject.dto.auth.UserRegisterDto;
import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.enums.Role;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.mapper.UserMapper;
import com.backend.foodproject.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);
        return userInfo
                .map(UserInfoDetails::new)
                .orElseThrow(()-> new CustomExceptionHandling("Email Not Found" + email,
                        HttpStatus.NOT_FOUND.value()));
    }


    @PostConstruct
    public void createAdminIfNotExists(){
        if(!userInfoRepository.existsByRole(Role.ROLE_ADMIN)){
            UserInfo admin = new UserInfo();
            admin.setName("admin");
            admin.setEmail("admin@fms.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setPhoneNumber("1234567890");
            admin.setEnabled(true);
            userInfoRepository.save(admin);
            System.out.println("Admin Created");
        }
    }

    public String register(UserRegisterDto dto){
        if(userInfoRepository.existsByEmail(dto.getEmail())){
            throw  new CustomExceptionHandling("Email already exists",
                    HttpStatus.CONFLICT.value());
        }
        if(userInfoRepository.existsByPhoneNumber(dto.getPhoneNumber())){
            throw  new CustomExceptionHandling("Phone Number already exists",
                    HttpStatus.CONFLICT.value());
        }

        UserInfo userInfo = userMapper.toEntity(dto);
        userInfo.setPassword(passwordEncoder.encode(dto.getPassword()));

        userInfo.setEnabled(false);
        userInfo.setVerificationToken(UUID.randomUUID().toString());

        userInfoRepository.save(userInfo);
        emailService.sendVerificationEmail(userInfo);

        return "User Register Success.";
    }

    public String registerAdmin(UserRegisterDto dto){
        if(userInfoRepository.existsByEmail(dto.getEmail())){
            throw  new CustomExceptionHandling("Email already exists",
                    HttpStatus.CONFLICT.value());
        }
        if(userInfoRepository.existsByPhoneNumber(dto.getPhoneNumber())){
            throw  new CustomExceptionHandling("Phone Number already exists",
                    HttpStatus.CONFLICT.value());
        }

        UserInfo userInfo = userMapper.toEntityAdmin(dto);
        userInfo.setPassword(passwordEncoder.encode(dto.getPassword()));

        userInfo.setEnabled(false);
        userInfo.setVerificationToken(UUID.randomUUID().toString());

        userInfoRepository.save(userInfo);
        emailService.sendVerificationEmail(userInfo);

        return "Admin Register Success.";

    }

    public void verifyUser(String token){
        UserInfo userInfo = userInfoRepository.findByVerificationToken(token)
                .orElseThrow(()->new CustomExceptionHandling("Invalid token", HttpStatus.BAD_REQUEST.value()));
        userInfo.setEnabled(true);
        userInfo.setVerificationToken(null);
        userInfoRepository.save(userInfo);
    }
}
