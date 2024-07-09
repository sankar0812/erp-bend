package com.example.erp.entity.organization;

import java.sql.Blob;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="announcements")
public class Announcements {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long announcementId;
	private String title;
	private Date fromDate;
	private Date toDate;
	@JsonIgnore
	private Blob attachment;
	private String url;
    private String informedBy;
    private String publish;
    private boolean published;
	private boolean status;
 @Column(columnDefinition = "TEXT")
	private String description;
	private boolean imageStatus;
	private String	filename;
	

	
	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public boolean isImageStatus() {
		return imageStatus;
	}
	public void setImageStatus(boolean imageStatus) {
		this.imageStatus = imageStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Blob getAttachment() {
		return attachment;
	}
	public void setAttachment(Blob attachment) {
		this.attachment = attachment;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public Announcements() {
		super();
	}
	public long getAnnouncementId() {
		return announcementId;
	}
	public void setAnnouncementId(long announcementId) {
		this.announcementId = announcementId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getInformedBy() {
		return informedBy;
	}
	public void setInformedBy(String informedBy) {
		this.informedBy = informedBy;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
