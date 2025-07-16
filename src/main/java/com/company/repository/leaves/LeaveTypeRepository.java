package com.company.repository.leaves;

import com.company.entity.leaves.LeaveType;
import com.company.enums.TypesOfLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType,Integer> {

    List<LeaveType> findByType(TypesOfLeave type);
    boolean existsByType(TypesOfLeave type);

}
