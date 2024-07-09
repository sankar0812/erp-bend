package com.example.erp.service.employee;

import java.time.Year;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Holidays;
import com.example.erp.entity.employee.HolidaysList;
import com.example.erp.repository.employee.HolidaysListRepository;
import com.example.erp.repository.employee.HolidaysRepository;



@Service 
public class HolidaysService {
	
	
	@Autowired
	private HolidaysRepository repo;
	
	
	@Autowired
	private HolidaysListRepository holidaysListRepository;

	public List<Holidays> listAll() {
		return repo.findAll();
	}

	public void saveOrUpdate(Holidays LeaveType) {
		repo.save(LeaveType);
	}

	public Holidays getById(long id) {
		return repo.findById(id).get();
	}

	public void deleteById(long id) {
		repo.deleteById(id);
	}


}
