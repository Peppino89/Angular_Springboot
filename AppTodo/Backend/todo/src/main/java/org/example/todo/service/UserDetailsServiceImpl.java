package org.example.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.todo.repository.UtenteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String userName)throws UsernameNotFoundException{
        return utenteRepository.findByUsername(userName)
                .map(user-> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .build())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username: " + userName));
    }

}
