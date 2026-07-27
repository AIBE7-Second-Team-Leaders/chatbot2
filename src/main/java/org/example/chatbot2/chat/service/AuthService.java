package org.example.chatbot2.chat.service;

import org.example.chatbot2.chat.domain.AppUser;
import org.example.chatbot2.chat.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser signUp(
            String email,
            String password,
            String displayName
    ){
        String normalizedEmail = email == null ? "" : email.trim();
        String normalizedName = displayName == null ? "" : displayName.trim();

        if(normalizedEmail.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if(normalizedName.isBlank()){
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }

        if(userRepository.findByEmail(normalizedEmail).isPresent()){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String passwordHash = passwordEncoder.encode(password);

        AppUser user = new AppUser(
                normalizedEmail,
                passwordHash,
                normalizedName
        );

        return userRepository.save(user);
    }

    public AppUser login(String email, String password){
        String normalizedEmail = email == null ? "" : email.trim();

        if(normalizedEmail.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        AppUser user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.")
                );

        if(!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;
    }
}
