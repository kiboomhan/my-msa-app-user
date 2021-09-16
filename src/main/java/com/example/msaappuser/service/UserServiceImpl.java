package com.example.msaappuser.service;

import com.example.msaappuser.client.OrderServiceClient;
import com.example.msaappuser.dto.UserDto;
import com.example.msaappuser.jpa.UserEntity;
import com.example.msaappuser.jpa.UserRepository;
import com.example.msaappuser.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder;

    Environment env;

    OrderServiceClient orderServiceClient;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if ( userEntity == null )
            throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mm = new ModelMapper();
        mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mm.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return mm.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        /* 미구현 */
//        List<ResponseOrder> orders = new ArrayList<>();

        /* Rest Template 활용 */
        /*String orderUrl = String.format(env.getProperty("order-service.url"), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<ResponseOrder>>() {});
        List<ResponseOrder> ordersList = orderListResponse.getBody();*/

        /* Feign Client 활용 */
//        List<ResponseOrder> ordersList = null;
//        try {
//            ordersList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }

        List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);

        userDto.setOrders(ordersList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
}
