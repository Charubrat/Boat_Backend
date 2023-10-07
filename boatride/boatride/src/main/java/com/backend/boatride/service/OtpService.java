package com.backend.boatride.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.boatride.Entities.Otp;
import com.backend.boatride.Entities.User;
import com.backend.boatride.Repository.OtpRepository;
import com.backend.boatride.Repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    public static boolean authenticateUser;
    @Autowired
    OtpRepository otpRepository;

    @Autowired
    UserRepository userRepository;

    public boolean verifyOtp(Long userId, String enteredOtp) {
        // Fetch the OTP record for the user from the database
        Optional<Otp> otpOptional = otpRepository.findByUserId(userId);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();

            // Check if the entered OTP matches the stored OTP
            return enteredOtp.equals(otp.getOtpCode());
        }

        return false;
    }

    public boolean authenticateUser(String userEmail, String password){
        Optional <User> userOptional = userRepository.findFirstByUserEmail(userEmail);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            System.out.println("User found: " + user.getUserEmail());
            System.out.println(user.getUserPassword());
            String storedHashedPassword = user.getUserPassword();
            System.out.println(password);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, storedHashedPassword)){
                String newOTPCode = generateNewOTP();


                System.out.println("Authentication successful");
                Optional<Otp> otpOptional = otpRepository.findByUserId(user.getId());
                if(otpOptional.isPresent()){
                    Otp otp = otpOptional.get();
                    otp.setOtpCode(newOTPCode);
                    otpRepository.save(otp);
                }else {
                    System.out.println("Authentication failed: Incorrect password");
                }
                return true;
            }else {
                System.out.println("User not found: " + userEmail);
            }
        }
        return false;
    }

    private String generateNewOTP(){
        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        return String.valueOf(otp);
    }
}
