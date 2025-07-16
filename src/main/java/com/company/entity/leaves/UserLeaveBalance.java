package com.company.entity.leaves;

import com.company.entity.baseModel.BaseModel;
import com.company.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_leave_balance")
public class UserLeaveBalance extends BaseModel {

    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(nullable = false,name = "leave_type_id")
    private LeaveType leaveType;

    private double totalAllocated;
    private double remaining;
}
