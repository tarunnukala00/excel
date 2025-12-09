package com.example.excel.controller;



import com.example.excel.dto.SalaryFilterRequest;
import com.example.excel.service.ExcelService;
import com.example.excel.service.SalaryFilterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import com.example.excel.model.Employee;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private SalaryFilterService filterService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * POST form-data:
     * - file -> (File) Excel .xlsx
     * - request -> (Text) JSON string for SalaryFilterRequest
     */
    @PostMapping("/filter")
    public ResponseEntity<?> filterSalary(
            @RequestParam("file") MultipartFile file,
            @RequestParam("request") String requestJson) {

        try {
            SalaryFilterRequest request = objectMapper.readValue(requestJson, SalaryFilterRequest.class);

            List<Employee> employees = excelService.readExcel(file);

            Map<String, Object> result = filterService.filterAndCalculate(request, employees);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // return message for client debugging
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}

