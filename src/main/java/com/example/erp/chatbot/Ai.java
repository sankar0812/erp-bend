package com.example.erp.chatbot;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.*;

@Entity
@Table(name = "ai")
public class Ai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String prompt;
    
	@Column(name = "intime", columnDefinition = "VARCHAR(12)")
	private String intime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Ai(Long id, String role, String content) {
		super();
		this.id = id;
		this.role = role;
		this.content = content;
	}
	public Ai() {
		super();
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getIntime() {
		return intime;
	}
	public void setIntime(String intime) {
		this.intime = intime;
	}

   
}
