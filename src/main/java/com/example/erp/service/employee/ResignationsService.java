package com.example.erp.service.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Resignations;
import com.example.erp.repository.employee.ResignationsRepository;





@Service
public class ResignationsService {
	

	@Autowired
    private ResignationsRepository repo;
    
    public List<Resignations> listAll() {
        return repo.findAll();
    }

    public void saveOrUpdate(Resignations resignations) {
        repo.save(resignations);
    }

    public Resignations getById(long id) {
        return repo.findById(id).get();
    }

    public void deleteById(long id) {
        repo.deleteById(id);
    }

public List<Map<String,Object>>ALLOver(){
	return repo.AllGoat();
}

public Optional<Resignations> getResignationByEmployeeId(long employeeId) {
	 return repo.findById(employeeId);

}


}
