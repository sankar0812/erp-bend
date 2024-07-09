package com.example.erp.entity.employee;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employeeAtt")
public class EmployeeAttendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeAttId;
	private Long employeeId;
	private boolean punchIn;
	private boolean punchOut;
	private String InTime;
	private String OutTime;
	private LocalDate inDate;
	private String workingHour;
	private String attendance;
	private String ipAddress;
	private Long traineeId;

	public Long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(Long traineeId) {
		this.traineeId = traineeId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public Long getEmployeeAttId() {
		return employeeAttId;
	}

	public void setEmployeeAttId(Long employeeAttId) {
		this.employeeAttId = employeeAttId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public boolean isPunchIn() {
		return punchIn;
	}

	public void setPunchIn(boolean punchIn) {
		this.punchIn = punchIn;
	}

	public boolean isPunchOut() {
		return punchOut;
	}

	public void setPunchOut(boolean punchOut) {
		this.punchOut = punchOut;
	}

	public String getInTime() {
		return InTime;
	}

	public void setInTime(String inTime) {
		InTime = inTime;
	}

	public String getOutTime() {
		return OutTime;
	}

	public void setOutTime(String outTime) {
		OutTime = outTime;
	}

	public LocalDate getInDate() {
		return inDate;
	}

	public void setInDate(LocalDate inDate) {
		this.inDate = inDate;
	}

	public String getWorkingHour() {
		return workingHour;
	}

	public void setWorkingHour(String workingHour) {
		this.workingHour = workingHour;
	}

	public EmployeeAttendance() {
		super();
	}

	public void setClientIpAddress(String clientIpAddress) {

	}

}
