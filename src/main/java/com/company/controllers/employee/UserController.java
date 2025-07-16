package com.company.controllers.employee;

import com.company.constants.ErrorConstants;
import com.company.dto.request.UserDetailsDto;
import com.company.dto.response.SuccessResponse;
import com.company.entity.user.JwtUserDetails;
import com.company.entity.user.User;
import com.company.entity.user.UserDetails;
import com.company.enums.Role;
import com.company.service.user.UserAdditionalDetailsService;
import com.company.service.user.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAdditionalDetailsService userAdditionalDetailsService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/add-details")
    public ResponseEntity<?> addUserDetails(@Valid @RequestBody UserDetailsDto dto, Authentication authentication){
        try{
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            String role = userDetails.role();
            if(!Objects.equals(role, Role.USER.name())){
                logger.error("Only user is allowed to add his details");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorConstants.NOT_ALLOWED_TO_ACCESS);
            }
            int user_id = userDetails.userId();

            User isExists = userService.getUser(user_id);
            UserDetails details = new UserDetails();
            details.setName(dto.getName());
            details.setEmail(dto.getEmail());
            details.setGender(dto.getGender());
            details.setPhone(dto.getPhone());
            details.setDob(dto.getDob());
            details.setUser(isExists);

            UserDetails savedDetails = userAdditionalDetailsService.addUserDetails(details);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details Saved Successfully",savedDetails));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/user-detail")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        try{
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            int id = userDetails.userId();

            User isExist = userService.getUser(id);
            if(isExist==null){
                logger.error("User not found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
            }

            UserDetails details = userAdditionalDetailsService.getUserDetailsByUserId(id);
            if(details==null){
                logger.error("No Details found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.DETAILS_NOT_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details fetched successfully",details));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/update-details")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserDetails dto){
        try{

            if(dto.getId()<=0){
                logger.error("User Id is not valid : {}",dto.getId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.VALID_ID);
            }

            UserDetails details = userAdditionalDetailsService.getUserDetails(dto.getId());
            if(details==null){
                logger.error("User Details not found for this ID: {} ",dto.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
            }

            if (dto.getEmail() != null) details.setEmail(dto.getEmail());
            if (dto.getPhone() != null) details.setPhone(dto.getPhone());
            if (dto.getName() != null) details.setName(dto.getName());
            if (dto.getDob() != null) details.setDob(dto.getDob());
            if (dto.getGender() != null) details.setGender(dto.getGender());

            UserDetails savedDetails = userAdditionalDetailsService.addUserDetails(details);

            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details Updated Successfully",savedDetails));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}

