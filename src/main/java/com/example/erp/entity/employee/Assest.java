package com.example.erp.entity.employee;


import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assest")
public class Assest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long assestId;
	private long employeeId;
	private String productName;
	private long serialNumber;
	private long modelNumber;
	private String brand;
	private String keyboardBrand;
	private String mouseBrand;
	private boolean status;
	private LocalDate date = LocalDate.now();


	public Assest() {
		super();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public long getAssestId() {
		return assestId;
	}

	public void setAssestId(long assestId) {
		this.assestId = assestId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public long getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(long modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getKeyboardBrand() {
		return keyboardBrand;
	}

	public void setKeyboardBrand(String keyboardBrand) {
		this.keyboardBrand = keyboardBrand;
	}

	public String getMouseBrand() {
		return mouseBrand;
	}

	public void setMouseBrand(String mouseBrand) {
		this.mouseBrand = mouseBrand;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
