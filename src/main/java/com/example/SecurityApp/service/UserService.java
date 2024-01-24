package com.example.SecurityApp.service;

import com.example.SecurityApp.model.AuthenticationRequest;
import com.example.SecurityApp.model.AuthenticationResponse;
import com.example.SecurityApp.model.User;
import com.example.SecurityApp.repository.UserRepository;
import com.example.SecurityApp.request.UserRegistrationRequest;
import com.example.SecurityApp.request.UserUpdateRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data

public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User saveUser(UserRegistrationRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());

        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public AuthenticationResponse userLogin(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                authenticationRequest.getPassWord()));

        User user = userRepository.findByUsername(authenticationRequest.getUserName());

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

    public User getUserById(int id){
        return userRepository.findById(id).get();
    }
    public User updateUser(int id, UserUpdateRequest UpdateRequest){
        User toUpdate = getUserById(id);
        toUpdate.setUsername(UpdateRequest.getUsername());
        toUpdate.setPassword(UpdateRequest.getPassword());
        toUpdate.setRole(UpdateRequest.getRole());

        return userRepository.save(toUpdate);
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}


