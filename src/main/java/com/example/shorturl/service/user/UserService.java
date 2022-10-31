package com.example.shorturl.service.user;

import com.example.shorturl.dao.RoleRepository;
import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.payload.request.user.AdminUserRequest;
import com.example.shorturl.model.payload.request.user.UserRequest;
import com.example.shorturl.model.roles.ERole;
import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.utils.exceptions.ERoleNotFoundException;
import com.example.shorturl.utils.exceptions.UserException;
import com.google.common.base.Enums;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{

    private int CUSTOM_PASSWORD_LENGTH = 15;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserEntity signUp(UserRequest userRequest) throws UserException {

        UserEntity userEntity = UserEntity.builder()
                .userName(userRequest.getUserName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleRepository.findByName(ERole.ROLE_USER).get()))
                .build();

        return userRepository.save(userEntity);
    }

    public UserEntity adminCreateUser(AdminUserRequest userRequest) {

        Set<RoleEntity> roles = userRequest.getRoles().size() == 0 ? ImmutableSet.of(roleRepository.findByName(ERole.ROLE_USER).get()) :
                userRequest.getRoles().stream().map(role ->
                        roleRepository.findByName(Enums.getIfPresent(ERole.class, role).orNull())
                                .orElseThrow(() -> new ERoleNotFoundException("Error: " + role + " not found"))
                ).collect(Collectors.toSet());

        String password = userRequest.getPassword() != null ? userRequest.getPassword() :
                generateRandomSpecialCharacters(CUSTOM_PASSWORD_LENGTH);

        UserEntity userEntity = UserEntity.builder()
                .userName(userRequest.getUserName())
                .password(passwordEncoder.encode(password))
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(roles)
                .build();

        Optional<UserEntity> existingUser = findFirstByUserNameOrEmail(userRequest.getUserName(), userRequest.getEmail());

        if(existingUser.isPresent())
            userEntity.setId(existingUser.get().getId());

        UserEntity userSaved = userRepository.save(userEntity);
        userSaved.setPassword(password);

        return userSaved;
    }

    public Optional<UserEntity> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public Optional<UserEntity> findFirstByUserNameOrEmail(String userName, String email){
        return userRepository.findFirstByUserNameOrEmail(userName, email);
    }

    public List<UserEntity> findAll(){
        return userRepository.findAll();
    }

    private String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z')
                .build();
        return pwdGenerator.generate(length);
    }

}
