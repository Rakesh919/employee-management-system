package com.company.repository.leaves;

import com.company.entity.leaves.UserLeaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLeavesRepository extends JpaRepository<UserLeaves,Integer> {
}
