package com.farukgenc.boilerplate.springboot.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class ZeebeWorkers {
  @JobWorker(type = "task_approvers_take_action")
  public void handleReviewTask(final JobClient client, final ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String ticketId = (String) variables.get("ticketId");
    List<String> reviewers = (List<String>) variables.get("reviewers");

    // Create user tasks in your system
    for (String reviewer : reviewers) {
      // userTaskService.createUserTask(
      // ticketId,
      // reviewer,
      // "review-task",
      // job.getKey());
    }
  }
}
