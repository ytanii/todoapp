package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

import dev.stratospheric.cdk.SpringBootApplicationStack;

public class TodoappApp {
    public static void main(final String[] args) {
        App app = new App();

        String accountId = (String) app
                .getNode()
                .tryGetContext("accountId");
        requireNonNull(accountId, "context variable 'accountId' must not be null");
        String region = (String) app
                .getNode()
                .tryGetContext("region");
        requireNonNull(region, "context variable 'region' must not be null");
        new SpringBootApplicationStack(
                app,
                "SpringBootApplication",
                makeEnv(accountId, region),
                "docker.io/ysft07/todo-app-v1:latest");

        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}
