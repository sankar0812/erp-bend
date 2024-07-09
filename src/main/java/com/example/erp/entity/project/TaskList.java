package com.example.erp.entity.project;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "taskList")
public class TaskList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskListId;
	private long departmentId;
	private long employeeId;
	private long traineeId;
	private long employeeReportId;
	private String type;
	private String projectkey;
	   @Column(columnDefinition = "TEXT")
	private String summary;
	private String projectStatus;
	   @Column(columnDefinition = "TEXT")
	private String category;
	private Date dueDate;
	private Date startDate;
	private String priority;
	private String label;
	private LocalDate created = LocalDate.now();
	private Date updated;
	private LocalDate todoDate;
	private LocalDate onProcessDate;
	private LocalDate holdDate;
	   @Column(columnDefinition = "TEXT")
	private String comments;
	private boolean status;
	private boolean hold;
	private boolean onProcess;
	private boolean todo;
	private boolean cancelled;
	private boolean completed;
	private boolean pending;
	private Date completedDate;
	   @Column(columnDefinition = "TEXT")
	private String holdReson;
	   @Column(columnDefinition = "TEXT")
	private String cancellationReason;

	
	
	
	public LocalDate getTodoDate() {
		return todoDate;
	}

	public void setTodoDate(LocalDate todoDate) {
		this.todoDate = todoDate;
	}

	public LocalDate getOnProcessDate() {
		return onProcessDate;
	}

	public void setOnProcessDate(LocalDate onProcessDate) {
		this.onProcessDate = onProcessDate;
	}

	public LocalDate getHoldDate() {
		return holdDate;
	}

	public void setHoldDate(LocalDate holdDate) {
		this.holdDate = holdDate;
	}

	public long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}

	public long getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(long taskListId) {
		this.taskListId = taskListId;
	}

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

	public long getEmployeeReportId() {
		return employeeReportId;
	}

	public void setEmployeeReportId(long employeeReportId) {
		this.employeeReportId = employeeReportId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectkey() {
		return projectkey;
	}

	public void setProjectkey(String projectkey) {
		this.projectkey = projectkey;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	public boolean isOnProcess() {
		return onProcess;
	}

	public void setOnProcess(boolean onProcess) {
		this.onProcess = onProcess;
	}

	public boolean isTodo() {
		return todo;
	}

	public void setTodo(boolean todo) {
		this.todo = todo;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getHoldReson() {
		return holdReson;
	}

	public void setHoldReson(String holdReson) {
		this.holdReson = holdReson;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public TaskList() {
		super();
	}

}
