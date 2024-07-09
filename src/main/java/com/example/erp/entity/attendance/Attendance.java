package com.example.erp.entity.attendance;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "attendance")
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long attendanceId;
	
	private Date attendanceDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_attendanceId", referencedColumnName = "attendanceId")
	private List<AttendanceList> attendance;
	
	

	public List<AttendanceList> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<AttendanceList> attendance) {
		this.attendance = attendance;
	}

	public long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	
	
}
