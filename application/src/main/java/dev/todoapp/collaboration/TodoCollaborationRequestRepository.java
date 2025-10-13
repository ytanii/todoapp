package dev.todoapp.collaboration;

import dev.todoapp.person.Person;
import dev.todoapp.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoCollaborationRequestRepository extends JpaRepository<TodoCollaborationRequest, Long> {
  TodoCollaborationRequest findByTodoIdAndCollaboratorId(Long todoId, Long collaboratorId);
  TodoCollaborationRequest findByTodoAndCollaborator(Todo todo, Person collaborator);
  TodoCollaborationRequest findByTodoIdAndCollaboratorIdAndToken(Long todoId, Long collaboratorId, String token);
}
