package com.example.erp.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.EmployeeExit;
import com.example.erp.entity.employee.Resignations;
import com.example.erp.repository.employee.EmployeeExitRepository;

@Service
public class EmployeeExitService {
	
	@Autowired
    private EmployeeExitRepository repo;
    
    public List<EmployeeExit> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(EmployeeExit EmployeeExit) {
        repo.save(EmployeeExit);
    }

    public EmployeeExit getById(long id) {
        return repo.findById(id).get();
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }

    

public Optional<EmployeeExit> getfingid(long id) {
	 return repo.findById(id);

}
}
