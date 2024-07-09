package com.example.erp.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.repository.employee.ComplaintsRepository;



@Service
public class ComplaintsService {
	
	@Autowired
    private ComplaintsRepository repo;
    
    public List<Complaints> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(Complaints complaints) {
        repo.save(complaints);
    }

    public Complaints getById(long id) {
        return repo.findById(id).get();
    }
    
    public Optional<Complaints> getByIdSample(long id) {
        return repo.findById(id);
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }
    public Optional<Complaints> getById1(long id) {
        return Optional.of(repo.findById(id).get());
    }

	


}
