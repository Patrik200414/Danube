package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.*;
import com.danube.danube.custom_exception.user.NotMatchingCurrentPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingNewPasswordAndReenterPasswordException;
import com.danube.danube.custom_exception.user.NotMatchingUserAndUpdateUserIdException;
import com.danube.danube.model.dto.jwt.JwtResponse;
import com.danube.danube.model.dto.user.*;
import com.danube.danube.model.user.Role;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.security.jwt.JwtUtils;
import com.danube.danube.utility.Converter;
import jakarta.transaction.Transactional;
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
    private final Converter converter;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, Converter converter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.converter = converter;
    }

    public void saveUser(UserRegistrationDTO userRegistrationDTO){
        registrationValidator(userRegistrationDTO);
        UserEntity user = converter.convertUserRegistrationDTOToUserEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(user);
    }

    public JwtResponse loginUser(UserLoginDTO userLoginDTO){
        validateEmail(userLoginDTO.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password())
        );

        UserEntity user = userRepository.findByEmail(userLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(userLoginDTO.email()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        return new JwtResponse(jwt, user.getFirstName(), user.getLastName(), user.getEmail(), user.getId(), roles);
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

        return generateJwtResponse(updatedUser);
    }

    @Transactional
    public JwtResponse updateUser(long id, UserUpdateDTO userUpdateDTO){
        validateEmail(userUpdateDTO.email());
        validateFirstNameAndLastName(userUpdateDTO.firstName(), userUpdateDTO.lastName());

        if(id != userUpdateDTO.userId()){
            throw new NotMatchingUserAndUpdateUserIdException();
        }


        Optional<UserEntity> searchedUser = userRepository.findById(id);
        if(searchedUser.isEmpty()){
            throw new NonExistingUserException();
        }

        UserEntity user = searchedUser.get();


        user.setEmail(userUpdateDTO.email());
        user.setFirstName(userUpdateDTO.firstName());
        user.setLastName(userUpdateDTO.lastName());

        UserEntity updatedUser = userRepository.save(user);
        return generateJwtResponse(updatedUser);
    }

    @Transactional
    public void updatePassword(long id, PasswordUpdateDTO passwordUpdateDTO){
        Optional<UserEntity> searchedUser = userRepository.findById(id);

        if(searchedUser.isEmpty()){
            throw new NonExistingUserException();
        }

        UserEntity user = searchedUser.get();

        if(!passwordEncoder.matches(passwordUpdateDTO.currentPassword(), user.getPassword())){
            throw new NotMatchingCurrentPasswordException();
        }

        if(!passwordUpdateDTO.newPassword().equals(passwordUpdateDTO.reenterPassword())){
            throw new NotMatchingNewPasswordAndReenterPasswordException();
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.newPassword()));
        userRepository.save(user);
    }

    public boolean verifyUser(UserVerificationDTO userVerificationDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userVerificationDTO.email(), userVerificationDTO.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }


    private void registrationValidator(UserRegistrationDTO userRegistrationDTO){
        if(userRegistrationDTO.email() == null || userRegistrationDTO.email().isEmpty()){
            throw new RegistrationFieldNullException("email");
        } else if(userRegistrationDTO.firstName() == null || userRegistrationDTO.firstName().isEmpty()){
            throw new RegistrationFieldNullException("first name");
        } else if(userRegistrationDTO.lastName() == null || userRegistrationDTO.lastName().isEmpty()){
            throw new RegistrationFieldNullException("last name");
        } else if(userRegistrationDTO.password() == null || userRegistrationDTO.password().isEmpty()){
            throw new RegistrationFieldNullException("password");
        }


        validateFirstNameAndLastName(userRegistrationDTO.firstName(), userRegistrationDTO.lastName());
        if(userRegistrationDTO.password().length() < MIN_LENGTH_PASSWORD){
            throw new InputTooShortException("password", MIN_LENGTH_PASSWORD);
        }

        validateEmail(userRegistrationDTO.email());
    }

    private JwtResponse generateJwtResponse(UserEntity user){
        String jwtToken = jwtUtils.generateJwtToken(user.getEmail());
        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        return new JwtResponse(
                jwtToken,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId(),
                roles
        );
    }

    private void validateFirstNameAndLastName(String firstName, String lastName){
        if(firstName.length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("first name", MIN_LENGTH_NAME);
        } else if(lastName.length() < MIN_LENGTH_NAME){
            throw new InputTooShortException("last name", MIN_LENGTH_NAME);
        }
    }

    private void validateEmail(String email){
        if(!Pattern.compile(".+\\@.+\\..+").matcher(email).matches()){
            throw new InvalidEmailFormatException();
        }
    }
}
