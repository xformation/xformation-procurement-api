package com.synectiks.procurement.business.service;

import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.repository.DepartmentRepository;

@Service
public class DepartmentService {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	public Department getDepartment(Long id) {
		logger.info("Getting department by id: "+id);
		Optional<Department> o = departmentRepository.findById(id);
		if(o.isPresent()) {
			logger.info("Department: "+o.get().toString());
			return o.get();
		}
		logger.warn("Department not found");
		return null;
	}
	
	public Department addDepartment(ObjectNode obj) throws JSONException {
		Department department = new Department();
		if (obj.get("name") != null) {
			department.setName(obj.get("name").asText());
		}
		department = departmentRepository.save(department);
		logger.info("Department added successfully. "+department.toString());
		return department;
	}
	
	public Department updateDepartment(ObjectNode obj) throws JSONException, URISyntaxException {
		Optional<Department> ur = departmentRepository.findById(Long.parseLong(obj.get("id").asText()));
		if(!ur.isPresent()) {
			return null;
		}
		Department department = new Department();
		department.setName(obj.get("name").asText());
		department = departmentRepository.save(department);
		logger.info("Updating department completed");
		return department;
	}
}
