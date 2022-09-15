package com.example.shorturl.service.user;

import com.example.shorturl.dao.RoleRepository;
import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.utils.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserEntity signUp(UserRequest userRequest) throws UserException {

        Optional<UserEntity> optionalUrl = userRepository.findByUserName(userRequest.getUserName());

        if(optionalUrl.isPresent())
            throw new ResponseStatusException(HttpStatus.GONE, "user name already used");

        optionalUrl = userRepository.findByEmail(userRequest.getEmail());

        if(optionalUrl.isPresent())
            throw new ResponseStatusException(HttpStatus.GONE, "email already used");

        UserEntity userEntity = UserEntity.builder()
                .userName(userRequest.getUserName())
                //TODO: Encode password
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(userRequest.getRoles())
                .build();

        Optional<RoleEntity> optionalRole = roleRepository.findByRoleName("ROLE_USER");

        RoleEntity role = optionalRole.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exists"));

        userEntity.addRole(role);

        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> findUserById(Integer id){
        return userRepository.findById(id);
    }
    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }

    public Optional<RoleEntity> findRoleById(Integer id){
        return roleRepository.findById(id);
    }

}
