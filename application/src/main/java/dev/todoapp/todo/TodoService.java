package dev.todoapp.todo;

import dev.todoapp.person.Person;
import dev.todoapp.person.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

  private final TodoRepository todoRepository;
  private final PersonRepository personRepository;

  public TodoService(TodoRepository todoRepository, PersonRepository personRepository) {
    this.todoRepository = todoRepository;
    this.personRepository = personRepository;
  }

  public List<Todo> findAllTodosForUser(String email) {
    return todoRepository.findAllByOwnerEmailOrderByIdAsc(email);
  }

  public Optional<Todo> findById(Long id) {
    return todoRepository.findById(id);
  }

  public Todo getOwnedOrSharedTodo(Long id, String email) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new NotFoundException());
    
    boolean isOwner = todo.getOwner().getEmail().equals(email);
    boolean isCollaborator = todo.getCollaborators().stream()
        .anyMatch(collaborator -> collaborator.getEmail().equals(email));
    
    if (!isOwner && !isCollaborator) {
      throw new ForbiddenException();
    }
    
    return todo;
  }

  public Person getOrCreateUser(String email, String name) {
    return personRepository.findByEmail(email)
        .orElseGet(() -> {
          Person newPerson = new Person();
          newPerson.setEmail(email);
          newPerson.setName(name);
          return personRepository.save(newPerson);
        });
  }

  public Todo saveNewTodo(Todo todo) {
    todo.setStatus(Status.OPEN);
    return todoRepository.save(todo);
  }

  public Todo saveNewTodo(Todo todo, String ownerEmail, String ownerName) {
    Person owner = getOrCreateUser(ownerEmail, ownerName);
    todo.setOwner(owner);
    todo.setStatus(Status.OPEN);
    return todoRepository.save(todo);
  }

  public Todo updateTodo(Todo updatedTodo, Long id, String ownerEmail) {
    Todo existingTodo = getOwnedOrSharedTodo(id, ownerEmail);
    
    existingTodo.setTitle(updatedTodo.getTitle());
    existingTodo.setDescription(updatedTodo.getDescription());
    existingTodo.setPriority(updatedTodo.getPriority());
    existingTodo.setDueDate(updatedTodo.getDueDate());
    
    return todoRepository.save(existingTodo);
  }

  public void delete(Todo todo) {
    todoRepository.delete(todo);
  }

  public Todo save(Todo todo) {
    return todoRepository.save(todo);
  }
}
