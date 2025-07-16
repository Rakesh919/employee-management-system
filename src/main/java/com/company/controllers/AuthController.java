package com.company.controllers;

import com.company.constants.ErrorConstants;
import com.company.dto.response.SuccessResponse;
import com.company.entity.user.User;
import com.company.exception.UserNotFoundException;
import com.company.service.user.UserAdditionalDetailsService;
import com.company.service.user.UserService;
import com.company.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdditionalDetailsService userAdditionalDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public ResponseEntity<?> loginUserController(@RequestParam String username, @RequestParam String password){
    try{
        User isExists = userService.findByUserName(username);
        if(isExists==null){
            logger.error("User Details not found for this username: {}",username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
        }
        boolean isPasswordMatch = userService.isPasswordMatch(username,password);
        if(!isPasswordMatch){
            logger.error("Password do not match for this username : {}",username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.PASSWORD_DO_NOT_MATCH);
        }

        int id = isExists.getId();
        String role = isExists.getRole().name();
        String token = jwtUtil.generateToken(username,id,role);

        return ResponseEntity.ok(new SuccessResponse("SUCCESS","Logged in successfully ",token));
        }
        catch(UserNotFoundException e){
            throw e;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
