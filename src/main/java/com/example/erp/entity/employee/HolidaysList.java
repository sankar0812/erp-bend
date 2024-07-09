package com.example.erp.entity.employee;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "holidaysList")
public class HolidaysList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long holidaysListId;
	private Date date;
	  private boolean status;
	private String day;

	
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getHolidaysListId() {
		return holidaysListId;
	}

	public void setHolidaysListId(Long holidaysListId) {
		this.holidaysListId = holidaysListId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
