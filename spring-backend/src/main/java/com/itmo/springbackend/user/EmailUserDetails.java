package com.itmo.springbackend.user;

import jakarta.validation.constraints.Email;
import org.springframework.security.core.userdetails.UserDetails;

public interface EmailUserDetails extends UserDetails {
    @Email String getEmail();
}
