package com.example.erp.service.attendance;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.attendance.Attendance;
import com.example.erp.entity.attendance.AttendanceList;
import com.example.erp.repository.attendance.AttendanceListRdepository;
import com.example.erp.repository.attendance.AttendanceRepository;


@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository repo;
	
	@Autowired
	private AttendanceListRdepository attendanceListRdepository;


	public Iterable<Attendance> listAll() {
		return this.repo.findAll();
	}

//	public void Save(Attendance attendance) {
//		repo.save(attendance);
//	}

	public Attendance getUserById(long id) {
		return repo.findById(id).get();
	}

	public void update(Attendance attendance, long AttendanceId) {
		repo.save(attendance);
	}

////////delete
	public void deleteMemberById(Long id) {
		repo.deleteById(id);

	}

	/////// edit
	public void save(Attendance existingAttendance) {
		repo.save(existingAttendance);
	}

	public Optional<AttendanceList> findByEmployeeIdAndDate(Long employeeId, Date date) {
		return attendanceListRdepository.findByEmployeeIdAndDate(employeeId, date);
	}


	public Attendance findById(Long id) {
		return repo.findById(id).get();
	}

	
	
	
	
	

//	public List<Attendance> filterAttendance(Date attendanceDate, Month month, boolean fullDay, boolean halfDay,
//			boolean present, boolean absent) {
//		return repo.findByAttendanceDateAndMonthAndFullDayAndHalfDayAndPresentAndAbsent(attendanceDate, month, fullDay,
//				halfDay, present, absent);
//	}
//
//	public List<Attendance> getAttendanceByMemberid(long memberid) {
//		return repo.findByMemberid(memberid);
//	}
//
//	public List<Attendance> getAttendanceByFilters(long memberid, Date attendanceDate, Month month, boolean present,
//			boolean absent, boolean fullDay, boolean halfDay) {
//		return repo.findByMemberidAndAttendanceDateAndMonthAndPresentAndAbsentAndFullDayAndHalfDay(memberid,
//				attendanceDate, month, present, absent, fullDay, halfDay);
//	}

}
