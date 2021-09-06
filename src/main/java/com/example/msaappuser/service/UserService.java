package com.example.msaappuser.service;

import com.example.msaappuser.dto.UserDto;
import com.example.msaappuser.jpa.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();





}
