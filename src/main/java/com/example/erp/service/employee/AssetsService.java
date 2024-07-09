package com.example.erp.service.employee;

import java.time.Month;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Assets;
import com.example.erp.repository.employee.AssetsRepository;




@Service
public class AssetsService {

	@Autowired
	private AssetsRepository repo;


	public Iterable<Assets> listAll() {
		return this.repo.findAll();
	}

//	public void Save(Attendance attendance) {
//		repo.save(attendance);
//	}

	public Assets getUserById(long id) {
		return repo.findById(id).get();
	}

	public void update(Assets attendance, long AttendanceId) {
		repo.save(attendance);
	}

////////delete
	public void deleteMemberById(Long id) {
		repo.deleteById(id);

	}

	/////// edit
	public void save(Assets existingAttendance) {
		repo.save(existingAttendance);
	}




	public Assets findById(Long id) {
		return repo.findById(id).get();
	}

	
	
	
	
	



}
