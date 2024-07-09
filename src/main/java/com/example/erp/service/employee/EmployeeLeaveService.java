package com.example.erp.service.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.EmployeeLeave;
import com.example.erp.repository.employee.EmployeeLeaveRepository;


@Service
public class EmployeeLeaveService {
	
	@Autowired
    private EmployeeLeaveRepository repo;
    
    public List<EmployeeLeave> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(EmployeeLeave EmployeeLeave) {
        repo.save(EmployeeLeave);
    }

    public EmployeeLeave getById(long id) {
        return repo.findById(id).get();
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }
    
    
    public EmployeeLeave getByEmployeeId(long id) {
        return repo.findByEmployeeId(id).get();
    }
  
    public EmployeeLeave getByEmployeeIdAndEmployeeLeaveId(long id, long employeeLeaveId) {
        return repo.findByEmployeeIdAndEmployeeLeaveId(id, employeeLeaveId).get();
    }
    
    public Map<String, Object> getAllEmpLeave(Long employeeId, Date date){
    	return repo.getAllEmpLeave(employeeId, date);
    }
    
    public Map<String, Object> getAllTrainingLeave(Long traineeId, Date date){
    	return repo.getAllTrainingLeave(traineeId, date);
    }
}
