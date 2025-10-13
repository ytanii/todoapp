package dev.todoapp.collaboration;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todo")
public class TodoCollaborationController {

  private final TodoCollaborationService todoCollaborationService;

  public TodoCollaborationController(TodoCollaborationService todoCollaborationService) {
    this.todoCollaborationService = todoCollaborationService;
  }

  @PostMapping("/{todoId}/collaborations/{collaboratorId}")
  public String shareTodoWithCollaborator(
      @PathVariable("todoId") Long todoId,
      @PathVariable("collaboratorId") Long collaboratorId,
      @AuthenticationPrincipal OidcUser user,
      RedirectAttributes redirectAttributes) {
    
    String collaboratorName = todoCollaborationService.shareWithCollaborator(user.getEmail(), todoId, collaboratorId);
    
    redirectAttributes.addFlashAttribute("message", 
        "Todo shared with " + collaboratorName + ". They will receive an email notification.");
    redirectAttributes.addFlashAttribute("messageType", "success");
    
    return "redirect:/todo";
  }

  @GetMapping("/{todoId}/collaborations/confirm")
  public String confirmCollaboration(
      @PathVariable("todoId") Long todoId,
      @RequestParam("token") String token,
      @AuthenticationPrincipal OidcUser user,
      RedirectAttributes redirectAttributes) {
    
    if (todoCollaborationService.confirmCollaboration(user.getEmail(), todoId, token)) {
      redirectAttributes.addFlashAttribute("message", 
          "You've confirmed that you'd like to collaborate on this todo.");
      redirectAttributes.addFlashAttribute("messageType", "success");
    } else {
      redirectAttributes.addFlashAttribute("message", "Invalid collaboration request.");
      redirectAttributes.addFlashAttribute("messageType", "danger");
    }
    
    return "redirect:/todo";
  }
}
