package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.Issue;
import com.example.erp.repository.project.IssueRepository;

@Service
public class IssueService {

	@Autowired
	private IssueRepository issueRepository;

	// view
	public List<Issue>listIssue() {
		return this.issueRepository.findAll();
	}

	// save
	public Issue SaveIssueDetails(Issue issue) {
		return issueRepository.save(issue);
	}

	public Issue findIssueId(Long issueId) {
		return issueRepository.findById(issueId).get();
	}

	// delete
	public void deleteIssueById(Long id) {
		issueRepository.deleteById(id);
	}
}
