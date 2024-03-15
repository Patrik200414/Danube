package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.UserLoginDTO;
import com.danube.danube.model.dto.user.UserRegistrationDTO;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.service.utility.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {
    public static final int MIN_LENGTH_NAME = 2;
    public static final int MIN_LENGTH_PASSWORD = 6;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        registrationValidator(userRegistrationDTO);
        UserEntity user = Converter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(user);
    }

    public JwtResponse loginUser(UserLoginDTO userLoginDTO){
        validateEmail(userLoginDTO.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserEntity user = userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(userLoginDTO.email()));

        return new JwtResponse(jwt, user.getFirstName(), user.getId(), roles);
    }

    public JwtResponse addSellerRoleToUser(long id){
        Optional<UserEntity> searchedUser = userRepository.findById(id);

        if(searchedUser.isEmpty()){
            throw new NonExistingUserException();
        }

        UserEntity user = searchedUser.get();

        Set<Role> userRoles = user.getRoles();
        userRoles.add(Role.ROLE_SELLER);

        user.setRoles(userRoles);

        UserEntity updatedUser = userRepository.save(user);

        String jwtToken = jwtUtils.generateJwtToken(updatedUser.getEmail());
        List<String> roles = updatedUser.getRoles().stream()
                .map(Enum::name)
                .toList();

        return new JwtResponse(
                jwtToken,
                updatedUser.getFirstName(),
                updatedUser.getId(),
                roles
        );
    }


    private void registrationValidator(UserRegistrationDTO userRegistrationDTO){
        if(userRegistrationDTO.email() == null){
            throw new RegistrationFieldNullException("email");
        } else if(userRegistrationDTO.firstName() == null){
            throw new RegistrationFieldNullException("first name");
        } else if(userRegistrationDTO.lastName() == null){
            throw new RegistrationFieldNullException("last name");
        } else if(userRegistrationDTO.password() == null){
            throw new RegistrationFieldNullException("password");
        }

        if(userRegistrationDTO.firstName().length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("first name", MIN_LENGTH_NAME);
        } else if(userRegistrationDTO.password().length() < MIN_LENGTH_PASSWORD){
            throw new InputTooShortException("password", MIN_LENGTH_PASSWORD);
        } else if(userRegistrationDTO.lastName().length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("last name", MIN_LENGTH_NAME);
        }

        validateEmail(userRegistrationDTO.email());
    }

    private void validateEmail(String email){
        if(!Pattern.compile(".+\\@.+\\..+").matcher(email).matches()){
            throw new InvalidEmailFormatException();
        }
    }
}
