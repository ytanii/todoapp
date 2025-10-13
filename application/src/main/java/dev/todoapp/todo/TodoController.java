package dev.todoapp.todo;

import dev.todoapp.dashboard.DashboardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodoController {

  private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

  private final TodoService todoService;
  private final DashboardService dashboardService;

  public TodoController(TodoService todoService, DashboardService dashboardService) {
    this.todoService = todoService;
    this.dashboardService = dashboardService;
  }

  @GetMapping
  public String dashboard(Model model, @AuthenticationPrincipal OidcUser user) {
    String email = user.getEmail();
    
    model.addAttribute("collaborators", dashboardService.getAvailableCollaborators(email));
    model.addAttribute("todos", dashboardService.getAllOwnedAndSharedTodos(email));
    
    return "dashboard";
  }

  @GetMapping("/show/{id}")
  public String showView(
    @AuthenticationPrincipal OidcUser user,
    @PathVariable("id") long id,
    Model model
  ) {
    Todo todo = todoService.getOwnedOrSharedTodo(id, user.getEmail());
    model.addAttribute("todo", todo);
    
    logger.info("Showing todo with id {}", id);
    return "todo/show";
  }

  @GetMapping("/add")
  public String addView(Model model) {
    model.addAttribute("todo", new Todo());
    model.addAttribute("editMode", EditMode.CREATE);
    return "todo/edit";
  }

  @PostMapping
  public String add(
    @Valid Todo toBeCreatedTodo,
    BindingResult bindingResult,
    @AuthenticationPrincipal OidcUser user,
    Model model,
    RedirectAttributes redirectAttributes
  ) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("todo", toBeCreatedTodo);
      model.addAttribute("editMode", EditMode.CREATE);
      return "todo/edit";
    }

    Todo savedTodo = todoService.saveNewTodo(toBeCreatedTodo, user.getEmail(), user.getAttribute("name"));

    redirectAttributes.addFlashAttribute("message", "Your new todo has been successfully saved.");
    redirectAttributes.addFlashAttribute("messageType", "success");
    redirectAttributes.addFlashAttribute("todoId", savedTodo.getId());

    logger.info("Successfully created todo");
    return "redirect:/dashboard";
  }

  @GetMapping("/edit/{id}")
  public String editView(
    @AuthenticationPrincipal OidcUser user,
    @PathVariable("id") long id,
    Model model
  ) {
    Todo todo = todoService.getOwnedOrSharedTodo(id, user.getEmail());
    model.addAttribute("todo", todo);
    model.addAttribute("editMode", EditMode.UPDATE);
    return "todo/edit";
  }

  @PostMapping("/update/{id}")
  public String update(
    @AuthenticationPrincipal OidcUser user,
    @PathVariable("id") long id,
    @Valid Todo updatedTodo,
    BindingResult result,
    Model model,
    RedirectAttributes redirectAttributes
  ) {
    if (result.hasErrors()) {
      model.addAttribute("todo", updatedTodo);
      model.addAttribute("editMode", EditMode.UPDATE);
      return "todo/edit";
    }

    todoService.updateTodo(updatedTodo, id, user.getEmail());

    redirectAttributes.addFlashAttribute("message", "Your todo was successfully updated.");
    redirectAttributes.addFlashAttribute("messageType", "success");

    logger.info("Successfully updated todo");
    return "redirect:/dashboard";
  }

  @PostMapping("/delete/{id}")
  public String deleteTodo(@PathVariable Long id, @AuthenticationPrincipal OidcUser user) {
    Optional<Todo> todo = todoService.findById(id);
    if (todo.isPresent() && todo.get().getOwner().getEmail().equals(user.getEmail())) {
      todoService.delete(todo.get());
    }
    return "redirect:/dashboard";
  }
}
