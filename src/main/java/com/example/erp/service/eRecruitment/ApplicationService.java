package com.example.erp.service.eRecruitment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.Application;
import com.example.erp.repository.erecruitment.ApplicationRepository;



@Service
public class ApplicationService {
	
	@Autowired
    private ApplicationRepository repo;
    
    public List<Application> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(Application application) {
        repo.save(application);
    }

    public Application getById(long id) {
        return repo.findById(id).get();
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }

}
