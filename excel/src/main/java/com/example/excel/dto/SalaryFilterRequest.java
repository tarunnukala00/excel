package com.example.excel.dto;



public class SalaryFilterRequest {
    private String employeeName;
    private String fromMonth;   // e.g., "Jan-2024"
    private String toMonth;     // e.g., "Dec-2024"
    private Double minSalary;   // optional
    private Double maxSalary;   // optional

    // getters & setters
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getFromMonth() { return fromMonth; }
    public void setFromMonth(String fromMonth) { this.fromMonth = fromMonth; }
    public String getToMonth() { return toMonth; }
    public void setToMonth(String toMonth) { this.toMonth = toMonth; }
    public Double getMinSalary() { return minSalary; }
    public void setMinSalary(Double minSalary) { this.minSalary = minSalary; }
    public Double getMaxSalary() { return maxSalary; }
    public void setMaxSalary(Double maxSalary) { this.maxSalary = maxSalary; }
}
