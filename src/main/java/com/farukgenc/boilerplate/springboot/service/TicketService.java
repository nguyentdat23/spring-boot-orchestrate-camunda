package com.farukgenc.boilerplate.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.farukgenc.boilerplate.springboot.model.Ticket;
import com.farukgenc.boilerplate.springboot.repository.TicketRepository;

import io.camunda.zeebe.client.ZeebeClient;

@Service
public class TicketService {
  private final ZeebeClient zeebeClient;
  private final static Logger LOG = LoggerFactory.getLogger(TicketService.class);
  private final TicketRepository ticketRepository;

  @Autowired // Constructor injection is preferred
  public TicketService(ZeebeClient zeebeClient, TicketRepository ticketRepository) {
    this.zeebeClient = zeebeClient;
    this.ticketRepository = ticketRepository;
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

  public void getTaskList(String assignee) {

    LOG.info("Total usertask found: %s");
  }
}
