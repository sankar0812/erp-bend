package com.example.erp.service.eRecruitment;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.repository.erecruitment.CandidateRepository;

@Service
public class CandidateService {
@Autowired
private CandidateRepository candidateRepository;


//view
	public List<Candidate> listAll() {
		return this.candidateRepository.findAll();
	}

//save
	public Candidate SaveCandidateDetails(Candidate candidate) {
		return candidateRepository.save(candidate);
	}
	
	public Candidate findById(Long candidateId) {
		return candidateRepository.findById(candidateId).get();
	}

	//delete
		public void deleteCandidateId(Long id) {
			candidateRepository.deleteById(id);
		}
}

