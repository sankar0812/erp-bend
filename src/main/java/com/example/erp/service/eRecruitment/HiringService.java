package com.example.erp.service.eRecruitment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.Hiring;
import com.example.erp.repository.erecruitment.HiringRepository;



@Service
public class HiringService {
	
	
	@Autowired
    private HiringRepository repo;
    
    public List<Hiring> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(Hiring hiring) {
        repo.save(hiring);
    }

    public Hiring getById(long id) {
        return repo.findById(id).get();
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }
    
    
  

}
