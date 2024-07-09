package com.example.erp.service.attendance;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.attendance.AttendanceList;
import com.example.erp.repository.attendance.AttendanceListRdepository;

@Service
public class AttendanceListService {
	
	@Autowired
	private AttendanceListRdepository  repo;
	
	
	public void save(AttendanceList attendancelist) {
		repo.save(attendancelist);
	}
	public AttendanceList findById(Long attendanceListId) {
		return repo.findById(attendanceListId).get();
	}
	
	
	public AttendanceList findByEmployeeIdAndDate(Long attendanceListId, Date date) {
		return repo.findByEmployeeIdAndDate(attendanceListId, date).get();
	}
	

}
