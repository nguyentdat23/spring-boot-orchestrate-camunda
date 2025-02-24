package com.farukgenc.boilerplate.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farukgenc.boilerplate.springboot.security.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
  private final TicketService ticketService;

  @GetMapping("/approve")
  public ResponseEntity<String> approve() {
    ticketService.approve("ABC_123", "yes");

    return ResponseEntity.ok("System recorded you approve command");
  }

  @PostMapping("/submit")
  public ResponseEntity<Boolean> submit() {
    ticketService.sumit("ABC_123");

    return ResponseEntity.ok(true);
  }
}
