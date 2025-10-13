package dev.todoapp.collaboration;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class TodoSharingListener {

  private final MailSender mailSender;
  private final TodoCollaborationService todoCollaborationService;
  private final boolean autoConfirmCollaborations;
  private final String confirmEmailFromAddress;
  private final String externalUrl;

  private static final Logger LOG = LoggerFactory.getLogger(TodoSharingListener.class.getName());

  public TodoSharingListener(
    MailSender mailSender,
    TodoCollaborationService todoCollaborationService,
    @Value("${custom.auto-confirm-collaborations}") boolean autoConfirmCollaborations,
    @Value("${custom.confirm-email-from-address}") String confirmEmailFromAddress,
    @Value("${custom.external-url}") String externalUrl) {
    this.mailSender = mailSender;
    this.todoCollaborationService = todoCollaborationService;
    this.autoConfirmCollaborations = autoConfirmCollaborations;
    this.confirmEmailFromAddress = confirmEmailFromAddress;
    this.externalUrl = externalUrl;
  }

  @SqsListener(value = "${custom.sharing-queue}")
  public void listenToSharingMessages(TodoCollaborationNotification payload) throws InterruptedException {
    LOG.info("Incoming todo sharing payload: {}", payload);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(confirmEmailFromAddress);
    message.setTo(payload.getCollaboratorEmail());
    message.setSubject("A todo was shared with you");
    message.setText(
      String.format(
        "Hi %s, \n\n" +
          "someone shared a Todo from https://app.stratospheric.dev with you.\n\n" +
          "Information about the shared Todo item: \n\n" +
          "Title: %s \n" +
          "Description: %s \n" +
          "Priority: %s \n" +
          "\n" +
          "You can accept the collaboration by clicking this link: " +
          "%s/todo/%s/collaborations/confirm?token=%s \n\n" +
          "Kind regards, \n" +
          "Stratospheric",
        payload.getCollaboratorEmail(),
        payload.getTodoTitle(),
        payload.getTodoDescription(),
        payload.getTodoPriority(),
        externalUrl,
        payload.getTodoId(),
        payload.getToken()
      )
    );

    if (autoConfirmCollaborations) {
      LOG.info("Auto-confirmed collaboration request for todo: {}", payload.getTodoId());
      todoCollaborationService.confirmCollaboration(payload.getCollaboratorEmail(), payload.getTodoId(), payload.getToken());
    } else {
      mailSender.send(message);
    }
  }
}
