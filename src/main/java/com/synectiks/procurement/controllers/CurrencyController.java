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
import com.synectiks.procurement.business.service.CurrencyService;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class CurrencyController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
	
	@Autowired
	private CurrencyService currencyService;
	
	@PostMapping("/addCurrency")
	public  ResponseEntity<Status> addCurrency(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add Currency");
		Status st = new Status();
		try {
			Currency currency = currencyService.addCurrency(obj);
			if(currency == null) {
				logger.error("Add currency failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add currency failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Currency addes successfully");
			st.setObject(currency);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Currency add failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Currency add failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PostMapping("/updateCurrency")
	public  ResponseEntity<Status> updateCurrency(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to Updating Currency");
		Status st = new Status(); 
		try {
			Currency currency = currencyService.updateCurrency(obj);
			if (currency == null) {
				logger.error("Currency could not be updated.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update currency failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Updating currency successful");
			st.setObject(currency);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Updating currency failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating currency failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchCurrency")
	public ResponseEntity<Status> searchCurrency(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get currency on given filter criteria");
		Status st = new Status();
		try {
			List<Currency> list = currencyService.searchCurrency(requestObj);
			if(list == null) {
				logger.error("Search currency failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search currency failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search currency successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Search currency failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search currency failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}	
	}
	
	@DeleteMapping("/currency/{id}")
	public ResponseEntity<Status> deleteCurrency(@PathVariable Long id) {
		Status st = new Status();
		try {
		    currencyService.deleteCurrency(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete currency successful");
			return ResponseEntity.status(HttpStatus.OK).body(st); 
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete currency failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st); 
		}
	}
	
	@GetMapping("/getCurrency/{id}")
	public ResponseEntity<Status> getCurrency(@PathVariable Long id) {
		logger.info("Getting currency by id: " + id);
		Status st = new Status();
		try {
			Currency currency = currencyService.getCurrency(id);
			if (currency == null) {
				logger.warn("Currency not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Currency not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Currency found");
			st.setObject(currency);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Currency not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Currency not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}
