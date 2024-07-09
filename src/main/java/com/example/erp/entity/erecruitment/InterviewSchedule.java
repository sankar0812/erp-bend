package com.example.erp.entity.erecruitment;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "interviewSchedule")
public class InterviewSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long interviewScheduleId;
	private long candidateId;
	private long departmentId;
	private long employeeId;
	private Date date;
	private String startTime;
	private String endTime;
	   @Column(columnDefinition = "TEXT")
	private String cancellationReason;
	private String interviewType;
	private boolean Scheduled;
	private boolean Completed;
	private boolean Canceled;
	private boolean status;

//	  private LocalTime startTime;
//	    private LocalTime endTime;
	    
	    
	    
//	public LocalTime getStartTime() {
//			return startTime;
//		}
//
//		public void setStartTime(LocalTime startTime) {
//			this.startTime = startTime;
//		}
//
//		public LocalTime getEndTime() {
//			return endTime;
//		}
//
//		public void setEndTime(LocalTime endTime) {
//			this.endTime = endTime;
//		}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getInterviewScheduleId() {
		return interviewScheduleId;
	}

	public void setInterviewScheduleId(long interviewScheduleId) {
		this.interviewScheduleId = interviewScheduleId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(String interviewType) {
		this.interviewType = interviewType;
	}

	public boolean isScheduled() {
		return Scheduled;
	}

	public void setScheduled(boolean scheduled) {
		Scheduled = scheduled;
	}

	public boolean isCompleted() {
		return Completed;
	}

	public void setCompleted(boolean completed) {
		Completed = completed;
	}

	public boolean isCanceled() {
		return Canceled;
	}

	public void setCanceled(boolean canceled) {
		Canceled = canceled;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public InterviewSchedule() {
		super();
	}



}
