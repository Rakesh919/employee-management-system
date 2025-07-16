package com.company.entity.leaves;

import com.company.entity.baseModel.BaseModel;
import com.company.entity.user.User;
import com.company.enums.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="user_leaves")
public class UserLeaves extends BaseModel {

    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false,name = "leave_type_id")
    private LeaveType leaveType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    private LocalDate end_date;

    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;
    private String reason;
}
