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
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.domain.InvoiceActivity;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.domain.QuotationActivity;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.InvoiceActivityRepository;
import com.synectiks.procurement.repository.InvoiceRepository;
import com.synectiks.procurement.repository.QuotationRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class InvoiceController {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
	private static final String ENTITY_NAME = "invoice";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private InvoiceActivityRepository invoiceActivityRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private QuotationRepository quotationRepository;

	@PostMapping("/addInvoice")
	public  ResponseEntity<Object> addInvoice(@RequestBody ObjectNode obj) throws JSONException {
		Status st = new Status();
		//String documentId =(obj.get("documentId")).asText();
		 Optional<Document> ur = documentRepository.findById(Long.parseLong(obj.get("documentId").asText()));
		 if (!ur.isPresent()) {
			// String quotationId =(obj.get("quotationId")).asText();
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Department not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		 }
			 Optional<Quotation> cd = quotationRepository.findById(Long.parseLong(obj.get("quotationId").asText()));
			 if (!cd.isPresent()) {
				 st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("Quotation not found");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			 }
				 
					try {
						Invoice invoice = new Invoice();
						if (obj.get("departmentId") != null) {
							invoice.setDocument(ur.get());
						}
						if (obj.get("currencyId") != null) {
							invoice.setQuotation(cd.get());
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
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
						LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
						invoice.setDueDate(localDate);
						
						invoice.setCreatedBy(Constants.SYSTEM_ACCOUNT);
						invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
						Instant now = Instant.now();
						invoice.setCreatedOn(now);
						invoice.setUpdatedOn(now);
						
						invoice = invoiceRepository.save(invoice);
						
						logger.info("Add New invoice SUCCESS");
						
						if(invoice.getId() != null) {
							InvoiceActivity invoiceActivity = new InvoiceActivity();
								BeanUtils.copyProperties(invoice, invoiceActivity);	
								invoiceActivity.setInvoice(invoice);
								invoiceActivity = invoiceActivityRepository.save(invoiceActivity);
								logger.info("Add New invoice Activity");
						}
						st.setCode(HttpStatus.OK.value());
						st.setType("SUCCESS");
						st.setMessage("Add New invoice SUCCESS : "+invoice);
						st.setObject(invoice);
						return ResponseEntity.status(HttpStatus.OK).body(st);
						
						
						}catch (Exception e) {
							logger.error("Add New invoice failed. Exception: ", e);
							st.setCode(HttpStatus.EXPECTATION_FAILED.value());
							st.setType("ERROR");
							st.setMessage("Add New invoice failed");
							return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
						}
			 }
			 
	
	@PutMapping("/updateInvoice")
	public  ResponseEntity<Object> updateinvoice(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		Optional<Invoice> ur = invoiceRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateInvoice/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			Invoice invoice = new Invoice();
		
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
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			invoice.setDueDate(localDate);
		
		 if (obj.get("user") != null) {
			 invoice.setUpdatedBy(obj.get("user").asText());
			 } else {
				 invoice.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 invoice.setUpdatedOn(now);
		invoice = invoiceRepository.save(invoice);
		logger.info("Updating invoice completed");
		
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating invoice SUCCESS : "+invoice);
		st.setObject(invoice);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating invoice failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating invoice failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchInvoice")
	public List<Invoice> searchinvoice(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get invoice on given filter criteria");
		Invoice invoice = new Invoice();
		
		try {
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
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Invoice>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating invoice failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Invoice>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/invoice/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 invoiceRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}
}
