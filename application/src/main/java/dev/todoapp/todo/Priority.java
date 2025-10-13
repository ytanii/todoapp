package dev.todoapp.todo;

public enum Priority {
  HIGH("High"),
  MEDIUM("Medium"),
  LOW("Low");

  private final String displayValue;

  Priority(String displayValue) {
    this.displayValue = displayValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }
}
