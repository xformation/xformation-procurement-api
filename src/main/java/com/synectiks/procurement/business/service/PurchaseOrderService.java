package com.synectiks.procurement.business.service;

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
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderService {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	public PurchaseOrder getPurchaseOrder(Long id) {
		logger.info("Getting purchase order by id: "+id);
		Optional<PurchaseOrder> op = purchaseOrderRepository.findById(id);
		if(op.isPresent()) {
			logger.info("PurchaseOrder: "+op.get().toString());
			return op.get();
		}
		logger.warn("PurchaseOrder not found");
		return null;
	}
	
	public PurchaseOrder addPurchaseOrder(ObjectNode obj) throws JSONException {
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
		logger.info("PurchaseOrder added successfully");
		return purchaseOrder;
	}
	
	public PurchaseOrder updatePurchaseOrder(ObjectNode obj) throws JSONException, URISyntaxException {
		Optional<PurchaseOrder> ur = purchaseOrderRepository.findById(Long.parseLong(obj.get("id").asText()));
		if(!ur.isPresent()) {
			logger.error("Purchase order not found");
			return null;
		}
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
	    logger.info("Updating purchaseOrder completed successfully: "+purchaseOrder.toString());
		return purchaseOrder;
	}
	
	public List<PurchaseOrder> searchPurchaseOrder(@RequestParam Map<String, String> requestObj) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();

		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			purchaseOrder.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		
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
		
        logger.info("PurchaseOrder searched data "+ list);
		return list;
	}
	
}
