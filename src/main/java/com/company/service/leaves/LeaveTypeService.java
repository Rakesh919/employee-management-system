package com.company.service.leaves;

import com.company.entity.leaves.LeaveType;
import com.company.enums.TypesOfLeave;
import com.company.repository.leaves.LeaveTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveTypeService.class);

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public LeaveType addLeave(LeaveType dto){
        try{
            return leaveTypeRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LeaveType getLeaveById(int id){
        try{
            return leaveTypeRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeaveType> getLeaveByType(String leaveType){
        try{
            TypesOfLeave parsedValue = TypesOfLeave.valueOf(leaveType.toUpperCase());
            return leaveTypeRepository.findByType(parsedValue);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteLeaveById(int id){
        try{
            LeaveType isExists = leaveTypeRepository.findById(id).orElse(null);
            if(isExists==null) {
                logger.error("Details not found for leave ID: {}",id);
                return false;
            }

            leaveTypeRepository.deleteById(id);
            return true;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLeaveTypeAlreadyExists(String type){
        try{
            TypesOfLeave parsedValue = TypesOfLeave.valueOf(type.toUpperCase());
            return leaveTypeRepository.existsByType(parsedValue);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
