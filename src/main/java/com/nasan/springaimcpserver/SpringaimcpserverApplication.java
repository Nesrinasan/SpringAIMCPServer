package com.nasan.springaimcpserver;

import com.nasan.springaimcpserver.service.CalcTools;
import com.nasan.springaimcpserver.service.EmployeeTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringaimcpserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringaimcpserverApplication.class, args);
	}

	@Bean
	ToolCallbackProvider toolCallbacks(EmployeeTools tools, CalcTools calcTools) {
		return MethodToolCallbackProvider.builder().toolObjects(tools, calcTools).build();
	}
}
