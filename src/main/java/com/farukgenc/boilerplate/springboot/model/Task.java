package com.farukgenc.boilerplate.springboot.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
  public String id;
  public String name;
  public String taskDefinitionId;
  public String processName;
  public Date creationDate;
  public Date completionDate;
  public String assignee;
  public Date dueDate;
  public String taskState;
  public Long processInstanceKey;
}
