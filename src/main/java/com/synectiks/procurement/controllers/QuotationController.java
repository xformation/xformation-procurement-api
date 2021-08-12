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
import org.springframework.beans.BeanUtils;
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
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.domain.QuotationActivity;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.QuotationActivityRepository;
import com.synectiks.procurement.repository.QuotationRepository;
import com.synectiks.procurement.repository.VendorRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class QuotationController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationController.class);
	private static final String ENTITY_NAME = "quotation";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private QuotationActivityRepository quotationActivityRepository;
	@Autowired
	private QuotationRepository quotationRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private VendorRepository vendorRepository;
	
	@PostMapping("/addQuotation")
	public  ResponseEntity<Object> addQuotation(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status(); 
//		String documentId =(obj.get("documentId")).asText();
		 Optional<Document> ur = documentRepository.findById(Long.parseLong(obj.get("documentId").asText()));
		 if (!ur.isPresent()) {
//			 String vendorId =(obj.get("vendorId")).asText();
			 st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Document not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		 }
			 Optional<Vendor> cd = vendorRepository.findById(Long.parseLong(obj.get("vendorId").asText()));
			 if (!cd.isPresent()) {
				 st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("Vendor not found");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			 }
				 
					try {
						Quotation quotation = new Quotation();
						if (obj.get("departmentId") != null) {
							quotation.setDocument(ur.get());
						}
						if (obj.get("vendorId") != null) {
							quotation.setVendor(cd.get());
						}
						if (obj.get("quotationNo") != null) {
							quotation.setQuotationNo(obj.get("quotationNo").asText());
						}
					
						if (obj.get("notes") != null) {
							quotation.setNotes(obj.get("notes").asText());
						}
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
						LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
						quotation.setDueDate(localDate);
						
						quotation.setCreatedBy(Constants.SYSTEM_ACCOUNT);
						quotation.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
						Instant now = Instant.now();
						quotation.setCreatedOn(now);
						quotation.setUpdatedOn(now);
						
						quotation = quotationRepository.save(quotation);
						logger.info("Add New quotation SUCCESS");
						if(quotation.getId() != null) {
							QuotationActivity quotationActivity = new QuotationActivity();
								BeanUtils.copyProperties(quotation, quotationActivity);	
								quotationActivity.setQuotation(quotation);
								quotationActivity = quotationActivityRepository.save(quotationActivity);
								logger.info("Add New quotation Activity");
						}
						st.setCode(HttpStatus.OK.value());
						st.setType("SUCCESS");
						st.setMessage("Add New quotation SUCCESS : "+quotation);
						st.setObject(quotation);
						return ResponseEntity.status(HttpStatus.OK).body(st);
						
						
						}catch (Exception e) {
							logger.error("Add New quotation failed. Exception: ", e);
							st.setCode(HttpStatus.EXPECTATION_FAILED.value());
							st.setType("ERROR");
							st.setMessage("Add New quotation failed");
							return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
						}
			 } 
			 
	@PutMapping("/updateQuotation")
	public  ResponseEntity<Object> updateQuotation(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		Optional<Quotation> ur = quotationRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateQuotation/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Quotation quotation = new Quotation();
			if (obj.get("quotationNo") != null) {
				quotation.setQuotationNo(obj.get("quotationNo").asText());
			}
		
			if (obj.get("notes") != null) {
				quotation.setNotes(obj.get("notes").asText());
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			quotation.setDueDate(localDate);
		
		 if (obj.get("user") != null) {
			 quotation.setUpdatedBy(obj.get("user").asText());
			 } else {
				 quotation.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 quotation.setUpdatedOn(now);
			 quotation = quotationRepository.save(quotation);
		logger.info("Updating quotation completed");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating quotation SUCCESS : "+quotation);
		st.setObject(quotation);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating quotation failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating quotation failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchQuotation")
	public List<Quotation> searchQuotation(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get quotation on given filter criteria");
		Quotation quotation = new Quotation();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			quotation.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		
		if (requestObj.get("quotationNo") != null) {
			quotation.setQuotationNo(requestObj.get("quotationNo"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			quotation.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<Quotation> list = null;
		if (isFilter) {
			list = this.quotationRepository.findAll(Example.of(quotation), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.quotationRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Quotation>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating quotation failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Quotation>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/quotation/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 quotationRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}
}
