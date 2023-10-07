package com.backend.boatride.auth;

import com.backend.boatride.Entities.Otp;
import com.backend.boatride.Repository.OtpRepository;
import com.backend.boatride.Repository.UserRepository;
import com.backend.boatride.service.JwtService;
import com.backend.boatride.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.boatride.Entities.User;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UserRepository repository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .userEmail(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        Long userid = user.getId();
//        HashMap<String, Object> extraClaims = new HashMap<>();
//
        System.out.println(user.getUsername());



        var jwtToken = jwtService.generateToken(user);

        System.out.println(jwtToken);


        return AuthenticationResponse.builder()
                .token(jwtToken).userId(userid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //figure out
        var user = repository.findFirstByUserEmail(request.getEmail())
                .orElseThrow(); //catch correct exception

        String newOTPCode = otpService.generateNewOTP();

        Optional<Otp> otpOptional = otpRepository.findByUserId(user.getId());
        if(otpOptional.isPresent()){
            Otp otp = otpOptional.get();
            otp.setOtpCode(newOTPCode);
            otpRepository.save(otp);
        }else {
            return (AuthenticationResponse) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }

        // added type cast to User
        HashMap<String, Object> extraClaims = new HashMap<>();
        Long userId = user.getId();
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken).userId(userId)
                .build();
    }
}
