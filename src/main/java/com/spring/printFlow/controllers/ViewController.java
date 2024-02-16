package com.spring.printFlow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
   @GetMapping("/")
   public String login() {
      return "login";
   }

   @GetMapping("/upload")
   public String upload() {
      return "upload";
   }

   @GetMapping("/dashboard/home")
   public String home() {
      return "dashboard";
   }

   @GetMapping("/dashboard/users")
   public String users() {
      return "users";
   }



}
