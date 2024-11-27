package com.reliaquest.api;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApiApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getEmployeesSuccess() throws Exception {
        ResponseEntity<Object[]> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/",Object[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertThat(Objects.equals(responseEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON));
    }

    @Test
    void getEmployeesByNameSearchSuccess() throws Exception {
        String searchString = "Christopher";
        ResponseEntity<Object[]> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/search/"+searchString,Object[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertThat(Objects.equals(responseEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON));
    }

    @Test
    void createGetEmployeeByIdSuccess() throws Exception {
        // Create New Employee
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setName("Dr. Lionel Messi");
        employeeRequestDTO.setSalary(60000);
        employeeRequestDTO.setAge(37);
        employeeRequestDTO.setTitle("Sr. Director Of Sales");

        ResponseEntity<Employee> employee = this.restTemplate.postForEntity("http://localhost:" + port + "/",employeeRequestDTO,  Employee.class);
        assertNotNull(employee.getBody());
        assertThat(employee.getBody().getName().equals(employeeRequestDTO.getName()));

        String searchString = String.valueOf(employee.getBody().getId());
        ResponseEntity<Employee> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/"+searchString,Employee.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertThat(Objects.equals(responseEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON));
    }
}
