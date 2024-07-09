package com.example.erp.entity.employee;

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
@Table(name="holidays")
public class Holidays {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long holidaysId;
	    private String title;
	  
	    private boolean status;
	    @OneToMany(cascade = CascadeType.ALL)
	    @JoinColumn(name = "holidaysId", referencedColumnName = "holidaysId")
		private List<HolidaysList> leaveList;
	    
	
		public Long getHolidaysId() {
			return holidaysId;
		}
		public void setHolidaysId(Long holidaysId) {
			this.holidaysId = holidaysId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	
		
		public List<HolidaysList> getLeaveList() {
			return leaveList;
		}
		public void setLeaveList(List<HolidaysList> leaveList) {
			this.leaveList = leaveList;
		}
		public boolean isStatus() {
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
	    
	    
	    
	    

}
