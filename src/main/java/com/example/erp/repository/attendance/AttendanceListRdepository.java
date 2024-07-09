package com.example.erp.repository.attendance;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.attendance.AttendanceList;

public interface AttendanceListRdepository extends JpaRepository<AttendanceList, Long> {

	Optional<AttendanceList> findByEmployeeIdAndDate(Long attendanceListId, Date date);
	
	
	

}
