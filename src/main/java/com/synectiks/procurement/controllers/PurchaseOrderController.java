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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.PurchaseOrderService;
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class PurchaseOrderController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@PostMapping("/addPurchaseOrder")
	public ResponseEntity<Status> addPurchaseOrder(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add purchase order");
		Status st = new Status();	
		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.addPurchaseOrder(obj);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("PurchaseOrder added successfully");
			st.setObject(purchaseOrder);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Add purchase order failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/updatePurchaseOrder")
	public  ResponseEntity<Status> updatePurchaseOrder(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update purchase order");
		Status st = new Status();
		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(obj);	
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Purchase order updated successfully");
			st.setObject(purchaseOrder);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Purchase order update failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Purchase order update failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchPurchaseOrder")
	public ResponseEntity<Status> searchPurchaseOrder(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get purchase orders on given filter criteria");
		try {
			List<PurchaseOrder> list = purchaseOrderService.searchPurchaseOrder(requestObj);
			Status st = new Status();
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search purchase orders successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		}catch (Exception e) {
			logger.error("Search purchase order failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}	
}
