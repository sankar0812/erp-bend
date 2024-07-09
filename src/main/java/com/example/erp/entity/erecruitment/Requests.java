package com.example.erp.entity.erecruitment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "requestsList")
public class Requests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long requestsId;
	   @Column(columnDefinition = "TEXT")
	private String  requests;
	
	
	public long getRequestsId() {
		return requestsId;
	}
	public void setRequestsId(long requestsId) {
		this.requestsId = requestsId;
	}
	public String getRequests() {
		return requests;
	}
	public void setRequests(String requests) {
		this.requests = requests;
	}
	
	
	
}
