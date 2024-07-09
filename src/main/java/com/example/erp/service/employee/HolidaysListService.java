package com.example.erp.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.HolidaysList;
import com.example.erp.repository.employee.HolidaysListRepository;

@Service
public class HolidaysListService {
	
	
	@Autowired
	private HolidaysListRepository holidaysListRepository;

	public List<HolidaysList> listAll() {
		return holidaysListRepository.findAll();
	}

	public void saveOrUpdate(HolidaysList LeaveType) {
		holidaysListRepository.save(LeaveType);
	}

	public HolidaysList getById(long id) {
		return holidaysListRepository.findById(id).get();
	}

	public void deleteById(long id) {
		holidaysListRepository.deleteById(id);
	}

}
