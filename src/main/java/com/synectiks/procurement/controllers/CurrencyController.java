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
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.CurrencyRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class CurrencyController {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
	private static final String ENTITY_NAME = "currency";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@PostMapping("/addCurrency")
	public  ResponseEntity<Object> addCurrency(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();
		try {
			Currency currency = new Currency();

		if (obj.get("code") != null) {
			currency.setCode(obj.get("code").asText());
		}
		if (obj.get("countryName") != null) {
			currency.setCountryName(obj.get("countryName").asText());
		}
		if (obj.get("countryCode") != null) {
			currency.setCountryCode(obj.get("countryCode").asText());
		}
		if (obj.get("symbolFilePath") != null) {
			currency.setSymbolFilePath(obj.get("symbolFilePath").asText());
		}
		
//		currency.setCreatedBy(Constants.SYSTEM_ACCOUNT);
//		currency.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
//		Instant now = Instant.now();
//		currency.setCreatedOn(now);
//		currency.setUpdatedOn(now);
		currency = currencyRepository.save(currency);
		logger.info("Add New currency SUCCESS");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New currency SUCCESS : "+currency);
		st.setObject(currency);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New currency failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New currency failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updateCurrency")
	public  ResponseEntity<Object> updateCurrency(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status(); 
		Optional<Currency> ur = currencyRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateCurrency/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Currency currency = new Currency();

			if (obj.get("code") != null) {
				currency.setCode(obj.get("code").asText());
			}
			if (obj.get("countryName") != null) {
				currency.setCountryName(obj.get("countryName").asText());
			}
			if (obj.get("countryCode") != null) {
				currency.setCountryCode(obj.get("countryCode").asText());
			}
			if (obj.get("symbolFilePath") != null) {
				currency.setSymbolFilePath(obj.get("symbolFilePath").asText());
			}
			
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
//			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
//			requisitionLineItem.setDueDate(localDate);
			
		
//		 if (obj.get("user") != null) {
//			 currency.setUpdatedBy(obj.get("user").asText());
//			 } else {
//				 currency.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
//			 }
//		 
//			 Instant now = Instant.now();
//			 currency.setUpdatedOn(now);
			 currency = currencyRepository.save(currency);
		logger.info("Updating requisition completed");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating currency SUCCESS : "+currency);
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
	public List<Currency> searchCurrency(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get currency on given filter criteria");
		Currency currency = new Currency();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			currency.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		
		if (requestObj.get("code") != null) {
			currency.setCode(requestObj.get("code"));
			isFilter = true;
		}
		if (requestObj.get("countryName") != null) {
			currency.setCountryName(requestObj.get("countryName"));
			isFilter = true;
		}
		if (requestObj.get("countryCode") != null) {
			currency.setCountryCode(requestObj.get("countryCode"));
			isFilter = true;
		}
		if (requestObj.get("symbolFilePath") != null) {
			currency.setSymbolFilePath(requestObj.get("symbolFilePath"));
			isFilter = true;
		}
		List<Currency> list = null;
		if (isFilter) {
			list = this.currencyRepository.findAll(Example.of(currency), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.currencyRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Currency>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating currency failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Currency>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}	
	}
	 @DeleteMapping("/currency/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 currencyRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}
