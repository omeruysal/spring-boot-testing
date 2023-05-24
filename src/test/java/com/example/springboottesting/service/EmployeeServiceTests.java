package com.example.springboottesting.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // to be able to use mockito annotations
public class EmployeeServiceTests {

    private static final String UPDATED_NAME = "updated-name";
    private static final String UPDATED_EMAIL = "updated-email";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("test-name")
                .lastName("test-lastName")
                .email("test-mail")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given - precondition or setup

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when -  action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given - precondition or setup

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then
        assertThat(savedEmployee).isNotNull();

    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail(UPDATED_EMAIL);
        employee.setFirstName(UPDATED_NAME);
        // when -  action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(updatedEmployee.getFirstName()).isEqualTo(UPDATED_NAME);
    }

    @Test

    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given - precondition or setup
        long employeeId = 1L;

        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when -  action or the behaviour that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
