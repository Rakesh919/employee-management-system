package com.company.controllers.admin;


import com.company.constants.ErrorConstants;
import com.company.dto.response.SuccessResponse;
import com.company.entity.leaves.LeaveType;
import com.company.entity.leaves.UserLeaveBalance;
import com.company.entity.leaves.UserLeaves;
import com.company.entity.user.User;
import com.company.repository.leaves.LeaveTypeRepository;
import com.company.repository.leaves.UserLeaveBalanceRepository;
import com.company.repository.leaves.UserLeavesRepository;
import com.company.repository.user.UserRepository;
import com.company.service.leaves.LeaveTypeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave/admin")
public class LeavesController {

    private static final Logger logger = LoggerFactory.getLogger(LeavesController.class);

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private UserLeavesRepository userLeavesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private UserLeaveBalanceRepository userLeaveBalanceRepository;


    @PostMapping("/add")
    public ResponseEntity<?> addLeaveController(@Valid @RequestBody LeaveType dto){
        try{

            boolean isTypeAlreadyExists = leaveTypeService.isLeaveTypeAlreadyExists(dto.getType().toString());
            if(isTypeAlreadyExists){
                logger.error("Leave type already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.LEAVE_TYPE_ALREADY_EXISTS);
            }
            LeaveType savedDetails = leaveTypeService.addLeave(dto);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Leave Added Successfully",savedDetails));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getLeaveTypeByIdController(@RequestParam(required = true) Integer id){
        try{
            if(id==null || id>=0){
                logger.error("Id is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.VALID_ID);
            }
            LeaveType details = leaveTypeService.getLeaveById(id);

            if(details==null){
                logger.error("No leave found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.LEAVE_DETAILS_NOT_FOUND);
            }

            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details Fetched Successfully",details));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }


    @GetMapping("/get/all")
    public ResponseEntity<?> getLeaveByTypeController(@RequestParam String type){
        try{
            if(type==null){
            List<LeaveType> list = leaveTypeRepository.findAll();
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","All Leaves Fetched Successfully",list));
            }
            List<LeaveType> details = leaveTypeService.getLeaveByType(type);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details Fetched Successfully",details));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLeaveController(@RequestParam int id){
        try{
            if(id<=0){
                logger.error("Id is required field");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.VALID_ID);
            }
            boolean isDeleted = leaveTypeService.deleteLeaveById(id);
            if(!isDeleted){
                logger.error("No leave found : {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.LEAVE_DETAILS_NOT_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Deleted Successfully",true));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/add-leave")
    public ResponseEntity<?> addLeaveForUserController(@Valid @RequestBody UserLeaves dto){
        try{
            int user_id = dto.getUser().getId();
            int leave_type_id = dto.getLeaveType().getId();

            User isExists = userRepository.findById(user_id).orElse(null);
            if(isExists==null){
                logger.error("User Details not found for this ID : {}",user_id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.USER_NOT_FOUND);
            }

            LeaveType leave_details = leaveTypeService.getLeaveById(leave_type_id);
            if(leave_details==null){
                logger.error("Leave Type details not found for this ID : {}",leave_type_id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.LEAVE_DETAILS_NOT_FOUND);
            }
            dto.setUser(isExists);
            dto.setLeaveType(leave_details);

            UserLeaves savedDetails = userLeavesRepository.save(dto);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Leave Added Successfully",savedDetails));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/get-leave")
    public ResponseEntity<?> getLeaveByIdController(@RequestParam(required = true) Integer id){
        try{
            if(id==null){
                logger.error("Id not found in params ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.VALID_ID);
            }

            UserLeaves balance = userLeavesRepository.findById(id).orElse(null);

            if(balance==null){
                logger.error("Details not found for this ID : {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.DETAILS_NOT_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Leave Details Fetched Successfully",balance));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete-leaves")
    public ResponseEntity<?> deleteLeaveByIdController(@RequestParam(required = true) Integer id){
        try{

            if(id==null){
                logger.error("ID not found in Params");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.VALID_ID);
            }
            UserLeaves details= userLeavesRepository.findById(id).orElse(null);
            if(details ==null){
                logger.error("Leave details not found for this ID : {}",id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.LEAVE_DETAILS_NOT_FOUND);
            }
            userLeavesRepository.deleteById(id);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Leave Details Deleted Successfully",true));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
