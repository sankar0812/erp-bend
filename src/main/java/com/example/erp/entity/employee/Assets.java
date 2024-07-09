package com.example.erp.entity.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "assets")
public class Assets {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long assetsId;
	private Long employeeId;
	private Long departmentId;
	private long traineeId;
	private LocalDate assetsDate = LocalDate.now();
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_assetsId", referencedColumnName = "assetsId")
	private List<AssetsList> assets;

	
	public long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}

	public long getAssetsId() {
		return assetsId;
	}

	public void setAssetsId(long assetsId) {
		this.assetsId = assetsId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public LocalDate getAssetsDate() {
		return assetsDate;
	}

	public void setAssetsDate(LocalDate assetsDate) {
		this.assetsDate = assetsDate;
	}

	public List<AssetsList> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetsList> assets) {
		this.assets = assets;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	
	
	
}
