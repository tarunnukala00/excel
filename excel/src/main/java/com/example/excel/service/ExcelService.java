package com.example.excel.service;
import com.example.excel.model.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    // We assume fixed columns:
    // 0: S.No
    // 1: Name
    // 2: Age
    // 3: Phone Number
    // 4: Base Salary
    // 5: Monthly PF (12%)
    // 6: Monthly Insurance
    // 7: Record Date
    // 8... : Month columns like "Dec-2023", "Jan-2024", ... (24 columns)

    public List<Employee> readExcel(MultipartFile file) throws Exception {
        List<Employee> employees = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return employees;

            Row header = sheet.getRow(0);
            if (header == null) throw new RuntimeException("Header row is missing in Excel");

            int totalColumns = header.getLastCellNum();
            List<String> monthColumns = new ArrayList<>();
            for (int i = 8; i < totalColumns; i++) {
                Cell c = header.getCell(i);
                if (c != null) {
                    monthColumns.add(getCellAsString(c));
                }
            }

            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Employee emp = new Employee();

                emp.setSerialNumber((int) getNumericCellValue(row.getCell(0)));
                emp.setName(getCellAsString(row.getCell(1)));
                emp.setAge((int) getNumericCellValue(row.getCell(2)));
                emp.setPhone(getCellAsString(row.getCell(3)));
                emp.setBaseSalary(getNumericCellValue(row.getCell(4)));
                emp.setMonthlyPf(getNumericCellValue(row.getCell(5)));
                emp.setMonthlyInsurance(getNumericCellValue(row.getCell(6)));
                emp.setRecordDate(getCellAsString(row.getCell(7)));

                Map<String, Double> monthly = new LinkedHashMap<>();
                for (int i = 0; i < monthColumns.size(); i++) {
                    Cell c = row.getCell(8 + i);
                    double val = getNumericCellValue(c);
                    monthly.put(monthColumns.get(i), val);
                }
                emp.setMonthlySalaries(monthly);
                employees.add(emp);
            }
        }

        return employees;
    }

    // helper to get numeric value (returns 0 for nulls)
    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0;
        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
            if (cell.getCellType() == CellType.STRING) {
                String s = cell.getStringCellValue().trim();
                if (s.isEmpty()) return 0;
                return Double.parseDouble(s);
            }
            if (cell.getCellType() == CellType.FORMULA) return cell.getNumericCellValue();
            if (cell.getCellType() == CellType.BOOLEAN) return cell.getBooleanCellValue() ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    // helper to get string representation
    private String getCellAsString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            } else {
                // remove .0 if integer
                double d = cell.getNumericCellValue();
                if (d == (long) d) return String.valueOf((long) d);
                return String.valueOf(d);
            }
        }
        if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
        if (cell.getCellType() == CellType.FORMULA) {
            try {
                return String.valueOf(cell.getNumericCellValue());
            } catch (Exception e) {
                return cell.getCellFormula();
            }
        }
        return "";
    }
}
