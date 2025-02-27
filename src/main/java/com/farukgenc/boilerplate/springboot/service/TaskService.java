package com.farukgenc.boilerplate.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.farukgenc.boilerplate.springboot.model.Task;

import io.jsonwebtoken.lang.Arrays;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

  private final WebClient webClient;

  private final CamundaIdentity camundaIdentity;

  @Autowired
  public TaskService(CamundaIdentity camundaIdentity, WebClient webClient) {
    this.camundaIdentity = camundaIdentity;
    this.webClient = webClient;
  }

  @Value("${camunda.tasklist.base-url}")
  private String tasklistBaseUrl;

  public List<Task> getPendingTasks(String asignee) {

    String token = camundaIdentity.getAccessToken();

    String requestBody = String.format(
        "{\"state\": \"CREATED\", \"assigned\": true, \"assignee\": \"%s\"}",
        asignee);

    Mono<Task[]> responseMono = webClient.post()
        .uri("http://localhost:8082/v1/tasks/search")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(Task[].class);

    List<Task> tasks = Arrays.asList(responseMono.block());

    return tasks;
  }

  public void complete(String taskId) {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("approved", true);
    variables.put("comment", "OK");

    Mono<Map> completeTaskMono = webClient.post()
        .uri("http://localhost:8082/v1/tasks/" + taskId + "/complete")
        .header("Authorization", "Bearer " + camundaIdentity.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(variables)
        .retrieve()
        .bodyToMono(Map.class);
    completeTaskMono.block();
  }
}
