package com.example.springboottesting.repository;

import com.example.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
// we add this annotation to test the persistence layer (repository) components that will autoconfigure
// in-memory embedded database for testing purposes
public class EmployeeRepositoryTests {

    private static final String UPDATED_NAME = "updated-name";
    private static final String UPDATED_EMAIL = "updated-email";

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("test-firstName")
                .lastName("test-lastName")
                .email("test-email")
                .build();

    }

    // test for save operation
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup

        //when  - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    // test for get all employees
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        //given - precondition or setup

        Employee employee2 = Employee.builder()
                .firstName("test2-firstName")
                .lastName("test2-lastName")
                .email("test2-email")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or the behaviour that we are going to test

        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // test for get employee by id
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    // test for findByEmail
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    // test for update
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenUpdatedEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeSaved = employeeRepository.findById(employee.getId()).get();
        employeeSaved.setFirstName(UPDATED_NAME);
        employeeSaved.setEmail(UPDATED_EMAIL);
        Employee updatedEmployee = employeeRepository.save(employeeSaved);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(updatedEmployee.getFirstName()).isEqualTo(UPDATED_NAME);

    }

    // test for delete
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    // test for custom query using JPQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    // test for custom query using JPQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }
}

