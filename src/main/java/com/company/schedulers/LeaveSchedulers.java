package com.company.schedulers;

import com.company.entity.leaves.UserLeaveBalance;
import com.company.enums.TypesOfLeave;
import com.company.repository.leaves.UserLeaveBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveSchedulers {

    private static final Logger logger = LoggerFactory.getLogger(LeaveSchedulers.class);

    @Autowired
    private UserLeaveBalanceRepository leaveBalanceRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    public void addMonthlyLeaveBalance(){
        logger.info("Add Monthly Leave Balance Schedule job started: ");
        try{
            List<UserLeaveBalance> allBalances = leaveBalanceRepository.findAll();
            for(UserLeaveBalance balance: allBalances){
                switch (balance.getLeaveType().getType()){
                    case TypesOfLeave.CASUAL :
                    case TypesOfLeave.SICK :
                        balance.setTotalAllocated(balance.getTotalAllocated()+1);
                        balance.setRemaining(balance.getRemaining()+1);
                        break;

                    case TypesOfLeave.EARNED :
                        balance.setTotalAllocated(balance.getTotalAllocated() + 0.5);
                        balance.setRemaining(balance.getRemaining() + 0.5);
                        break;
//                    case TypesOfLeave.SICK :
//                        balance.setTotalAllocated(balance.getTotalAllocated()+1);
//                        balance.setRemaining(balance.getRemaining()+1);
//                        break;
                    default:
                        break;

                }
            }
            leaveBalanceRepository.saveAll(allBalances);
            logger.info("Schedule Add Monthly Leave Balance Job Ended:");

        } catch (RuntimeException e) {
            logger.error("Exception Occurred at add monthly leave balance : {}",e.toString());
            throw new RuntimeException(e);
        }
    }
}
