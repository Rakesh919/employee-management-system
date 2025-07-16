package com.company.dto.request;

import com.company.validation.annotation.MinAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private int id;

    @NotNull(message = "Name can't be null")
    private String name;

    @Email(message = "Email must be valid")
    @NotNull(message = "Email can't be null")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be valid and 10 digits long")
    private String phone;

    @NotNull(message = "dob is required field")
    @MinAge(value=18,message = "User must be 18 years old")
    private LocalDate dob;

    @NotNull(message = "gender is required")
    private String gender;

}