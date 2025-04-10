package com.java.assetmanagement.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmployeeTest {

    @Test
    public void testDefaultConstructorSettersGetters() {
        Employee employee = new Employee();

        employee.setEmployeeId(1);
        employee.setName("John Doe");
        employee.setDepartment("IT");
        employee.setEmail("john.doe@example.com");
        employee.setPassword("password123");

        assertEquals(1, employee.getEmployeeId());
        assertEquals("John Doe", employee.getName());
        assertEquals("IT", employee.getDepartment());
        assertEquals("john.doe@example.com", employee.getEmail());
        assertEquals("password123", employee.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        Employee employee = new Employee(2, "Jane Smith", "HR", "jane.smith@example.com", "securePass");

        assertEquals(2, employee.getEmployeeId());
        assertEquals("Jane Smith", employee.getName());
        assertEquals("HR", employee.getDepartment());
        assertEquals("jane.smith@example.com", employee.getEmail());
        assertEquals("securePass", employee.getPassword());
    }

    @Test
    public void testToString() {
        Employee employee = new Employee(3, "Alice", "Finance", "alice@example.com", "alice123");
        String expected = "Employee [employeeId=3, name=Alice, department=Finance, email=alice@example.com, password=alice123]";
        assertEquals(expected, employee.toString());
    }
}
