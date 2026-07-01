package com.example.barberbooking.service;

import com.example.barberbooking.dto.request.*;
import com.example.barberbooking.dto.response.*;
import com.example.barberbooking.entity.*;
import com.example.barberbooking.enums.Role;
import com.example.barberbooking.exception.*;
import com.example.barberbooking.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request) {


        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserNotFoundException("Username già in uso");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailUsernamAlreadyExistsException("Email già in uso");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
       String token = jwtService.generateToken(user);
        return new AuthResponse(token,user.getUsername(),user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
        }catch(BadCredentialsException | InternalAuthenticationServiceException e){
            throw new BadCredentialsException(" Username o password non validi");
        }
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new  UserNotFoundException("Utente non trovato"));

//        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
//            throw new InvalidPasswordException("Password non valida");
//        }
         String token = jwtService.generateToken(user);
        return new AuthResponse(token,user.getUsername(),user.getEmail());
    }


    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
       userRepository.findByEmail(request.getEmail())
                .ifPresent(
                        user -> {
                            passwordResetTokenRepository.deleteByUser(user);

                            String token = UUID.randomUUID().toString();

                            PasswordResetToken resetToken = PasswordResetToken.builder()
                                    .token(token)
                                    .user(user)
                                    .expiresAt(LocalDateTime.now().plusMinutes(15))
                                    .build();

                            passwordResetTokenRepository.save(resetToken);

                            String resetLink = "http://localhost:4200/reset-password?token=" + token;

                            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

                        }
                );



    }

    @Transactional(noRollbackFor = InvalidCredentialsException.class)
    public ResetTokenValidationResponse validateToken(String token) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
            .orElseThrow(()->new InvalidCredentialsException("Link non valido"));

    if(resetToken.getExpiresAt().isBefore(LocalDateTime.now())){
        passwordResetTokenRepository.delete(resetToken);
        throw new InvalidCredentialsException("Link Scaduto");
    }

    return new ResetTokenValidationResponse (true,"Token Valido", resetToken.getExpiresAt());
    }

    @Transactional(noRollbackFor = InvalidCredentialsException.class)
    public void resetPassword(ResetPasswordRequest request) {

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new InvalidPasswordException("Le password non coincidono");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(()->new InvalidCredentialsException("Token non valido"));

        if(resetToken.getExpiresAt().isBefore(LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            throw new InvalidCredentialsException("Token Scaduto");
        }


        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        passwordResetTokenRepository.delete(resetToken);

    }


}
