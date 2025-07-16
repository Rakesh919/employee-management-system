package com.company.entity.user;

import com.company.entity.baseModel.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
@EqualsAndHashCode(callSuper = true)
public class UserDetails extends BaseModel {

    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}
