package com.example.erp.service.eRecruitment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.Offer;
import com.example.erp.repository.erecruitment.OfferRepository;

@Service
public class OfferService {
@Autowired
private OfferRepository offerRepo;

//view
	public List<Offer> listAll() {
		return this.offerRepo.findAll();
	}

//save
	public Offer SaveOfferLetter(Offer offer) {
		return offerRepo.save(offer);
	}
	
	public Offer getByCandidateId(Long candidateid) {
		return offerRepo.findBycandidateId(candidateid).get();
	}

	//delete
		public void deleteOfferId(Long id) {
			offerRepo.deleteById(id);
		}

	
}
