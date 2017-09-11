package com.hubrick.solution.model;

public class Employee {

    public String name;

    public String sex;

    public int departmentId;

    public double salary;

    public Integer age;

    /**
     * 6,Opal Ballard,f,4350.00
     */
    public static Employee fromEmployeeList(String line) {
        if (line == null || line.trim().length() == 0) return null;
        String[] values = line.split(",");
        Employee employee = new Employee();
        employee.name = values[1].trim().toLowerCase();
        employee.sex = values[2];
        try {
            employee.departmentId = Integer.valueOf(values[0]);
            employee.salary = Double.valueOf(values[3]);
        } catch (Exception ex ) {
            System.out.println(line);
        }
        return employee;
    }

}
