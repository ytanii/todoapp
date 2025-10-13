package dev.todoapp.collaboration;

public class TodoCollaborationNotification {

  private String collaboratorEmail;
  private String collaboratorName;
  private String todoTitle;
  private String todoDescription;
  private String todoPriority;
  private Long todoId;
  private Long collaboratorId;
  private String token;

  public TodoCollaborationNotification() {
  }

  public TodoCollaborationNotification(TodoCollaborationRequest request) {
    this.collaboratorEmail = request.getCollaborator().getEmail();
    this.collaboratorName = request.getCollaborator().getName();
    this.todoTitle = request.getTodo().getTitle();
    this.todoDescription = request.getTodo().getDescription();
    this.todoPriority = request.getTodo().getPriority().toString();
    this.todoId = request.getTodo().getId();
    this.collaboratorId = request.getCollaborator().getId();
    this.token = request.getToken();
  }

  public String getCollaboratorEmail() {
    return collaboratorEmail;
  }

  public void setCollaboratorEmail(String collaboratorEmail) {
    this.collaboratorEmail = collaboratorEmail;
  }

  public String getCollaboratorName() {
    return collaboratorName;
  }

  public void setCollaboratorName(String collaboratorName) {
    this.collaboratorName = collaboratorName;
  }

  public String getTodoTitle() {
    return todoTitle;
  }

  public void setTodoTitle(String todoTitle) {
    this.todoTitle = todoTitle;
  }

  public String getTodoDescription() {
    return todoDescription;
  }

  public void setTodoDescription(String todoDescription) {
    this.todoDescription = todoDescription;
  }

  public String getTodoPriority() {
    return todoPriority;
  }

  public void setTodoPriority(String todoPriority) {
    this.todoPriority = todoPriority;
  }

  public Long getTodoId() {
    return todoId;
  }

  public void setTodoId(Long todoId) {
    this.todoId = todoId;
  }

  public Long getCollaboratorId() {
    return collaboratorId;
  }

  public void setCollaboratorId(Long collaboratorId) {
    this.collaboratorId = collaboratorId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
