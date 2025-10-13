package dev.todoapp.dashboard;

import dev.todoapp.person.Person;
import dev.todoapp.todo.Priority;
import dev.todoapp.todo.Status;
import dev.todoapp.todo.Todo;

import java.time.LocalDate;
import java.util.List;

public class TodoDto {

  private Long id;
  private String title;
  private String description;
  private Priority priority;
  private Status status;
  private Person owner;
  private List<Person> collaborators;
  private int amountOfCollaborators;
  private int amountOfCollaborationRequests;
  private LocalDate dueDate;
  private boolean isCollaboration;

  public TodoDto(Todo todo, boolean isCollaboration) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.description = todo.getDescription();
    this.priority = todo.getPriority();
    this.status = todo.getStatus();
    this.owner = todo.getOwner();
    this.collaborators = todo.getCollaborators();
    this.amountOfCollaborationRequests = todo.getCollaborationRequests().size();
    this.amountOfCollaborators = todo.getCollaborators().size();
    this.dueDate = todo.getDueDate();
    this.isCollaboration = isCollaboration;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Priority getPriority() {
    return priority;
  }

  public Status getStatus() {
    return status;
  }

  public Person getOwner() {
    return owner;
  }

  public List<Person> getCollaborators() {
    return collaborators;
  }

  public int getAmountOfCollaborators() {
    return amountOfCollaborators;
  }

  public int getAmountOfCollaborationRequests() {
    return amountOfCollaborationRequests;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public boolean isCollaboration() {
    return isCollaboration;
  }
}
