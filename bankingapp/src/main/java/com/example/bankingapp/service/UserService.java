package com.example.bankingapp.service;

import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.UserEntity;
import com.example.bankingapp.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    //Why do we need to override here
    //how does this work
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(email).orElseThrow();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(userEntity.getRole()));

        return new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }

    public UserDTO saveUser(UserDTO userDTO){

        UserEntity userEntity = new UserEntity(userDTO.getEmail(), userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
        return mapper.map(userEntity, UserDTO.class);
    }

    public UserDTO getUser(Long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow();

        return mapper.map(userEntity, UserDTO.class);
    }

    public List<UserDTO> getUsers(){
        List<UserEntity> userEntities = userRepository.findAll();

        List<UserDTO> userDTOs = userEntities.stream().map(userEntity -> mapper.map(userEntity, UserDTO.class)).collect(Collectors.toList());

        return userDTOs;
    }

    public UserDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User does not exist"));
        return mapper.map(user,UserDTO.class);
    }

}
