package com.farukgenc.boilerplate.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farukgenc.boilerplate.springboot.model.Task;
import com.farukgenc.boilerplate.springboot.service.TicketService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TicketController {

  @Autowired
  private final TicketService ticketService;

  @GetMapping("/approve/{processInstanceKey}")
  public ResponseEntity<Map<String, Object>> approve(@RequestParam String asignee,
      @PathVariable Long processInstanceKey) {
    List<Task> taskList = ticketService.getTaskList(asignee, processInstanceKey);
    var result = new HashMap<String, Object>();
    result.put("data", taskList);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/submit")
  public ResponseEntity<Boolean> submit() {
    ticketService.sumit("ABC_123");

    return ResponseEntity.ok(true);
  }
}
