package com.example.erp.entity.project;

import java.sql.Date;
import java.time.LocalDate;
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
@Table(name = "projectAssigning")
public class ProjectAssigning {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long projectAssigningId;
	private long projectId;
	private Date date;
	private String typeOfProject;
	private String projectStatus;
	private boolean accepted;
	private boolean rejected;
	private long clientId;
	private String url;
	private LocalDate todayDate;
	private long employeeId;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "projectAssigningId", referencedColumnName = "projectAssigningId")
	private List<DepartmentList> departmentList;

	
	
	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public LocalDate getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(LocalDate todayDate) {
		this.todayDate = todayDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getProjectAssigningId() {
		return projectAssigningId;
	}

	public void setProjectAssigningId(long projectAssigningId) {
		this.projectAssigningId = projectAssigningId;
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

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public List<DepartmentList> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<DepartmentList> departmentList) {
		this.departmentList = departmentList;
	}

	public ProjectAssigning() {
		super();
	}

}
