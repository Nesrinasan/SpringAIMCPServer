package com.nasan.springaimcpserver.service;
import org.springframework.stereotype.Service;
import org.springframework.ai.tool.annotation.Tool;
import java.util.List;

@Service
public class EmployeeTools {

    @Tool(name = "employee.list", description = "Tüm çalışanları (ad, yaş) döndürür")
    public List<Employee> listEmployees() {
        return List.of(
                new Employee("Ali Yılmaz", 18),
                new Employee("Ayşe Demir", 22),
                new Employee("Mehmet Kaya", 18),
                new Employee("Zeynep Şahin", 30)
        );
    }
}