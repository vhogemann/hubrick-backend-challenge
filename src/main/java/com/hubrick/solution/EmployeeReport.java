package com.hubrick.solution;

import com.hubrick.solution.model.Employee;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeReport {


    static Stream<String> groupByDepartment(Stream<String> employeeStream, Stream<String> departmentStream, Function<Employee[], Double> reducer) {

        Map<Integer, List<Employee>> departmentMap = employeeStream
                .map(Employee::fromEmployeeList)
                .collect(Collectors.groupingBy(e -> e.departmentId));

        String[] departments = departmentStream.sorted().toArray(String[]::new);
        Map<String, Integer> deptIdx = new HashMap<>();

        for (int i = 0; i < departments.length; i++) {
            deptIdx.put(departments[i], i + 1);
        }

        return Arrays.stream(departments)
                .map(dept -> {

                    Employee[] employees = departmentMap.get(deptIdx.get(dept))
                            .parallelStream().sorted(Comparator.comparing(e -> e.salary)).toArray(Employee[]::new);

                    double result = reducer.apply(employees);
                    return dept + ',' + result;
                });
    }

    static Stream<String> medianIncomeByDepatrment(Stream<String> employeeStream, Stream<String> departmentStream) {
        return groupByDepartment(employeeStream, departmentStream, employees -> {
            int size = employees.length;
            if (size % 2 == 0) {
                int mid = Math.round((size / 2) - 1);
                return (employees[mid].salary + employees[mid + 1].salary) / 2;
            } else {
                int mid = Math.round(size / 2);
                return employees[mid].salary;
            }
        });

    }

    static Stream<String> percentileIncomeByDepartment(Stream<String> employeeStream, Stream<String> departmentStream, double factor) {
        if (factor < 0 || factor > 1) throw new IllegalArgumentException("factor should be between 0 and 1");
        return groupByDepartment(employeeStream, departmentStream, employees -> {

            int index = Math.toIntExact(Math.round((employees.length - 1) * factor));
            return employees[index].salary;

        });
    }

    static Stream<String> averageIncomeByAge(Stream<String> employeeStream, Stream<String> ageStream) {

        Map<String, List<Employee>> employeeMap = employeeStream
                .map(Employee::fromEmployeeList)
                .filter(e -> e != null)
                .collect(
                        Collectors.groupingBy(e -> e.name));

        return ageStream
                .map(line -> {
                    String[] values = line.split(",");

                    Employee employee = null;
                    String key = values[0].trim().toLowerCase();
                    if( employeeMap.get(key) != null){
                        employee = employeeMap.get(key).get(0);
                        if (employee != null) {
                            employee.age = Integer.parseInt(values[1]);
                        }
                    }

                    return employee;
                })
                .filter(e -> e != null && e.age != null)
                .collect(
                        Collectors.groupingBy(
                                e -> Math.round(e.age / 10) * 10,
                                Collectors.averagingDouble(e -> e.salary)
                        ))
                .entrySet().stream()
                .map(entry -> entry.getKey() + "," + entry.getValue());

    }

    static Stream<String> medianAgeByDepartment(Stream<String> employeeStream, Stream<String> departmentStream, Stream<String> ageStream) {

        Map<String, Integer> ageMap = ageStream
                .map(line -> line.split(","))
                .collect(Collectors.groupingBy(
                        value -> value[0].trim().toLowerCase(),
                        Collectors.summingInt(value -> Integer.parseInt(value[1]))));

        Map<Integer, List<Employee>> employeeMap = employeeStream
                .map(Employee::fromEmployeeList)
                .peek(e -> e.age = ageMap.get(e.name))
                .collect(Collectors.groupingBy(e -> e.departmentId));

        String[] departments = departmentStream.sorted().toArray(String[]::new);
        Map<String, Integer> deptIdx = new HashMap<>();

        for (int i = 0; i < departments.length; i++) {
            deptIdx.put(departments[i], i + 1);
        }

        return Arrays.stream(departments)
                .map( dept -> {
                    Employee[] employees = employeeMap.get(deptIdx.get(dept))
                            .parallelStream()
                            .filter(e -> e != null && e.age != null)
                            .sorted(Comparator.comparing(e -> e.age)).toArray(Employee[]::new);

                    int size = employees.length;
                    int mid;
                    int median;
                    if (size % 2 == 0) {
                        mid = Math.round((size / 2) - 1);
                        median = Math.round((employees[mid].age + employees[mid + 1].age) / 2);
                    } else {
                        mid = Math.round(size / 2);
                        median = employees[mid].age;
                    }

                    return dept + "," + median;

                })
                .filter( l -> l != null);

    }

}
