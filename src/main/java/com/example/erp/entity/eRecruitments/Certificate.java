package com.example.erp.entity.eRecruitments;

import java.sql.Blob;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="certificate")
public class Certificate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long certificateId;
	private long traineeId;
	private String trainingProgram;
	private String hospitalName;
	private Date certificateIssuedDate;
	private Blob authorizedSignature;
	private Blob traineeSignature;
	private Blob officialLogo;
	private boolean status;
	private String authorizedSignatureUrl;
	private String traineeSignatureUrl;
	private String officialLogoUrl;
	public long getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}
	public long getTraineeId() {
		return traineeId;
	}
	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}
	public String getTrainingProgram() {
		return trainingProgram;
	}
	public void setTrainingProgram(String trainingProgram) {
		this.trainingProgram = trainingProgram;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public Date getCertificateIssuedDate() {
		return certificateIssuedDate;
	}
	public void setCertificateIssuedDate(Date certificateIssuedDate) {
		this.certificateIssuedDate = certificateIssuedDate;
	}
	public Blob getAuthorizedSignature() {
		return authorizedSignature;
	}
	public void setAuthorizedSignature(Blob authorizedSignature) {
		this.authorizedSignature = authorizedSignature;
	}
	public Blob getTraineeSignature() {
		return traineeSignature;
	}
	public void setTraineeSignature(Blob traineeSignature) {
		this.traineeSignature = traineeSignature;
	}
	public Blob getOfficialLogo() {
		return officialLogo;
	}
	public void setOfficialLogo(Blob officialLogo) {
		this.officialLogo = officialLogo;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getAuthorizedSignatureUrl() {
		return authorizedSignatureUrl;
	}
	public void setAuthorizedSignatureUrl(String authorizedSignatureUrl) {
		this.authorizedSignatureUrl = authorizedSignatureUrl;
	}
	public String getTraineeSignatureUrl() {
		return traineeSignatureUrl;
	}
	public void setTraineeSignatureUrl(String traineeSignatureUrl) {
		this.traineeSignatureUrl = traineeSignatureUrl;
	}
	public String getOfficialLogoUrl() {
		return officialLogoUrl;
	}
	public void setOfficialLogoUrl(String officialLogoUrl) {
		this.officialLogoUrl = officialLogoUrl;
	}
	public Certificate() {
		super();
	}
	
}
