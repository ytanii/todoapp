package dev.todoapp.collaboration;

import dev.todoapp.person.Person;
import dev.todoapp.todo.Todo;
import jakarta.persistence.*;

@Entity
public class TodoCollaborationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String token;

  @ManyToOne
  @JoinColumn(name = "todo_id")
  private Todo todo;

  @ManyToOne
  @JoinColumn(name = "collaborator_id")
  private Person collaborator;

  public TodoCollaborationRequest() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Todo getTodo() {
    return todo;
  }

  public void setTodo(Todo todo) {
    this.todo = todo;
  }

  public Person getCollaborator() {
    return collaborator;
  }

  public void setCollaborator(Person collaborator) {
    this.collaborator = collaborator;
  }
}
