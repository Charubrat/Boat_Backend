package com.backend.boatride.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.boatride.Entities.Otp;
import com.backend.boatride.Entities.User;
import com.backend.boatride.Repository.OtpRepository;
import com.backend.boatride.Repository.UserRepository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;

@Service
@Configuration
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Bean
    public BCryptPasswordEncoder pBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User signUpUser(User user) {
        String hashedpassword = pBCryptPasswordEncoder().encode(user.getUserPassword());
        System.out.println(user.getUserPassword());
        user.setUserPassword(hashedpassword);
        System.out.println(hashedpassword);
        return userRepository.save(user);
    }
    public User findById(Long user_id){
        return userRepository.findById(user_id).orElse(null);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> findByEmailOrUsername(String email) {
        return userRepository.findFirstByUserEmail(email);
    }

    @Autowired
    private OtpRepository otpRepository;
    @Transactional
    public User registerUser(User user) {
        String otpCode = generateRandomOtp();
        User savedUser = userRepository.save(user);

        Otp otp = new Otp();
        otp.setUser(savedUser);
        otp.setOtpCode(otpCode);
        otp.setOtpCreatedA(new Timestamp(System.currentTimeMillis()));
        otpRepository.save(otp);

        return savedUser;
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }
}
