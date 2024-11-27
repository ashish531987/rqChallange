package com.reliaquest.api.service;

import com.reliaquest.api.controller.EmployeeNotFoundException;
import com.reliaquest.api.controller.TooMayRequestsException;
import com.reliaquest.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {
    private final WebClient webClient;

    @Autowired
    public EmployeeService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<EmployeeListResponseDTO> getEmployees() {
        log.debug("Getting All Employees...");
        return webClient.get()
                .uri("/employee")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new TooMayRequestsException()))
                .bodyToMono(EmployeeListResponseDTO.class);
    }

    public Mono<EmployeeResponseDTO> getEmployeeById(String id) {
        log.debug("Getting Employee by id...");
        return webClient.get()
                .uri("/employee/"+id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new EmployeeNotFoundException(id)))
                .bodyToMono(EmployeeResponseDTO.class);
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        log.debug("Getting Employee by name...");
        EmployeeListResponseDTO employeeList = getEmployees().block();
        return Objects.requireNonNull(employeeList).getData().stream().filter(e -> e.getName().contains(searchString)).collect(Collectors.toList());
    }

    public Integer getHighestSalaryOfEmployee() {
        log.debug("Getting Highest Salary...");
        EmployeeListResponseDTO employeeList = getEmployees().block();
        return Objects.requireNonNull(employeeList).getData().stream().map(Employee::getSalary).max(Integer::compare).orElse(-1);
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        log.debug("Getting Top 10 highest earnings employee names...");
        EmployeeListResponseDTO employeeList = getEmployees().block();
        return Objects.requireNonNull(employeeList).getData().stream().sorted((o1, o2) -> o2.getSalary().compareTo(o1.getSalary())).limit(10).map(Employee::getName).toList();
    }

    public Mono<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        log.debug("Creating new employee...");
        return webClient.post()
                .uri("/employee")
                .bodyValue(employeeRequestDTO)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new TooMayRequestsException()))
                .bodyToMono(EmployeeResponseDTO.class);
    }

    public String deleteEmployeeById(String id) {
        /*
         * Step 1: Get Employee with this id
         * Step 2: If Employee object found then use name to delete in the backend API.
         */

        log.debug("Deleting the employee with id...");
        EmployeeResponseDTO employeeResponseDTO = getEmployeeById(id).block();
        String name = Objects.requireNonNull(employeeResponseDTO).getData().getName();
        EmployeeDeleteRequestDTO employeeDeleteResponseDTO = new EmployeeDeleteRequestDTO(name);
        log.debug("Deleting the employee with the name...");
        EmployeeDeleteResponseDTO deleteResponseDTO = webClient.method(HttpMethod.DELETE)
                .uri("/employee")
                .bodyValue(employeeDeleteResponseDTO)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new EmployeeNotFoundException(id)))
                .bodyToMono(EmployeeDeleteResponseDTO.class).block();
        if(Objects.requireNonNull(deleteResponseDTO).getData()) {
            return name;
        } else {
            throw new EmployeeNotFoundException(id);
        }
    }
}