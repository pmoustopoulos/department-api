package com.ainigma100.departmentapi.service;

import java.util.List;
import java.util.Optional;

import com.ainigma100.departmentapi.model.request.EmployeeRequestModel;
import com.ainigma100.departmentapi.model.response.EmployeeResponseModel;
import com.ainigma100.departmentapi.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.exception.RecordAlreadyExistException;
import com.ainigma100.departmentapi.exception.RecordNotFoundException;
import com.ainigma100.departmentapi.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;


	public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {

		List<Employee> listOfEmployeeFromDB = employeeRepository.findAll();

		if( !listOfEmployeeFromDB.isEmpty() ) {

			List<EmployeeResponseModel> returnedList = Utils.mapList(listOfEmployeeFromDB, EmployeeResponseModel.class);

			return new ResponseEntity<>(returnedList, HttpStatus.OK);
		}

		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}


	public ResponseEntity<EmployeeResponseModel> getEmployeeById(Integer employeeId) {

		Optional<Employee> employeeFromDb = employeeRepository.findById(employeeId);

		if (employeeFromDb.isPresent()) {

			// because employeeFromDb is Optional, I have to use the get() method to retrieve the actual record
			EmployeeResponseModel returnedValue = Utils.map(employeeFromDb.get(), EmployeeResponseModel.class);

			return new ResponseEntity<>(returnedValue, HttpStatus.OK);
		}

		throw new RecordNotFoundException("Employee with id '" + employeeId + "' was not found");
	}



	public ResponseEntity<EmployeeResponseModel> createEmployee(EmployeeRequestModel employeeRequestModel) {

//		Optional<Employee> employeeFromDb = employeeRepository.;
//
//		if (employeeFromDb.isPresent()) {
//			throw new RecordAlreadyExistException("Employee with id " + employee.getEmpId() + " already exists");
//		}

		Employee employeeTobeSaved = Utils.map(employeeRequestModel, Employee.class);

		Employee savedEmployee = employeeRepository.save(employeeTobeSaved);

		EmployeeResponseModel returnedValue = Utils.map(savedEmployee, EmployeeResponseModel.class);

		return new ResponseEntity<>(returnedValue, HttpStatus.CREATED);
	}



	public ResponseEntity<String> deleteEmployeeById(Integer employeeId) {

		Optional<Employee> employeeFromDb = employeeRepository.findById(employeeId);

		if (employeeFromDb.isPresent()) {

			employeeRepository.deleteById(employeeId);
			String deletionMessage = "Employee with id '" + employeeId + "' has been deleted";

			return new ResponseEntity<>(deletionMessage, HttpStatus.OK);
		}

		throw new RecordNotFoundException("Employee with id '" + employeeId + "' was not found");
	}



}
