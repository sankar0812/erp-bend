package com.example.erp.entity.message;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "chat_table")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonDeserialize(using = MessageDeserializer.class)
	private Long messageId;
	@JsonProperty("senderId")
	private Long senderId;
	@JsonProperty("receiverId")
	private Long receiverId;
	@JsonProperty("message")
	@Column(columnDefinition = "TEXT")
	private String message;
	@Column(name = "intime", columnDefinition = "TIME")
	private LocalTime intime = LocalTime.now();
	private LocalDate date = LocalDate.now();
	private Status status;
	private String senderName;
	private String receiverName;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getSenderId() {
		return senderId;
	}

	@JsonProperty("senderId")
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	@JsonProperty("receiverId")
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	public LocalTime getIntime() {
		return intime;
	}

	public void setIntime(LocalTime intime) {
		this.intime = intime;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Message(Long messageId, Long senderId, Long receiverId, String message, LocalTime intime, LocalDate date,
			Status status, String senderName, String receiverName) {
		super();
		this.messageId = messageId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.message = message;
		this.intime = intime;
		this.date = date;
		this.status = status;
		this.senderName = senderName;
		this.receiverName = receiverName;
	}

	public Message() {
		super();
	}

}
