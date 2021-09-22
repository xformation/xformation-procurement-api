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
import com.synectiks.procurement.business.service.BuyerService;
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class BuyerController {
	private static final Logger logger = LoggerFactory.getLogger(BuyerController.class);

	@Autowired
	BuyerService buyerService;

	@PostMapping("/addBuyer")
	public ResponseEntity<Status> addBuyer(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a buyer");
		Status st = new Status();
		try {
			Buyer buyer = buyerService.addBuyer(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Buyer added successfully");
			st.setObject(buyer);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Add buyer failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add buyer failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateBuyer")
	public ResponseEntity<Status> updateBuyer(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update a buyer");
		Status st = new Status();
		try {
			Buyer buyer = buyerService.updateBuyer(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Buyer updated successfully");
			st.setObject(buyer);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating buyer failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating buyer failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchBuyer")
	public ResponseEntity<Status> searchBuyer(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search buyer on given filter criteria");
		Status st = new Status();
		try {
			List<Buyer> list = buyerService.searchBuyer(requestObj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("search data get SUCCESS : " + list);
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search buyer failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search buyer failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deleteBuyer/{id}")
	public ResponseEntity<Status> deleteBuyer(@PathVariable Long id) {
		logger.info("Request to delete a buyer");
		Status st = new Status();
		try {
			buyerService.deleteBuyer(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Buyer deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete buyer failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getBuyer/{id}")
	public ResponseEntity<Status> getBuyer(@PathVariable Long id) {
		logger.info("Getting buyer by id: " + id);
		Status st = new Status();
		try {
			Buyer buyer = buyerService.getBuyer(id);
			if (buyer == null) {
				logger.warn("Buyer not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Buyer not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("buyer found");
			st.setObject(buyer);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Buyer not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Buyer not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}
