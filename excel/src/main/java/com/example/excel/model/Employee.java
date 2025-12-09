package com.example.excel.model;



import java.util.LinkedHashMap;
import java.util.Map;

public class Employee {
    private int serialNumber;
    private String name;
    private Integer age;
    private String phone;
    private Double baseSalary;
    private Double monthlyPf;
    private Double monthlyInsurance;
    private String recordDate;

    // Keep month order, so use LinkedHashMap month -> salary
    private Map<String, Double> monthlySalaries = new LinkedHashMap<>();

    // getters & setters
    public int getSerialNumber() { return serialNumber; }
    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public Double getMonthlyPf() { return monthlyPf; }
    public void setMonthlyPf(Double monthlyPf) { this.monthlyPf = monthlyPf; }

    public Double getMonthlyInsurance() { return monthlyInsurance; }
    public void setMonthlyInsurance(Double monthlyInsurance) { this.monthlyInsurance = monthlyInsurance; }

    public String getRecordDate() { return recordDate; }
    public void setRecordDate(String recordDate) { this.recordDate = recordDate; }

    public Map<String, Double> getMonthlySalaries() { return monthlySalaries; }
    public void setMonthlySalaries(Map<String, Double> monthlySalaries) { this.monthlySalaries = monthlySalaries; }
}

