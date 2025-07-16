package com.company.controllers.admin;

import com.company.constants.ErrorConstants;
import com.company.dto.request.UserDto;
import com.company.dto.response.ErrorResponse;
import com.company.dto.response.SuccessResponse;
import com.company.entity.user.JwtUserDetails;
import com.company.entity.user.User;
import com.company.entity.user.UserDetails;
import com.company.enums.Role;
import com.company.service.user.UserAdditionalDetailsService;
import com.company.service.user.UserService;
import com.company.utils.PasswordUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdditionalDetailsService userAdditionalDetailsService;


    @PostMapping("/add-user")
    public ResponseEntity<?> addUserController(Authentication authentication,@Valid @RequestBody UserDto dto){
        try{
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            String userRole = userDetails.role();
            if(!Objects.equals(userRole, Role.ADMIN.name())){
                logger.error("Normal User can not create new employees userRole: {}",userRole);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorConstants.NOT_ALLOWED_TO_ACCESS);
            }

            boolean isAlreadyUser = userService.isUserNameExists(dto.getUsername());
            if(isAlreadyUser){
                logger.error("User Name Already Exists for the username: {}",dto.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.USERNAME_ALREADY_EXISTS);
            }
            User userDto = new User();
            userDto.setUsername(dto.getUsername());
            userDto.setPassword(passwordUtil.hashPassword(dto.getPassword()));
            userDto.setRole(dto.getRole());
            User savedDetails= userService.addUser(userDto);

            return ResponseEntity.ok(new SuccessResponse("SUCCESS","User Added Successfully",savedDetails));

        }    catch (DataIntegrityViolationException ex) {
            logger.error("Duplicate entry: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse("Username already exists", "DUPLICATE"));
        }
        catch(Exception ex){
            logger.error("Unexpected Error Occurred at add user controller method {} ",ex.getMessage());
            throw ex;
        }

    }


    @GetMapping("/get/users")
    public ResponseEntity<?> getAllUsers(@RequestParam String role){
        try{
            List<UserDetails> list = userAdditionalDetailsService.getAllUsers(role);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","All Users Fetched Successfully",list));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUserController(@RequestParam int id){
        try{
            User isExists = userService.getUser(id);
            if(isExists==null){
                logger.error("User Details not found for this ID: {} ",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
            }

            if (isExists.getRole() == Role.ADMIN || isExists.getRole()!=Role.USER) {
                logger.error("You don't have permission to delete admin : {}", id);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorConstants.NOT_ALLOWED_TO_ACCESS);
            }

            isExists.setDeleted(true);
            userService.addUser(isExists);

            UserDetails details = userAdditionalDetailsService.getUserDetailsByUserId(id);
            if(details==null){
                logger.error("User Details not found for this ID: {} ",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
            }

            details.setDeleted(true);
            userAdditionalDetailsService.addUserDetails(details);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","User Deleted Successfully",true));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
