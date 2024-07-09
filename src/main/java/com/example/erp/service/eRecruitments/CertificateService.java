package com.example.erp.service.eRecruitments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.eRecruitments.Certificate;
import com.example.erp.repository.eRecruitments.CertificateRepository;

@Service
public class CertificateService {
	@Autowired
	private CertificateRepository certificateRepository;

//view
	public List<Certificate> listAll() {
		return this.certificateRepository.findAll();
	}

//save
	public Certificate SaveCertificate(Certificate certificate) {
		return certificateRepository.save(certificate);
	}

	public Certificate findById(Long certificateId) {
		return certificateRepository.findById(certificateId).get();
	}

	// delete
	public void deleteCertificateId(Long id) {
		certificateRepository.deleteById(id);
	}
}
