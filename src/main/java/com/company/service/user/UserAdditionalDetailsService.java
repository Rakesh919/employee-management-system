package com.company.service.user;

import com.company.entity.user.UserDetails;
import com.company.enums.Role;
import com.company.repository.user.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdditionalDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public UserDetails addUserDetails(UserDetails dto){
        try{
            return userDetailsRepository.save(dto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails getUserDetails(int id){
        try{
            return userDetailsRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails getUserDetailsByUserId(int id){
        try{
            return userDetailsRepository.findByUserId(id).orElse(null);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDetails> getAllUsers(String role){
        try{
            Role parsedRole = Role.valueOf(role.toUpperCase());
//            return userDetailsRepository.findByUserRole(parsedRole);
            return userDetailsRepository.findUsersWithDetailsByRole(parsedRole);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


}
