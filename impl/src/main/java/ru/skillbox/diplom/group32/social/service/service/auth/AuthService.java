package ru.skillbox.diplom.group32.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.JwtTokenProvider;
import ru.skillbox.diplom.group32.social.service.config.security.exception.PasswordsAreNotMatchingException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.UserAlreadyExistsException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.UserNotFoundException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.WrongPasswordException;
import ru.skillbox.diplom.group32.social.service.mapper.auth.UserMapper;
import ru.skillbox.diplom.group32.social.service.model.auth.*;
import ru.skillbox.diplom.group32.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final HttpServletResponse httpServletResponse;

    private final AccountService accountService;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto, HttpServletResponse response) {
        String email = authenticateDto.getEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        log.info("User with email: " + email + " found");


        if (!passwordEncoder.matches(authenticateDto.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        String token = jwtTokenProvider.createToken(user.getId(), email, user.getRoles());

        setCookie(response, token);

        return new AuthenticateResponseDto(token, token);
    }

    public void setCookie(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    public UserDto register(RegistrationDto registrationDto) {
        String email = registrationDto.getEmail();

        // TODO - заменить метод на exists()
        userRepository.findUserByEmail(email).ifPresent(x -> { throw new UserAlreadyExistsException("This email already taken");});


        if (!registrationDto.getPassword1().equals(registrationDto.getPassword2())) {
            throw new PasswordsAreNotMatchingException("Passwords should be equal");
        }


        return createUser(userMapper.registrationDtoToUserDto(registrationDto));
    }

    public UserDto createUser(UserDto userDto) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findByName("USER"));

        User userToDB = userMapper.dtoToUser(userDto);
        userToDB.setRoles(userRoles);

        userToDB.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("Created User to save - " + userToDB);

        accountService.createAccount(userToDB);
        log.info("Created User saved to db - " + userToDB);

        UserDto userDtoResult = userMapper.userToDto(userToDB);
        log.info("Created User Dto result - " + userDtoResult);
        return userDtoResult;
    }

    public UserDto getUser(Long id) {

        User userFromDB = userRepository.findById(id).get();
        UserDto userDtoResult = userMapper.userToDto(userFromDB);
        log.info("Search User Dto result - " + userDtoResult);

        return userDtoResult;
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).get();
        log.info("User by email is - " + user);
        return user;
    }
}
