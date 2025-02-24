package com.farukgenc.boilerplate.springboot.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.farukgenc.boilerplate.springboot.model.Ticket;
import com.farukgenc.boilerplate.springboot.repository.TicketRepository;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Service
public class TicketService {
  final CamundaClient client;
  private final static Logger LOG = LoggerFactory.getLogger(TicketService.class);
  private final TicketRepository ticketRepository;

  @Autowired // Constructor injection is preferred
  public TicketService(CamundaClient client, TicketRepository ticketRepository) {
    this.client = client;
    this.ticketRepository = ticketRepository;
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

  // FIXME:: update this function

  // public void approveTicket(
  // @PathVariable Long ticketId,
  // @RequestParam String userId,
  // @RequestBody ApprovalData data) {
  //
  // // Find the ticket
  // Ticket ticket = ticketRepository.findById(ticketId)
  // .orElseThrow(() -> new NotFoundException());
  //
  // // In Camunda 8, you would have stored the job key for this user's task
  // // This might be in a separate UserTask table with references to tickets
  // Long jobKey = userTaskService.findActiveTaskJobKey(ticketId, userId);
  //
  // if (jobKey == null) {
  // return ResponseEntity.badRequest().body("No active task found for this
  // user");
  // }
  //
  // // Complete the job with approval data
  // client.newCompleteCommand(jobKey)
  // .variables(Map.of(
  // "approved", true,
  // "comments", data.getComments(),
  // "approvedBy", userId))
  // .send()
  // .join();
  //
  // // Update ticket status in your database
  // ticket.updateStatus("APPROVED_BY_" + userId);
  // ticketRepository.save(ticket);
  //
  // return ResponseEntity.ok().build();
  // }
}
