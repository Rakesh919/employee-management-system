package com.company.dto.request;

import com.company.enums.Role;
import com.company.validation.annotation.ValidPassword;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "username can't be null and must be unique")
    @Column(unique = true)
    private String username;

    @ValidPassword
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "role is required and can't be null")
    private Role role;
}
