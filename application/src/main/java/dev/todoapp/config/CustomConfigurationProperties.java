package dev.todoapp.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@ConfigurationProperties(prefix = "custom")
@Validated
class CustomConfigurationProperties {

  @NotEmpty
  private Set<String> invitationCodes;

  @NotNull
  private Boolean useCognitoAsIdentityProvider;

  public Boolean getUseCognitoAsIdentityProvider() {
    return useCognitoAsIdentityProvider;
  }

  public void setUseCognitoAsIdentityProvider(Boolean useCognitoAsIdentityProvider) {
    this.useCognitoAsIdentityProvider = useCognitoAsIdentityProvider;
  }

  public Set<String> getInvitationCodes() {
    return invitationCodes;
  }

  public void setInvitationCodes(Set<String> invitationCodes) {
    this.invitationCodes = invitationCodes;
  }

}
