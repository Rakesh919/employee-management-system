package com.company.service.user;

import com.company.entity.user.User;
import com.company.exception.UserNotFoundException;
import com.company.repository.user.UserRepository;
import com.company.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    public User addUser(User dto){
        try{
            return userRepository.save(dto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(int id){
        try{
            return userRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User details not found for username: {}", username);
                    return new UserNotFoundException("User details not found for username: " + username);
                });
    }

    public boolean isPasswordMatch(String username,String password){
        try{
            User isExists = userRepository.findByUsername(username).orElse(null);
            if(isExists==null){
                throw new RuntimeException("No user found for this username : " +username);
            }
            String hashedPassword = isExists.getPassword();
            return passwordUtil.matchPassword(password,hashedPassword);

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    public boolean isUserNameExists(String username){
        try{
            return userRepository.existsByUsername(username);
        } catch (Exception ex) {
            logger.error("Unexpected Error Occurred at is user name exists service method: {}",ex.getMessage());
            throw ex;
        }
    }


}
