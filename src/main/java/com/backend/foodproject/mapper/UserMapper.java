package com.backend.foodproject.mapper;

import com.backend.foodproject.dto.auth.UserRegisterDto;
import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserInfo toEntity(UserRegisterDto dto){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(dto.getName());
        userInfo.setEmail(dto.getEmail());
        userInfo.setPassword(dto.getPassword());
        userInfo.setRole(Role.ROLE_USER);
        userInfo.setPhoneNumber(dto.getPhoneNumber());
        userInfo.setEnabled(false);

        return userInfo;
    }
    public static UserInfo toEntityAdmin(UserRegisterDto dto){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(dto.getName());
        userInfo.setEmail(dto.getEmail());
        userInfo.setPassword(dto.getPassword());
        userInfo.setRole(Role.ROLE_ADMIN);
        userInfo.setPhoneNumber(dto.getPhoneNumber());
        userInfo.setEnabled(false);

        return userInfo;
    }
}
