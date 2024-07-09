package com.example.erp.entity.employee;

import java.sql.Blob;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "awardsphoto")
public class AwardsPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "awardsPhotoId")
	private long awardsPhotoId;
	private String url;
	@Lob
	@JsonIgnore
	private Blob awardsPhoto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "awardsId", referencedColumnName = "awardsId")
	private Awards awards;

	public long getAwardsPhotoId() {
		return awardsPhotoId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setAwardsPhotoId(long awardsPhotoId) {
		this.awardsPhotoId = awardsPhotoId;
	}

	public Blob getAwardsPhoto() {
		return awardsPhoto;
	}

	public void setAwardsPhoto(Blob awardsPhoto) {
		this.awardsPhoto = awardsPhoto;
	}

//	public Awards getAwards() {
//		return awards;
//	}
//
//	public void setAwards(Awards awards) {
//		this.awards = awards;
//	}

	public AwardsPhoto() {
		super();
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "awardsPhotoId")
//	private long awardsPhotoId;
//	@Lob
//    @JsonIgnore
//	private Blob awardsPhoto;

}
