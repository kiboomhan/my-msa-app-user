package com.example.msaappuser.controller;

import com.example.msaappuser.dto.UserDto;
import com.example.msaappuser.service.UserService;
import com.example.msaappuser.vo.Greeting;
import com.example.msaappuser.vo.RequestUser;
import com.example.msaappuser.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private Environment env;
    private Greeting greeting;
    private UserService userService;

    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/status")
    public String status() {
        return String.format("msa-app-user > port %s", env.getProperty("local.server.port"));
    }

    @GetMapping("welcome")
    public String welcome() {
        //        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mm = new ModelMapper();
        mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mm.map(user, UserDto.class);
        userService.createUser(userDto);
//        "Create user method is called";

        ResponseUser responseUser = mm.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseUser);
    }

}
