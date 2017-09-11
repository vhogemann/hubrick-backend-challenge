package com.hubrick.solution;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;

public class EmplyeeReportTest {

    String[] employees = new String[]{
            "1,Opal Ballard,f,4350.00",
            "1,Otis Bell,m,2650.50",
            "2,Francis Hayes,m,2490.00",
            "2,June Holland,f,2670.00",
            "2,Ethel Vega,f,4440.00"
    };

    String[] departments = new String[]{
            "Human Resources",
            "Finance"
    };

    String[] ages = new String[]{
            "Opal Ballard,23",
            "Otis Bell,35",
            "Francis Hayes,30",
            "June Holland,49",
            "Ethel Vega,21"
    };

    @Test
    public void shouldCalculateMedian() {
        String[] expected = new String[]{
                "Finance,3500.25",
                "Human Resources,2670.0"
        };

        String[] actual = EmployeeReport.medianIncomeByDepatrment(Arrays.stream(employees), Arrays.stream(departments)).sorted().toArray(String[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldCalculate95Percentile() {
        String[] actual = EmployeeReport.percentileIncomeByDepartment(Arrays.stream(employees), Arrays.stream(departments), 0.5).sorted().toArray(String[]::new);

        String[] expected = new String[]{
                "Finance,4350.0",
                "Human Resources,2670.0"
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldCalculateAverageIncomeByAge() {
        String[] expected = new String[]{
                "20,4395.0",
                "30,2570.25",
                "40,2670.0"
        };

        String[] actual = EmployeeReport.averageIncomeByAge(Arrays.stream(employees), Arrays.stream(ages)).sorted().toArray(String[]::new);

        assertArrayEquals(actual, expected);
    }

    @Test
    public void shouldCalculateMedianAgeByDepartment() {
        String[] expected = new String[] {
                "Finance,29",
                "Human Resources,30"
        };

        String[] actual = EmployeeReport.medianAgeByDepartment(Arrays.stream(employees), Arrays.stream(departments), Arrays.stream(ages)).sorted().toArray(String[]::new);

        assertArrayEquals(expected, actual);
    }

}
