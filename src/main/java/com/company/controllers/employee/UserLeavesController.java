package com.company.controllers.employee;

import com.company.constants.ErrorConstants;
import com.company.dto.response.SuccessResponse;
import com.company.entity.leaves.LeaveType;
import com.company.entity.leaves.UserLeaveBalance;
import com.company.entity.user.User;
import com.company.repository.leaves.LeaveTypeRepository;
import com.company.repository.leaves.UserLeaveBalanceRepository;
import com.company.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaves/user")
public class UserLeavesController {

    private static final Logger logger = LoggerFactory.getLogger(UserLeavesController.class);

    @Autowired
    private UserLeaveBalanceRepository userLeaveBalanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @PostMapping("/request-leave")
    public ResponseEntity<?> addLeaveController(@RequestBody UserLeaveBalance dto){
        try{
            int user_id = dto.getUser().getId();
            int leave_type_id = dto.getLeaveType().getId();

            User user = userRepository.findById(user_id).orElse(null);
            if(user==null){
                logger.error("User Details not found for this ID: {}",user_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.USER_NOT_FOUND);
            }

            LeaveType leaveType = leaveTypeRepository.findById(leave_type_id).orElse(null);
            if(leaveType==null){
                logger.error("Leave Type not found for this ID : {}",leave_type_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.LEAVE_DETAILS_NOT_FOUND);
            }
            dto.setUser(user);
            dto.setLeaveType(leaveType);

            UserLeaveBalance savedDetails = userLeaveBalanceRepository.save(dto);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Leaved Added Successfully",savedDetails));


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
