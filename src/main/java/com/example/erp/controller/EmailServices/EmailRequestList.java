package com.example.erp.controller.EmailServices;

public class EmailRequestList {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        return "Requirement{" +
                "name='" + name + '\'' +
                '}';
    }
}
