package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.Brand;
import com.example.erp.repository.accounting.BrandRepository;



@Service
public class BrandService {
	
	@Autowired
	private BrandRepository  Repo;

	public Iterable<Brand> listAll() {
		return this.Repo.findAll();

	}

	public void SaveorUpdate(Brand brand) {
		Repo.save(brand);
	}

	public void save(Brand brand) {
		Repo.save(brand);

	}

	public Brand findById(Long brand_id) {
		return Repo.findById(brand_id).get();

	}

	public void deleteBrandById(Long brand_id) {
		Repo.deleteById(brand_id);
	}

	public Optional<Brand> getBrandById(Long brand_id) {
		return Repo.findById(brand_id);

	}
	

}
