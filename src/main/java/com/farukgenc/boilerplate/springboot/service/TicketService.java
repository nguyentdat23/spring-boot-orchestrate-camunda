package com.farukgenc.boilerplate.springboot.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.farukgenc.boilerplate.springboot.model.Ticket;
import com.farukgenc.boilerplate.springboot.repository.TicketRepository;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;

@Service
public class TicketService {
  private final TicketRepository ticketRepository;
  private final CamundaClient client;

  @Autowired
  public TicketService(TicketRepository ticketRepository, CamundaClient client) {
    this.ticketRepository = ticketRepository;
    this.client = client;
  }

  public void sumit(String ticketNo) {
    Ticket ticket = new Ticket();
    ticket.setTicketNo(ticketNo);
    ticket.setTitle("Proposal ");

    try {
      ProcessInstanceEvent job = client.newCreateInstanceCommand()
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

  public void approveTicket(Long jobKey) throws NotFoundException {
    // Complete the job with approval data
    client.newCompleteCommand(jobKey)
        .variables(Map.of(
            "approved", true))
        .send()
        .join();
  }
}
