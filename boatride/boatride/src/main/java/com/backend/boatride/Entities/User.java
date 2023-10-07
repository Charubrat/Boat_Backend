package com.backend.boatride.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    //@Column(name = "first_name") // Specify the column name
    private String firstName;

    //@Column(name = "last_name") // Specify the column name
    private String lastName;

    //@Column(name = "user_email") // Specify the column name
    private String userEmail;

    @Getter
    //@Column(name = "user_password") // Specify the column name
    public String userPassword;

    //@Column(name = "user_phone", nullable = true) // Specify the column name
    public String userPhone;

    public Long getId(){
        return userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}

