package com.synectiks.procurement.controllers;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.VendorService;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.domain.Vendor;

@RestController
@RequestMapping("/api")
public class VendorController {
	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	@Autowired
	VendorService vendorService;

	@PostMapping("/addVendor")
	public ResponseEntity<Status> addVendor(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a vendor");
		Status st = new Status();
		try {
			Vendor vendor = vendorService.addVendor(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Vendor added successfully");
			st.setObject(vendor);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Add vendor failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateVendor")
	public ResponseEntity<Status> updateVendor(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update a vendor");
		Status st = new Status();
		try {
			Vendor vendor = vendorService.updateVendor(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Vendor updated successfully");
			st.setObject(vendor);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating vendor failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchVendor")
	public ResponseEntity<Status> searchVendor(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search vendor on given filter criteria");
		Status st = new Status();
		try {
			List<Vendor> list = vendorService.searchVendor(requestObj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("search data get SUCCESS : " + list);
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search vendor failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteVendor/{id}")
	public ResponseEntity<Status> deleteVendor(@PathVariable Long id) {
		logger.info("Request to delete a vendor");
		Status st = new Status();
		try {
			vendorService.deleteVender(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Vendor deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete vendor failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getVendor/{id}")
	public ResponseEntity<Status> getVendor(@PathVariable Long id) {
		logger.info("Getting vendor by id: " + id);
		Status st = new Status();
		try {
			Vendor vendor = vendorService.getVendor(id);
			if (vendor == null) {
				logger.warn("Vendor not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Vendor not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Vendor found");
			st.setObject(vendor);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Vendor not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Vendor not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}
