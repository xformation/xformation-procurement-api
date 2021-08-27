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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RequisitionLineItemService;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class RequisitionLineItemController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemController.class);

	@Autowired
	private RequisitionLineItemService requisitionLineItemService;

	@PostMapping("/addRequisitionLineItem")
	public ResponseEntity<Object> addRequisitionLineItem(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a requisition line item");
		Status st = new Status();
		try {
			RequisitionLineItem requisitionLineItem = requisitionLineItemService.addRequisitionLineItem(obj);
			if(requisitionLineItem == null) {
				logger.error("Add requisition line item failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add requisition line item failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Add requisition line item successful");
			st.setObject(requisitionLineItem);
			return ResponseEntity.status(HttpStatus.OK).body(st);

		} catch (Exception e) {
			logger.error("Add New requisition line item failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add New requisition line item failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PutMapping("/updateRequisitionLineItem")
	public ResponseEntity<Object> updateRequisitionLineItem(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		logger.info("Request to update a requsition");
		Status st = new Status(); 
		try {
			RequisitionLineItem requisitionLineItem = requisitionLineItemService.updateRequisitionLineItem(obj);
			if(requisitionLineItem == null) {
				logger.error("Update requisition line item failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update requisition line item failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Update requisition line item successful");
			st.setObject(requisitionLineItem);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Update requisition line item failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Update requisition line item failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@GetMapping("/searchRequisitionLineItem")
	public ResponseEntity<Status> searchRequisitionLineItem(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisition line item on given filter criteria");
		Status st = new Status();
		try {
			List<RequisitionLineItem> list = requisitionLineItemService.searchRequisitionLineItem(requestObj);
			if(list == null) {
				logger.error("Search requisition line item failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search requisition line item failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search requisition line item successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Searching requisition line item failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search requisition line item failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@DeleteMapping("/deleteRequisitionLineItem/{id}")
	public ResponseEntity<Status> deleteRequisitionLineItem(@PathVariable Long id) {
		Status st = new Status();
		try {
			requisitionLineItemService.deleteRequisitionLineItem(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete requisitionLineItem successful");
			return ResponseEntity.status(HttpStatus.OK).body(st); 
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete requisitionLineItem failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st); 
		}
	}
	@GetMapping("/getRequisitionLineItem/{id}")
	public ResponseEntity<Status> getRequisitionLineItem(@PathVariable Long id) {
		logger.info("Getting requisitionLineItem by id: " + id);
		Status st = new Status();
		try {
			RequisitionLineItem requisitionLineItem = requisitionLineItemService.getRequisitionLineItem(id);
			if (requisitionLineItem == null) {
				logger.warn("RequisitionLineItem not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("RequisitionLineItem not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Contact found");
			st.setObject(requisitionLineItem);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("RequisitionLineItem not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("RequisitionLineItem not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
}
	

	

