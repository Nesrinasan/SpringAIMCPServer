package com.nasan.springaimcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class CalcTools {

    @Tool(description = "İki tam sayıyı toplar")
    public int sum(int a, int b) {
        return a + b;
    }

    @Tool(description = "İsme selamlama döner")
    public String greet(String name) {
        return "Merhaba, " + name + "!";
    }
}
