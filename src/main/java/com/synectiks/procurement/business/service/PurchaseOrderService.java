package com.synectiks.procurement.business.service;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.repository.PurchaseOrderRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

@Service
public class PurchaseOrderService {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

	@Autowired
	private RequisitionRepository requisitionRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrder getPurchaseOrder(Long id) {
		logger.info("Getting purchase order by id: " + id);
		Optional<PurchaseOrder> op = purchaseOrderRepository.findById(id);
		if (op.isPresent()) {
			logger.info("PurchaseOrder: " + op.get().toString());
			return op.get();
		}
		logger.warn(" Purchase order not found");
		return null;
	}

	public PurchaseOrder addPurchaseOrder(ObjectNode obj) throws JSONException {
		PurchaseOrder purchaseOrder = new PurchaseOrder();

		Optional<Requisition> oc = requisitionRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (oc.isPresent()) {
			purchaseOrder.setRequisition(oc.get());
		}

		if (obj.get("poNo") != null) {
			purchaseOrder.setPoNo(obj.get("poNo").asText());
		}
		if (obj.get("termsAndConditions") != null) {
			purchaseOrder.setTermsAndConditions(obj.get("termsAndConditions").asText());
		}
		if (obj.get("notes") != null) {
			purchaseOrder.setNotes(obj.get("notes").asText());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			purchaseOrder.setDueDate(localDate);
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			purchaseOrder.setDueDate(localDate);
		}
		purchaseOrder.setStatus(Constants.Status);
		purchaseOrder.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		purchaseOrder.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		purchaseOrder.setCreatedOn(now);
		purchaseOrder.setUpdatedOn(now);
		purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
		logger.info("Purchase order added successfully");
		return purchaseOrder;
	}

	public PurchaseOrder updatePurchaseOrder(ObjectNode obj) throws JSONException, URISyntaxException {
		Optional<PurchaseOrder> ur = purchaseOrderRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Purchase order not found");
			return null;
		}
		PurchaseOrder purchaseOrder = ur.get();

		if (obj.get("poNo") != null) {
			purchaseOrder.setPoNo(obj.get("poNo").asText());
		}
		if (obj.get("termsAndConditions") != null) {
			purchaseOrder.setTermsAndConditions(obj.get("termsAndConditions").asText());
		}
		if (obj.get("notes") != null) {
			purchaseOrder.setNotes(obj.get("notes").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			purchaseOrder.setDueDate(localDate);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); 
			c.add(Calendar.DATE, 5);
			String output = sdf.format(c.getTime());
			System.out.println(output);
		}

		if (obj.get("user") != null) {
			purchaseOrder.setUpdatedBy(obj.get("user").asText());
		} else {
			purchaseOrder.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		purchaseOrder.setUpdatedOn(now);
		purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
		logger.info("Updating purchase order completed successfully: " + purchaseOrder.toString());
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

		logger.info("Purchase order search completed. Total records: " + list.size());
		return list;
	}

	public void deletePurchaseOrder(Long id) {
		purchaseOrderRepository.deleteById(id);
	}

}
