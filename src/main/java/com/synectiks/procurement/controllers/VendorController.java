package com.synectiks.procurement.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.VendorRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class VendorController {
	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);
	private static final String ENTITY_NAME = "vendor";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private VendorRepository vendorRepository;
	
	@PostMapping("/addVendor")
	public  ResponseEntity<Object> addVendor(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();
		try {
			Vendor vendor = new Vendor();

		if (obj.get("firstName") != null) {
			vendor.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			vendor.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			vendor.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			vendor.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			vendor.setEmail(obj.get("email").asText());
		}
		if (obj.get("address") != null) {
			vendor.setAddress(obj.get("address").asText());
		}
		if (obj.get("zipCode") != null) {
			vendor.setZipCode(obj.get("zipCode").asText());
		}
		
		vendor.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		vendor.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		vendor.setCreatedOn(now);
		vendor.setUpdatedOn(now);
		vendor = vendorRepository.save(vendor);
		logger.info("Add New Vendor SUCCESS");

		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New Vendor SUCCESS : "+vendor);
		st.setObject(vendor);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New Vendor failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New Vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updateVendor")
	public  ResponseEntity<Object> updateRequisitionLineItem(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		 Optional<Vendor> ur = vendorRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateVendor/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Vendor vendor = new Vendor();

			if (obj.get("firstName") != null) {
				vendor.setFirstName(obj.get("firstName").asText());
			}
			if (obj.get("middleName") != null) {
				vendor.setMiddleName(obj.get("middleName").asText());
			}
			if (obj.get("lastName") != null) {
				vendor.setLastName(obj.get("lastName").asText());
			}
			if (obj.get("phoneNumber") != null) {
				vendor.setPhoneNumber(obj.get("phoneNumber").asText());
			}
			if (obj.get("email") != null) {
				vendor.setEmail(obj.get("email").asText());
			}
			if (obj.get("address") != null) {
				vendor.setAddress(obj.get("address").asText());
			}
			if (obj.get("zipCode") != null) {
				vendor.setZipCode(obj.get("zipCode").asText());
			}
		
		 if (obj.get("user") != null) {
			 vendor.setUpdatedBy(obj.get("user").asText());
			 } else {
				 vendor.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 vendor.setUpdatedOn(now);
			 vendor = vendorRepository.save(vendor);
		logger.info("Updating requisition completed");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating Vendor SUCCESS : "+vendor);
		st.setObject(vendor);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating Vendor failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating Vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchVendor")
	public List<Vendor> searchVendor(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisitionLineItem on given filter criteria");
		Vendor vendor = new Vendor();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			vendor.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		
		if (requestObj.get("firstName") != null) {
			vendor.setFirstName(requestObj.get("firstName"));
			isFilter = true;
		}
		if (requestObj.get("lastName") != null) {
			vendor.setLastName(requestObj.get("lastName"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			vendor.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			vendor.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("address") != null) {
			vendor.setAddress(requestObj.get("address"));
			isFilter = true;
		}
		if (requestObj.get("zipCode") != null) {
			vendor.setZipCode(requestObj.get("zipCode"));
			isFilter = true;
		}
		List<Vendor> list = null;
		if (isFilter) {
			list = this.vendorRepository.findAll(Example.of(vendor), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.vendorRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Vendor>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating Vendor failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Vendor>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}	
	}
	 @DeleteMapping("/vendor/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 vendorRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}
