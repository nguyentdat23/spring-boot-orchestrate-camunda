package com.farukgenc.boilerplate.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farukgenc.boilerplate.springboot.service.TicketService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TicketController {

  @Autowired
  private final TicketService ticketService;

  @GetMapping("/approve")
  public ResponseEntity<String> approve(@RequestParam String assignee) {
    ticketService.getTaskList(assignee);

    return ResponseEntity.ok("System recorded you approve command");
  }

  @PostMapping("/submit")
  public ResponseEntity<Boolean> submit() {
    ticketService.sumit("ABC_123");

    return ResponseEntity.ok(true);
  }
}
