package com.example.erp.service.accounting;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.Accessories;
import com.example.erp.repository.accounting.AccessoriesRepository;

@Service
public class AccessoriesService {

	@Autowired
	private AccessoriesRepository Repo;

	public List<Accessories> listAll() {
		return this.Repo.findAll();

	}

	public void SaveorUpdate(Accessories keyboardBrand) {
		Repo.save(keyboardBrand);
	}

	public void save(Accessories keyboardBrand) {
		Repo.save(keyboardBrand);
	}

	public Accessories findById(Long keyboard_brand_id) {
		return Repo.findById(keyboard_brand_id).get();

	}

	public void deleteKeyboardBrandById(Long keyboard_brand_id) {
		Repo.deleteById(keyboard_brand_id);
	}

	public Optional<Accessories> getKeyboardBrandById(Long keyboard_brand_id) {
		return Repo.findById(keyboard_brand_id);

	}

}
