package com.example.erp.entity.accounting;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="accessories")
public class Accessories {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accessoriesId;
	private String accessoriesName;
    private String color;  
	@JsonIgnore
	private Blob image;
	private String url;
    
	
    

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getAccessoriesId() {
		return accessoriesId;
	}

	public void setAccessoriesId(Long accessoriesId) {
		this.accessoriesId = accessoriesId;
	}

	public String getAccessoriesName() {
		return accessoriesName;
	}

	public void setAccessoriesName(String accessoriesName) {
		this.accessoriesName = accessoriesName;
	}
	
	
}
