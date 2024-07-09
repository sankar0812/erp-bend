package com.example.erp.entity.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "employeeleave")
public class EmployeeLeave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeLeaveId;
	private Long employeeId;
	private boolean approvedBy;
	private String leaveType;
	private Date date;
	private Date toDate;
	private String reason;
	private boolean status;
	private boolean pending;
	private boolean completed;
	private boolean canceled;
	private long traineeId;
	private String cancellationReason;
	   @Column(columnDefinition = "TEXT")
	private String reasonDescription;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employeeLeaveId", referencedColumnName = "employeeLeaveId")
	private List<EmployeeLeaveList> employeeLeaveList;

	public List<EmployeeLeaveList> getEmployeeLeaveList() {
		return employeeLeaveList;
	}

	public void setEmployeeLeaveList(List<EmployeeLeaveList> employeeLeaveList) {
		this.employeeLeaveList = employeeLeaveList;
	}

	public long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	private LocalDate savedate = LocalDate.now();

	public LocalDate getSavedate() {
		return savedate;
	}

	public void setSavedate(LocalDate savedate) {
		this.savedate = savedate;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Long getEmployeeLeaveId() {
		return employeeLeaveId;
	}

	public void setEmployeeLeaveId(Long employeeLeaveId) {
		this.employeeLeaveId = employeeLeaveId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public boolean isApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(boolean approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

//	public int getTotalDay() {
//		return totalDay;
//	}
//	public void setTotalDay(int totalDay) {
//		this.totalDay = totalDay;
//	}
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}