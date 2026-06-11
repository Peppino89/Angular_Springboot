package org.example.todo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.todo.dto.*;
import org.example.todo.entity.PasswordResetToken;
import org.example.todo.entity.User;
import org.example.todo.exception.InvalidCredentialsException;
import org.example.todo.exception.UserAlreadyExistsException;
import org.example.todo.exception.UserNotFoundException;
import org.example.todo.repository.PasswordResetTokenRepository;
import org.example.todo.repository.UtenteRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordResetTokenRepository  passwordResetTokenRepository;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest registerRequest) {
        if(utenteRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Utente già esistente");
        }
        if(utenteRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email già in uso");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        utenteRepository.save(user);

        String token = jwtService.generateToken(
          userDetailsService.loadUserByUsername(registerRequest.getUsername())
        );

        return new AuthResponse(token,user.getUsername(), user.getEmail());

    }

    public AuthResponse login(LoginRequest request) {
        try{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        }catch(BadCredentialsException | InternalAuthenticationServiceException e){
            throw new InvalidCredentialsException("Username o password non validi");
        }
        User user = utenteRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new UserNotFoundException("Utente non trovato"));
        String token = jwtService.generateToken(
                userDetailsService.loadUserByUsername(user.getUsername())
        );
        return new AuthResponse(token,user.getUsername(), user.getEmail());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        utenteRepository.findByEmail(request.getEmail()).
                ifPresent(utente -> {
                    passwordResetTokenRepository.deleteByUser(utente);

                    String token = UUID.randomUUID().toString();

                    PasswordResetToken resetToken = PasswordResetToken.builder()
                            .token(token)
                            .user(utente)
                            .expiresAt(LocalDateTime.now().plusMinutes(15))
                         //   .used(false)
                            .build();

                    passwordResetTokenRepository.save(resetToken);
                    String resetLink = "http://localhost:4200/reset-password?token=" + token ;
                    emailService.sendPasswordResetEmail(utente.getEmail(), resetLink);

                });

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(()->new InvalidCredentialsException("Token non valido"));


//        if(resetToken.isUsed()){
//            throw new InvalidCredentialsException("Token già utilizzato");
//        }

        if(resetToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidCredentialsException("Token scaduto");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new InvalidCredentialsException("Le password non coincidono");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        utenteRepository.save(user);

//        resetToken.setUsed(true);
//        passwordResetTokenRepository.save(resetToken);

        passwordResetTokenRepository.delete(resetToken);

    }

    @Transactional
    public ResetTokenValidationResponse validateResetToken(String token){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(()->new InvalidCredentialsException("Link non valido"));

        if(resetToken.getExpiresAt().isBefore(LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            throw new InvalidCredentialsException("Link scaduto Richiedi un nuovo reset password.");
        }

        return new ResetTokenValidationResponse("Token valido",resetToken.getExpiresAt());


    }
}
