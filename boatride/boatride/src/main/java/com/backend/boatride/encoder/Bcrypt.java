package com.backend.boatride.encoder;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcrypt {
   public String encode(String plainPassword) {
      int strength = 10;
      BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
      return bCryptPasswordEncoder.encode(plainPassword);
   }
}
