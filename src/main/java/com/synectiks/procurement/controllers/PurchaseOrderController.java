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
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.PurchaseOrderRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PurchaseOrderController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);
	private static final String ENTITY_NAME = "purchaseOrder";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@PostMapping("/addPurchaseOrder")
	public  ResponseEntity<Object> addPurchaseOrder(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();	
		try {
			PurchaseOrder purchaseOrder = new PurchaseOrder();

		if (obj.get("poNo") != null) {
			purchaseOrder.setPoNo(obj.get("poNo").asText());
		}
		if (obj.get("termsAndConditions") != null) {
			purchaseOrder.setTermsAndConditions(obj.get("termsAndConditions").asText());
		}
		if (obj.get("notes") != null) {
			purchaseOrder.setNotes(obj.get("notes").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
		purchaseOrder.setDueDate(localDate);
		purchaseOrder.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		purchaseOrder.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		purchaseOrder.setCreatedOn(now);
		purchaseOrder.setUpdatedOn(now);
		purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
		logger.info("Add New purchaseOrder SUCCESS");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Add New purchaseOrder SUCCESS : "+purchaseOrder);
		st.setObject(purchaseOrder);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Add New purchaseOrder failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New purchaseOrder failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@PutMapping("/updatePurchaseOrder")
	public  ResponseEntity<Object> updatePurchaseOrder(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status(); 
		Optional<PurchaseOrder> ur = purchaseOrderRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updatePurchaseOrder/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			PurchaseOrder purchaseOrder = new PurchaseOrder();


			if (obj.get("poNo") != null) {
				purchaseOrder.setPoNo(obj.get("poNo").asText());
			}
			if (obj.get("termsAndConditions") != null) {
				purchaseOrder.setTermsAndConditions(obj.get("termsAndConditions").asText());
			}
			if (obj.get("notes") != null) {
				purchaseOrder.setNotes(obj.get("notes").asText());
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			purchaseOrder.setDueDate(localDate);
			
		
		 if (obj.get("user") != null) {
			 purchaseOrder.setUpdatedBy(obj.get("user").asText());
			 } else {
				 purchaseOrder.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 purchaseOrder.setUpdatedOn(now);
			 purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
		logger.info("Updating purchaseOrder completed");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating purchaseOrder SUCCESS : "+purchaseOrder);
		st.setObject(purchaseOrder);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating purchaseOrder failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating purchaseOrder failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchPurchaseOrder")
	public List<PurchaseOrder> searchPurchaseOrder(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get purchaseOrder on given filter criteria");
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			purchaseOrder.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		
		if (requestObj.get("poNo") != null) {
			purchaseOrder.setPoNo(requestObj.get("poNo"));
			isFilter = true;
		}
		if (requestObj.get("termsAndConditions") != null) {
			purchaseOrder.setTermsAndConditions(requestObj.get("termsAndConditions"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			purchaseOrder.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<PurchaseOrder> list = null;
		if (isFilter) {
			list = this.purchaseOrderRepository.findAll(Example.of(purchaseOrder), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.purchaseOrderRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<PurchaseOrder>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating purchaseOrder failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<PurchaseOrder>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/purchaseOrder/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 purchaseOrderRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}	
}
