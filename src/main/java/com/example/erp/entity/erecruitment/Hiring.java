package com.example.erp.entity.erecruitment;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javax.persistence.Temporal;

import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
@Table(name = "hiring")
public class Hiring {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long hiringId;
	private long applicationId;
	private long companyId;
	private String email;
	 @Column(columnDefinition = "TEXT")
	private String Requests;
	private int vacancy;
	private boolean status;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "hiringId", referencedColumnName = "hiringId")
	private List<Brief> briefList ;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "hiringId", referencedColumnName = "hiringId")
	private List<Preferred> preferredList;
	private Date currentdate;
	private Long departmentId;
	private Date closing;
	private Date posted;
	private String position;
	
	
	
	
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getCurrentdate() {
		return currentdate;
	}

	public void setCurrentdate(Date currentdate) {
		this.currentdate = currentdate;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public long getHiringId() {
		return hiringId;
	}

	public void setHiringId(long hiringId) {
		this.hiringId = hiringId;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	public Date getClosing() {
		return closing;
	}

	public void setClosing(Date closing) {
		this.closing = closing;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public int getVacancy() {
		return vacancy;
	}

	public Date getPosted() {
		return posted;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

	public void setVacancy(int vacancy) {
		this.vacancy = vacancy;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Brief> getBriefList() {
		return briefList;
	}

	public void setBriefList(List<Brief> briefList) {
		this.briefList = briefList;
	}

	public List<Preferred> getPreferredList() {
		return preferredList;
	}

	public void setPreferredList(List<Preferred> preferredList) {
		this.preferredList = preferredList;
	}

	public String getRequests() {
		return Requests;
	}

	public void setRequests(String requests) {
		Requests = requests;
	}
	
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "hiringId", referencedColumnName = "hiringId")
//	private List<Requests> requestsList;
//	public List<Requests> getRequestsList() {
//		return requestsList;
//	}
//
//	public void setRequestsList(List<Requests> requestsList) {
//		this.requestsList = requestsList;
//	}
	
	
	
}
