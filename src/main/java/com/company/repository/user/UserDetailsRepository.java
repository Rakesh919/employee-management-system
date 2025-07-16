package com.company.repository.user;

import com.company.entity.user.UserDetails;
import com.company.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Integer> {

    Optional<UserDetails> findByUserId(int userId);

    List<UserDetails> findByUserRole(Role role);

    @Query("SELECT ud FROM UserDetails ud WHERE ud.user.role = :role")
    List<UserDetails> findUsersWithDetailsByRole(@Param("role") Role role);



}
