package com.example.erp.entity.project;

import java.sql.Blob;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskId;
	private long projectId;
	private Date date;
	private String typeOfProject;
	private String projectStatus;
	private boolean completed;
	private boolean notCompleted;
	private boolean research;
	private boolean development;
	private LocalDate todayDate;
	private LocalDate testingDate;
	private LocalDate projectDate;
	private LocalDate hostingDate;
	private LocalDate completedDate;
	private boolean testing;
	private long employeeReportId;
	private boolean hosting;
	private boolean project;
	@JsonIgnore
	private Blob fileUpload;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String url;
	private String sharingFileUrl;
	private long clientId;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "taskId", referencedColumnName = "taskId")
	private List<TaskList> taskList;
	
	




	public LocalDate getTestingDate() {
		return testingDate;
	}

	public void setTestingDate(LocalDate testingDate) {
		this.testingDate = testingDate;
	}

	public LocalDate getProjectDate() {
		return projectDate;
	}

	public void setProjectDate(LocalDate projectDate) {
		this.projectDate = projectDate;
	}

	public LocalDate getHostingDate() {
		return hostingDate;
	}

	public void setHostingDate(LocalDate hostingDate) {
		this.hostingDate = hostingDate;
	}

	public long getEmployeeReportId() {
		return employeeReportId;
	}

	public void setEmployeeReportId(long employeeReportId) {
		this.employeeReportId = employeeReportId;
	}

	public boolean isHosting() {
		return hosting;
	}

	public void setHosting(boolean hosting) {
		this.hosting = hosting;
	}

	public LocalDate getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(LocalDate completedDate) {
		this.completedDate = completedDate;
	}

	public LocalDate getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(LocalDate todayDate) {
		this.todayDate = todayDate;
	}

	public boolean isProject() {
		return project;
	}

	public void setProject(boolean project) {
		this.project = project;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getSharingFileUrl() {
		return sharingFileUrl;
	}

	public void setSharingFileUrl(String sharingFileUrl) {
		this.sharingFileUrl = sharingFileUrl;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTypeOfProject() {
		return typeOfProject;
	}

	public void setTypeOfProject(String typeOfProject) {
		this.typeOfProject = typeOfProject;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isNotCompleted() {
		return notCompleted;
	}

	public void setNotCompleted(boolean notCompleted) {
		this.notCompleted = notCompleted;
	}

	public boolean isResearch() {
		return research;
	}

	public void setResearch(boolean research) {
		this.research = research;
	}

	public boolean isDevelopment() {
		return development;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}

	public boolean isTesting() {
		return testing;
	}

	public void setTesting(boolean testing) {
		this.testing = testing;
	}

	public Blob getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(Blob fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<TaskList> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskList> taskList) {
		this.taskList = taskList;
	}

	public Task() {
		super();
	}

}
