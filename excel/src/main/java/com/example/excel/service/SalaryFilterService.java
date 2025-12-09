package com.example.excel.service;



import com.example.excel.dto.SalaryFilterRequest;
import com.example.excel.model.Employee;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SalaryFilterService {

    private final SimpleDateFormat fmt = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);

    public Map<String, Object> filterAndCalculate(SalaryFilterRequest req, List<Employee> employees) throws Exception {
        if (req.getEmployeeName() == null || req.getFromMonth() == null || req.getToMonth() == null) {
            throw new IllegalArgumentException("employeeName, fromMonth and toMonth are required");
        }

        Employee selected = employees.stream()
                .filter(e -> e.getName() != null && e.getName().equalsIgnoreCase(req.getEmployeeName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found: " + req.getEmployeeName()));

        Map<String, Double> monthly = selected.getMonthlySalaries();
        Date from = parseMonth(req.getFromMonth());
        Date to = parseMonth(req.getToMonth());

        if (from.after(to)) {
            throw new IllegalArgumentException("fromMonth must be before or equal to toMonth");
        }

        double totalAmount = 0;
        Map<String, Double> filteredMonths = new LinkedHashMap<>();

        for (String monthKey : monthly.keySet()) {
            Date cur;
            try {
                cur = parseMonth(monthKey);
            } catch (ParseException e) {
                // skip months that don't match expected format
                continue;
            }

            if (!cur.before(from) && !cur.after(to)) {
                double value = monthly.getOrDefault(monthKey, 0.0);

                if (req.getMinSalary() != null && value < req.getMinSalary()) continue;
                if (req.getMaxSalary() != null && value > req.getMaxSalary()) continue;

                filteredMonths.put(monthKey, value);
                totalAmount += value;
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("employee", selected.getName());
        response.put("age", selected.getAge());
        response.put("phone", selected.getPhone());
        response.put("months", filteredMonths);
        response.put("totalAmount", totalAmount);

        return response;
    }

    private Date parseMonth(String m) throws ParseException {
        return fmt.parse(m);
    }
}

