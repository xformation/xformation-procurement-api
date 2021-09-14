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
			if (purchaseOrder == null) {
				logger.error("Add purchase order failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add purchase order failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Purchase Order added successfully");
			st.setObject(purchaseOrder);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Add purchase order failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updatePurchaseOrder")
	public ResponseEntity<Status> updatePurchaseOrder(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		logger.info("Request to update purchase order");
		Status st = new Status();
		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(obj);
			if (purchaseOrder == null) {
				logger.error("Update purchase order failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update purchase order failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Purchase order updated successfully");
			st.setObject(purchaseOrder);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
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
		Status st = new Status();
		try {
			List<PurchaseOrder> list = purchaseOrderService.searchPurchaseOrder(requestObj);
			if (list == null) {
				logger.error("Search purchase order failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search purchase order failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search purchase orders successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search purchase order failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@DeleteMapping("/deletePurchaseOrder/{id}")
	public ResponseEntity<Status> deletePurchaseOrder(@PathVariable Long id) {
		Status st = new Status();
		try {
			purchaseOrderService.deletePurchaseOrder(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete purchase order successful");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getPurchaseOrder/{id}")
	public ResponseEntity<Status> getPurchaseOrder(@PathVariable Long id) {
		logger.info("Getting invoice by id: " + id);
		Status st = new Status();
		try {
			PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(id);
			if (purchaseOrder == null) {
				logger.warn("Purchase order not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Purchase order not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Purchase order found");
			st.setObject(purchaseOrder);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Purchase order not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Purchase order not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
	
	@PostMapping("/approvePurchaseOrder")
	public ResponseEntity<Status> approvePurchaseOrder(@RequestBody ObjectNode obj)
			throws JSONException {
		logger.info("Request to approve a purchase order");
		
		Status st = new Status();
		try {
			boolean updateFlag = purchaseOrderService.approvePurchaseOrder(obj);
			if (updateFlag) {
				st.setCode(HttpStatus.OK.value());
				st.setType("SUCCESS");
				st.setMessage("purchase order approved successfully");
			} else {
				st.setCode(HttpStatus.OK.value());
				st.setType("Fialed");
				st.setMessage("purchase order could not be updated");
			}
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Approve purchase order failed");
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Approve purchase order failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}
}
