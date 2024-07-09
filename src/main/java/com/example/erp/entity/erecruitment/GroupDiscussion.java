package com.example.erp.entity.erecruitment;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groupDiscussion")
public class GroupDiscussion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long groupDiscussionId;
	private long candidateId;
	private String topic;
	private Date date;
	private String time;
	 @Column(columnDefinition = "TEXT")
	private String feedback;
	private boolean scheduled;
	private boolean completed;
	private boolean status;
	private String approvalType;
	private boolean canceled;
	   @Column(columnDefinition = "TEXT")
	private String cancellationReason;	

	
	

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public long getGroupDiscussionId() {
		return groupDiscussionId;
	}

	public void setGroupDiscussionId(long groupDiscussionId) {
		this.groupDiscussionId = groupDiscussionId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	


	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public GroupDiscussion() {
		super();
	}

}
