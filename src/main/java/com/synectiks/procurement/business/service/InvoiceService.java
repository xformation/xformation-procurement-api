package com.synectiks.procurement.business.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.domain.InvoiceActivity;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.InvoiceActivityRepository;
import com.synectiks.procurement.repository.InvoiceRepository;
import com.synectiks.procurement.repository.QuotationRepository;

@Service
public class InvoiceService {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
	
	@Autowired
	private InvoiceActivityRepository invoiceActivityRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	public Invoice addInvoice(ObjectNode obj) throws JSONException {
		Invoice invoice = new Invoice();
		
		Optional<Document> odc = documentRepository.findById(Long.parseLong(obj.get("documentId").asText()));
		if (odc.isPresent()) {
			invoice.setDocument(odc.get());
		}
		
		Optional<Quotation> oqot = quotationRepository.findById(Long.parseLong(obj.get("quotationId").asText()));
		if (oqot.isPresent()) {
			invoice.setQuotation(oqot.get());
		}
		
		if (obj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(obj.get("invoiceNo").asText());
		}
		
		if (obj.get("amount") != null) {
			invoice.setAmount(obj.get("amount").asInt());
		}
		if (obj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(obj.get("modeOfPayment").asText());
		}
		if (obj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(obj.get("txnRefNo").asText());
		}
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		if (obj.get("issuerBank") != null) {
			invoice.setIssuerBank(obj.get("issuerBank").asText());
		}
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		if (obj.get("notes") != null) {
			invoice.setNotes(obj.get("notes").asText());
		}

		invoice.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		Instant now = Instant.now();
		invoice.setCreatedOn(now);
		invoice.setUpdatedOn(now);
		invoice = invoiceRepository.save(invoice);
		logger.info("Invoice added successfully");

		if (invoice.getId() != null) {
			InvoiceActivity invoiceActivity = new InvoiceActivity();
			BeanUtils.copyProperties(invoice, invoiceActivity);
			invoiceActivity.setInvoice(invoice);
			invoiceActivity = invoiceActivityRepository.save(invoiceActivity);
			logger.info("Invoice activity added successfully");
		}
		return invoice;
	}
	
	public Invoice updateinvoice(ObjectNode obj) throws JSONException, URISyntaxException {
		Invoice invoice = new Invoice();
		
		Optional<Invoice> ur = invoiceRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Invoice not found");
			return null;
		}

		if (obj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(obj.get("invoiceNo").asText());
		}
		
		if (obj.get("amount") != null) {
			invoice.setAmount(obj.get("amount").asInt());
		}
		
		if (obj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(obj.get("modeOfPayment").asText());
		}
		
		if (obj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(obj.get("txnRefNo").asText());
		}
		
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		
		if (obj.get("issuerBank") != null) {
			invoice.setIssuerBank(obj.get("issuerBank").asText());
		}
		
		if (obj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(obj.get("chequeOrDdNo").asText());
		}
		
		if (obj.get("notes") != null) {
			invoice.setNotes(obj.get("notes").asText());
		}

		if (obj.get("user") != null) {
			invoice.setUpdatedBy(obj.get("user").asText());
		} else {
			invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		invoice.setUpdatedOn(now);
		invoice = invoiceRepository.save(invoice);
		logger.info("Updating invoice completed"+invoice.toString());
		return invoice;

	}
	
	public List<Invoice> searchinvoice(Map<String, String> requestObj) {
		logger.info("Request to get invoice on given filter criteria");
		Invoice invoice = new Invoice();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			invoice.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("amount") != null) {
			invoice.setAmount(Integer.valueOf(requestObj.get("amount")));
			isFilter = true;
		}
		if (requestObj.get("invoiceNo") != null) {
			invoice.setInvoiceNo(requestObj.get("invoiceNo"));
			isFilter = true;
		}
		if (requestObj.get("modeOfPayment") != null) {
			invoice.setModeOfPayment(requestObj.get("modeOfPayment"));
			isFilter = true;
		}
		if (requestObj.get("txnRefNo") != null) {
			invoice.setTxnRefNo(requestObj.get("txnRefNo"));
			isFilter = true;
		}
		if (requestObj.get("chequeOrDdNo") != null) {
			invoice.setChequeOrDdNo(requestObj.get("chequeOrDdNo"));
			isFilter = true;
		}
		if (requestObj.get("issuerBank") != null) {
			invoice.setIssuerBank(requestObj.get("issuerBank"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			invoice.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<Invoice> list = null;
		if (isFilter) {
			list = this.invoiceRepository.findAll(Example.of(invoice), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.invoiceRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
		logger.info("Invoice list: "+invoice);
		return list;
		
	}
		
}


