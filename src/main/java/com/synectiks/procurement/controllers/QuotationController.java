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
import com.synectiks.procurement.business.service.QuotationService;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class QuotationController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationController.class);
	
	@Autowired
	private QuotationService quotationService;
	
	@GetMapping("/getQuotation/{id}")
	public ResponseEntity<Status> getQuotation(@PathVariable Long id) {
		logger.info("Getting quotation by id: "+id);
		Status st = new Status();
		try {
			Quotation quotation = quotationService.getQuotation(id);
			if(quotation == null) {
				logger.warn("Quotation not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Quotation not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Quotation found");
			st.setObject(quotation);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Quotation not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Quotation not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/addQuotation")
	public ResponseEntity<Status> addQuotation(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();
		try {
			Quotation quotation = quotationService.addQuotation(obj);
			if(quotation == null) {
				logger.error("Quotation could not be added.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add quotation failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Quotation created");
			st.setObject(quotation);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Add quotation failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add quotation failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	} 
			 
	@PostMapping("/updateQuotation")
	public ResponseEntity<Status> updateQuotation(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		Status st = new Status();
		try {
			Quotation quotation = quotationService.addQuotation(obj);
			if(quotation == null) {
				logger.error("Quotation could not be updated.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update quotation failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Quotation updated");
			st.setObject(quotation);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Update quotation failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Update quotation failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@GetMapping("/searchQuotation")
	public ResponseEntity<Status> searchQuotation(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search quotation on given filter criteria");
		Status st = new Status();
		try {
			List<Quotation> list = quotationService.searchQuotation(requestObj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search quotation successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Search quotation failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search quotation failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@DeleteMapping("/deleteQuotation/{id}")
    public ResponseEntity<Status> deleteQuotation(@PathVariable Long id) {
		Status st = new Status();
		try {
			quotationService.deleteQuotation(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Quotation deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable th) {
			st.setCode(HttpStatus.OK.value());
			st.setType("ERROR");
			st.setMessage("Delete quotation failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
}
