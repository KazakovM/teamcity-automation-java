package org.example.teamcity.api.spec;

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.teamcity.api.models.User;

import java.util.List;

import static org.example.teamcity.api.config.Config.getProperty;

public class Specifications {
    private static Specifications specifications;

    private static RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri("http://" + getProperty("host")).build();
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setAccept(ContentType.JSON);
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        return reqBuilder;
    }

    public static RequestSpecification superUserSpec() {
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted("", getProperty("superUserToken"), getProperty("host")))
                .build();
    }

    public static RequestSpecification unauthSpec() {
        return reqBuilder().build();
    }

    public static RequestSpecification authSpec(User user) {
        BasicAuthScheme authScheme = new BasicAuthScheme();
        authScheme.setUserName(user.getUsername());
        authScheme.setPassword(user.getPassword());
        return reqBuilder()
                .setAuth(authScheme)
                .build();
    }
}
