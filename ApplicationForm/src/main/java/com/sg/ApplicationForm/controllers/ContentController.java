/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.ApplicationForm.controllers;

/**
 *
 * @author board
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/tenantinfo")
    public String displayContentPage() {
        return "tenantinfo";
    }
}