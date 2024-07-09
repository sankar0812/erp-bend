package com.example.erp.controller.EmailServices;

import java.util.List;

public class EmailRequest {
	private String email;
	private String mobileNumber;
	private String description;
	private String userName;
	private String needs;
	private List<EmailRequestList> requirement;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNeeds() {
		return needs;
	}

	public void setNeeds(String needs) {
		this.needs = needs;
	}

	public List<EmailRequestList> getRequirement() {
		return requirement;
	}

	public void setRequirement(List<EmailRequestList> requirement) {
		this.requirement = requirement;
	}

	@Override
	public String toString() {
		return "EmailRequest{" + "email='" + email + '\'' + ", mobileNumber='" + mobileNumber + '\'' + ", description='"
				+ description + '\'' + ", userName='" + userName + '\'' + ", needs='" + needs + '\'' + ", "
				+ formatRequirements() + '}';
	}

	String formatRequirements() {
		if (requirement == null || requirement.isEmpty()) {
			return "Requirements: No requirements";
		}

		StringBuilder formattedRequirements = new StringBuilder("Requirements: ");
		for (EmailRequestList req : requirement) {
			formattedRequirements.append(capitalize(req.getName())).append(", ");
		}

		return formattedRequirements.substring(0, formattedRequirements.length() - 2);
	}

	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}
}
