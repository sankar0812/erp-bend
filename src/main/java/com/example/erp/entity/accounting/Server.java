package com.example.erp.entity.accounting;

import java.sql.Blob;
import java.sql.Date;
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
@Table(name = "server")
public class Server {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serverId;
	private String serverName;
	private String paying;
	@JsonIgnore
	private Blob serverFileUpload;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String serverTypeUrl;

	private Date date;
	private boolean Status;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_serverId", referencedColumnName = "serverId")
	private List<ServerList> serverList;
	
	

	public String getPaying() {
		return paying;
	}

	public void setPaying(String paying) {
		this.paying = paying;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<ServerList> getServerList() {
		return serverList;
	}

	public void setServerList(List<ServerList> serverList) {
		this.serverList = serverList;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Blob getServerFileUpload() {
		return serverFileUpload;
	}

	public void setServerFileUpload(Blob serverFileUpload) {
		this.serverFileUpload = serverFileUpload;
	}

	public String getServerTypeUrl() {
		return serverTypeUrl;
	}

	public void setServerTypeUrl(String serverTypeUrl) {
		this.serverTypeUrl = serverTypeUrl;
	}

	public Server() {
		super();
	}

}
