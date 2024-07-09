package com.example.erp;

public class UrlsResponse {
	private String redirectUrl;
	private String cancelUrl;

	public UrlsResponse(String redirectUrl, String cancelUrl) {
		this.redirectUrl = redirectUrl;
		this.cancelUrl = cancelUrl;
	}


	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
}
