package dev.todoapp.person;

import dev.todoapp.todo.Todo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @Email
  @Column(unique = true)
  private String email;

  @ManyToMany(mappedBy = "collaborators")
  private List<Todo> collaborativeTodos = new ArrayList<>();

  public Person() {
  }

  public Person(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Todo> getCollaborativeTodos() {
    return collaborativeTodos;
  }

  public void setCollaborativeTodos(List<Todo> collaborativeTodos) {
    this.collaborativeTodos = collaborativeTodos;
  }
}
