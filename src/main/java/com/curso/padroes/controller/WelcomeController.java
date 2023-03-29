package com.curso.padroes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/")
    public String welcome() {
        return "<!DOCTYPE html>" +
                "<html>"+
                "<head>"+
                "<meta charset=\"UTF-8\">"+
                "<title>REST API PADROES</title>"+
                "<style>"+
                "body {"+
                "font-family: 'Roboto', sans-serif;"+
                "text-align: center;"+
                "text-color: #0066ff;"+
                "}"+
                "a {text-decoration: none; color: #336600; font-weight: bold; font-size: 14px;}"+
                "</style>"+
                "<link href=\"https://fonts.googleapis.com/css?family=Roboto&display=swap\" rel=\"stylesheet\">"+
                "</head>"+
                "<body>"+
                "<div>"+
                "<p style=\"margin-top:40px;\"><h2>REST API - Desafio Padrões</h2></p>" +
                "</div>"+
                "<hr>"+
                "<p><a href=\"/sandbox\">TESTAR API</a> ▶</p>" +
                "</body>"+
                "</html>";
    }
}