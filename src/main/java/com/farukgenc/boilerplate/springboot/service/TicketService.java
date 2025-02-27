package com.farukgenc.boilerplate.springboot.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.farukgenc.boilerplate.springboot.model.Task;
import com.farukgenc.boilerplate.springboot.model.Ticket;
import com.farukgenc.boilerplate.springboot.repository.TicketRepository;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.CompleteJobCommandStep1;
import io.camunda.zeebe.client.api.response.ActivatedJob;

@Service
public class TicketService {
  private final ZeebeClient zeebeClient;
  private final TicketRepository ticketRepository;
  private final TaskService taskService;

  public TicketService(ZeebeClient zeebeClient,
      TicketRepository ticketRepository,
      TaskService taskService) {
    this.zeebeClient = zeebeClient;
    this.ticketRepository = ticketRepository;
    this.taskService = taskService;
  }

  public void sumit(String ticketNo) {
    Ticket ticket = new Ticket();
    ticket.setTicketNo(ticketNo);
    ticket.setTitle("Proposal ");

    try {
      zeebeClient.newCreateInstanceCommand()
          .bpmnProcessId("PaymentApprovalProcess")
          .latestVersion()
          .variables(ticket)
          .send()
          .join();

      ticketRepository.save(ticket);
    } catch (Exception e) {
      throw e;
    }
  }

  public List<Task> getTaskList(String asignee, Long processInstanceKey) {
    var tasks = taskService.getPendingTasks(asignee);
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("approved", true);
    variables.put("comment", "OK");

    Task currentTask = tasks.stream().filter(f -> f.processInstanceKey.equals(processInstanceKey)).findFirst()
        .orElseThrow();

    taskService.complete(currentTask.id);
    return tasks;
  }
}
