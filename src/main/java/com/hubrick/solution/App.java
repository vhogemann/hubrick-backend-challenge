package com.hubrick.solution;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("first parameter must be path to a directory");
            System.exit(1);
        }

        try {

            incomeByDepartment(args[0]);

            income95ByDepartment(args[0]);

            incomeAverageByDateRange(args[0]);

            employeeAgeByDepartment(args[0]);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

    }

    static void incomeByDepartment(String path) throws IOException {

        Stream<String> departments = Files.lines(Paths.get(path, "departments.csv"));
        Stream<String> employees = Files.lines(Paths.get(path, "employees.csv"));

        Stream<String> headers = Arrays.stream(new String[]{"department,income"});
        Stream<String> data = Stream.concat(headers, EmployeeReport.medianIncomeByDepatrment(employees, departments));

        writeReport(path, "income-by-department.csv", data);
    }

    static void income95ByDepartment(String path) throws IOException {

        Stream<String> departments = Files.lines(Paths.get(path, "departments.csv"));
        Stream<String> employees = Files.lines(Paths.get(path, "employees.csv"));

        Stream<String> headers = Arrays.stream(new String[]{"department,95percentile"});
        Stream<String> data = Stream.concat(headers, EmployeeReport.percentileIncomeByDepartment(employees, departments, 0.95));

        writeReport(path, "income-95-by-department.csv", data);
    }

    static void incomeAverageByDateRange(String path) throws IOException {

        Stream<String> employees = Files.lines(Paths.get(path, "employees.csv"));
        Stream<String> ages = Files.lines(Paths.get(path, "ages.csv"));

        Stream<String> headers = Arrays.stream(new String[]{"age,income"});
        Stream<String> data = Stream.concat(headers, EmployeeReport.averageIncomeByAge(employees, ages));

        writeReport(path, "income-average-by-age-range.csv", data);
    }

    static void employeeAgeByDepartment(String path) throws IOException {
        Stream<String> departments = Files.lines(Paths.get(path, "departments.csv"));
        Stream<String> employees = Files.lines(Paths.get(path, "employees.csv"));
        Stream<String> ages = Files.lines(Paths.get(path, "ages.csv"));

        Stream<String> headers = Arrays.stream(new String[]{"department,age"});
        Stream<String> data = Stream.concat(headers, EmployeeReport.medianAgeByDepartment(employees, departments, ages));

        writeReport(path, "employee-age-by-department.csv", data);
    }

    static void writeReport(String path, String filename, Stream<String> data) throws IOException {
        PrintWriter pw = new PrintWriter(Paths.get(path, filename).toString(), "UTF-8");
        data.forEachOrdered(pw::println);
        pw.close();
    }

}
