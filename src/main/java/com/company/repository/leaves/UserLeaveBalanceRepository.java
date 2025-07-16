package com.company.repository.leaves;

import com.company.entity.leaves.UserLeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLeaveBalanceRepository extends JpaRepository<UserLeaveBalance,Integer> {

    UserLeaveBalance findByUserIdAndLeaveTypeId(int userId, int leaveTypeId);

}
