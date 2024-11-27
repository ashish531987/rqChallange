package com.reliaquest.api.controller;

import com.reliaquest.api.model.BaseResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeRequestDTO;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController()
public class EmployeeControllerImpl implements IEmployeeController<Employee, EmployeeRequestDTO>{
    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        BaseResponse<List<Employee>> responseDTO =  employeeService.getEmployees().block();
        return ResponseEntity.ok(Objects.requireNonNull(responseDTO).getData());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        BaseResponse<Employee> responseDTO = employeeService.getEmployeeById(id).block();
        return ResponseEntity.ok(Objects.requireNonNull(responseDTO).getData());
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployee());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        BaseResponse<Employee> responseDTO = employeeService.createEmployee(employeeRequestDTO).block();
        return ResponseEntity.ok(Objects.requireNonNull(responseDTO).getData());
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }
}
