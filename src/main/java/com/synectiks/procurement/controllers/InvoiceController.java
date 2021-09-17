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
import com.synectiks.procurement.business.service.InvoiceService;
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class InvoiceController {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceService invoiceService;

	@PostMapping("/addInvoice")
	public ResponseEntity<Status> addInvoice(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to add a new invoice");
		Status st = new Status();
		try {
			Invoice invoice = invoiceService.addInvoice(obj);
			if (invoice == null) {
				logger.error("Add invoice failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add invoice failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Invoice add successful");
			st.setObject(invoice);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Invoice add failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Invoice add failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateInvoice")
	public ResponseEntity<Status> updateinvoice(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
		logger.info("Request to update an invoice");
		Status st = new Status();
		try {
			Invoice invoice = invoiceService.updateinvoice(obj);
			if (invoice == null) {
				logger.error("Update invoice failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update invoice failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Updating invoice successful");
			st.setObject(invoice);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Updating invoice failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating invoice failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchInvoice")
	public ResponseEntity<Status> searchinvoice(@RequestParam Map<String, String> requestObj) {
		Status st = new Status();
		logger.info("Request to get list of invoices on given filter criteria");
		try {
			List<Invoice> list = invoiceService.searchinvoice(requestObj);
			if (list == null) {
				logger.error("Search invoice failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search invoice failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search of invoice list successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search of invoice list failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search of invoice list failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}

	@DeleteMapping("/deleteInvoice/{id}")
	public ResponseEntity<Status> deleteInvoice(@PathVariable Long id) {
		Status st = new Status();
		try {
			invoiceService.deleteInvoice(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Delete invoice successful");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable e) {
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Delete invoice failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/getInvoice/{id}")
	public ResponseEntity<Status> getInvoice(@PathVariable Long id) {
		logger.info("Getting invoice by id: " + id);
		Status st = new Status();
		try {
			Invoice invoice = invoiceService.getInvoice(id);
			if (invoice == null) {
				logger.warn("Invoice not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Invoice not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Invoice found");
			st.setObject(invoice);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Invoice not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Invoice not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/approveInvoice")
	public ResponseEntity<Status> approveInvoice(@RequestBody ObjectNode obj) throws JSONException {
		logger.info("Request to approve a invoice");

		Status st = new Status();
		try {
//			boolean updateFlag = invoiceService.approveInvoice(obj);
			if (updateFlag) {
				st.setCode(HttpStatus.OK.value());
				st.setType("SUCCESS");
				st.setMessage("Invoice  approved successfully");
			} else {
				st.setCode(HttpStatus.OK.value());
				st.setType("Fialed");
				st.setMessage("Invoice could not be updated");
			}
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Approve invoice failed");
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Approve invoice failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

}
