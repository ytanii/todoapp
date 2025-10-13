package dev.todoapp.collaboration;

import dev.todoapp.person.Person;
import dev.todoapp.person.PersonRepository;
import dev.todoapp.todo.Todo;
import dev.todoapp.todo.TodoRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@Transactional
public class TodoCollaborationService {

  private final TodoRepository todoRepository;
  private final PersonRepository personRepository;
  private final TodoCollaborationRequestRepository todoCollaborationRequestRepository;

  private final SqsTemplate sqsTemplate;
  private final String todoSharingQueueName;
  private final ObjectMapper objectMapper;

  private static final Logger LOG = LoggerFactory.getLogger(TodoCollaborationService.class.getName());

  private static final String INVALID_TODO_ID = "Invalid todo ID: ";
  private static final String INVALID_PERSON_ID = "Invalid person ID: ";
  private static final String INVALID_PERSON_EMAIL = "Invalid person Email: ";

  public TodoCollaborationService(
    @Value("${custom.sharing-queue}") String todoSharingQueueName,
    TodoRepository todoRepository,
    PersonRepository personRepository,
    TodoCollaborationRequestRepository todoCollaborationRequestRepository,
    SqsTemplate sqsTemplate,
    ObjectMapper objectMapper) {
    this.todoRepository = todoRepository;
    this.personRepository = personRepository;
    this.todoCollaborationRequestRepository = todoCollaborationRequestRepository;
    this.sqsTemplate = sqsTemplate;
    this.todoSharingQueueName = todoSharingQueueName;
    this.objectMapper = objectMapper;
  }

  public String shareWithCollaborator(String todoOwnerEmail, Long todoId, Long collaboratorId) {

    Todo todo = todoRepository
      .findByIdAndOwnerEmail(todoId, todoOwnerEmail)
      .orElseThrow(() -> new IllegalArgumentException(INVALID_TODO_ID + todoId));

    Person collaborator = personRepository
      .findById(collaboratorId)
      .orElseThrow(() -> new IllegalArgumentException(INVALID_PERSON_ID + collaboratorId));

    if (todoCollaborationRequestRepository.findByTodoAndCollaborator(todo, collaborator) != null) {
      LOG.info("Collaboration request for todo {} with collaborator {} already exists", todoId, collaboratorId);
      return collaborator.getName();
    }

    LOG.info("About to share todo with id {} with collaborator {}", todoId, collaboratorId);

    TodoCollaborationRequest collaboration = new TodoCollaborationRequest();
    String token = UUID.randomUUID().toString();
    collaboration.setToken(token);
    collaboration.setCollaborator(collaborator);
    collaboration.setTodo(todo);
    todo.getCollaborationRequests().add(collaboration);

    todoCollaborationRequestRepository.save(collaboration);

    try {
      TodoCollaborationNotification notification = new TodoCollaborationNotification(collaboration);
      String jsonMessage = objectMapper.writeValueAsString(notification);
      sqsTemplate.send(todoSharingQueueName, jsonMessage);
    } catch (Exception e) {
      LOG.error("Failed to send SQS message", e);
    }

    return collaborator.getName();
  }

  public boolean confirmCollaboration(String authenticatedUserEmail, Long todoId, String token) {

    Person collaborator = personRepository
      .findByEmail(authenticatedUserEmail)
      .orElseThrow(() -> new IllegalArgumentException(INVALID_PERSON_EMAIL + authenticatedUserEmail));

    TodoCollaborationRequest collaborationRequest = todoCollaborationRequestRepository
      .findByTodoIdAndCollaboratorIdAndToken(todoId, collaborator.getId(), token);

    if (collaborationRequest == null) {
      return false;
    }

    Todo todo = todoRepository
      .findById(todoId)
      .orElseThrow(() -> new IllegalArgumentException(INVALID_TODO_ID + todoId));

    todo.addCollaborator(collaborator);

    todoCollaborationRequestRepository.delete(collaborationRequest);

    LOG.info("Successfully confirmed collaboration for todo {} with user {}", todoId, authenticatedUserEmail);

    return true;
  }
}
