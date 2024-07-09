package com.example.erp.controller.accounting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.accounting.Brand;
import com.example.erp.service.accounting.BrandService;

@RestController
@CrossOrigin
public class BrandController {

	@Autowired

	private BrandService brandService;

	@GetMapping("/brand")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String brand) {
	    try {
	        if ("brand".equals(brand)) {

			Iterable<Brand> assestDetails = brandService.listAll();
			List<Brand> sortedAssets = StreamSupport.stream(assestDetails.spliterator(), false)
					.sorted(Comparator.comparing(Brand::getBrandId).reversed()).collect(Collectors.toList());
			return new ResponseEntity<>(sortedAssets, HttpStatus.OK);
		}
	        else {	   
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided brand is not supported.");
	        }
	    }catch (Exception e) {
			String errorMessage = "An error occurred while retrieving l details.";

			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	

	@PostMapping("/brand/save")

	public ResponseEntity<?> saveBank(@RequestBody Brand brand) {

		try {
//			assest.setStatus(true);
			brandService.SaveorUpdate(brand);

			return ResponseEntity.status(HttpStatus.CREATED).body("brand details saved successfully.");

		} catch (Exception e) {

			String errorMessage = "An error occurred while saving brand details.";

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

		}

	}

	@RequestMapping("/brand/{brandId}")
	private Optional<Brand> getBrand(@PathVariable(name = "brandId") long brandId) {

		return brandService.getBrandById(brandId);

	}

	@PutMapping("/brand/edit/{brandId}")
	public ResponseEntity<Brand> updateBrand(@PathVariable("brandId") Long brandId, @RequestBody Brand brandDetails) {

		try {

			Brand existingBrand = brandService.findById(brandId);

			if (existingBrand == null) {

				return ResponseEntity.notFound().build();

			}

			existingBrand.setBrandName(brandDetails.getBrandName());
			brandService.save(existingBrand);

			return ResponseEntity.ok(existingBrand);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

	@DeleteMapping("/brand/delete/{brandId}")
	public ResponseEntity<String> deleteBrand(@PathVariable("brandId") Long brandId) {

		brandService.deleteBrandById(brandId);

		return ResponseEntity.ok("Brand deleted successfully");

	}

}
