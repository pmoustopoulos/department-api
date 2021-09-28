package com.ainigma100.departmentapi.service;

import java.util.List;
import java.util.Optional;

import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.model.request.DepartmentRequestModel;
import com.ainigma100.departmentapi.model.response.DepartmentResponseModel;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.ainigma100.departmentapi.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.exception.RecordAlreadyExistException;
import com.ainigma100.departmentapi.exception.RecordNotFoundException;
import com.ainigma100.departmentapi.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	private final EmployeeRepository employeeRepository;

	
	
	public ResponseEntity<List<DepartmentResponseModel>> getAllDepartments() {
		
		List<Department> listOfDepartmentFromDB = departmentRepository.findAll();
		
		if(!listOfDepartmentFromDB.isEmpty()) {

			List<DepartmentResponseModel> returnedList = Utils.mapList(listOfDepartmentFromDB, DepartmentResponseModel.class);

			return new ResponseEntity<>(returnedList, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}



	public ResponseEntity<DepartmentResponseModel> getDepartmentById(Integer departmentId) {
		
		Optional<Department> departmentFromDb = departmentRepository.findById(departmentId);
		
		if (departmentFromDb.isPresent()) {

			// because departmentFromDb is Optional, I have to use the get() method to retrieve the actual record
			DepartmentResponseModel returnedValue = Utils.map(departmentFromDb.get(), DepartmentResponseModel.class);

			return new ResponseEntity<>(returnedValue, HttpStatus.OK);
		}
		
		throw new RecordNotFoundException("Department with id " + departmentId + " was not found");
	}



	public ResponseEntity<DepartmentResponseModel> createDepartment(DepartmentRequestModel departmentRequestModel) {
		
		Department departmentFromDb = departmentRepository.findByDepNameIgnoreCase(departmentRequestModel.getDepName());

		if (departmentFromDb != null) {
			throw new RecordAlreadyExistException("Department with name " + departmentRequestModel.getDepName() + " already exists");
		}

		Department departmentToBeSaved = Utils.map(departmentRequestModel, Department.class);

		Department savedDepartment = departmentRepository.save(departmentToBeSaved);

		DepartmentResponseModel returnedValue = Utils.map(savedDepartment, DepartmentResponseModel.class);
		
		return new ResponseEntity<>(returnedValue, HttpStatus.CREATED);
	}



	public ResponseEntity<String> deleteDepartmentById(Integer departmentId) {
		
		Optional<Department> departmentFromDb = departmentRepository.findById(departmentId);
		
		if (departmentFromDb.isPresent()) {
			
			departmentRepository.deleteById(departmentId);
			// you can also use delete by using the actual object retrieved from the database
//			departmentRepository.delete(departmentFromDb.get());
			
			String deletionMessage = "Department with id " + departmentId + " has been deleted";
			
			return new ResponseEntity<>(deletionMessage, HttpStatus.OK);
		}
		
		throw new RecordNotFoundException("Department with id " + departmentId + " was not found");
	}


    public ResponseEntity<DepartmentResponseModel> addEmployeeToDepartment(Integer departmentId, Integer employeeId) {

		Department departmentFromDB = departmentRepository.findById(departmentId)
				.orElseThrow(()-> new RecordNotFoundException("Department with id '" + departmentId + "' was not found"));

		Employee employeeFromDB = employeeRepository.findById(employeeId)
				.orElseThrow(()-> new RecordNotFoundException("Employee with id '" + employeeId + "' was not found"));

		departmentFromDB.addEmployee(employeeFromDB);

		Department updatedDepartment = departmentRepository.save(departmentFromDB);

		DepartmentResponseModel returnedValue = Utils.map(updatedDepartment, DepartmentResponseModel.class);

		return new ResponseEntity<>(returnedValue, HttpStatus.CREATED);
    }
}
