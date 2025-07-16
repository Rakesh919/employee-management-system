package com.company.entity.leaves;


import com.company.entity.baseModel.BaseModel;
import com.company.enums.TypesOfLeave;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "leave_type")
public class LeaveType extends BaseModel {

    @Enumerated(EnumType.STRING)
    private TypesOfLeave type;

    private int max_days_allowed;
}
