package com.example.erp.entity.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "serverType")
public class ServerType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serverTypeId;
	private String serverTypeName;
	public Long getServerTypeId() {
		return serverTypeId;
	}
	public void setServerTypeId(Long serverTypeId) {
		this.serverTypeId = serverTypeId;
	}
	public String getServerTypeName() {
		return serverTypeName;
	}
	public void setServerTypeName(String serverTypeName) {
		this.serverTypeName = serverTypeName;
	}
	
	
}
