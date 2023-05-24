package com.example.springboottesting.service;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
