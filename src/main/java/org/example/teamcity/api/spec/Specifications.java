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

    private Specifications() {

    }

    public static Specifications getSpecifications() {
        if (specifications == null) {
            specifications = new Specifications();
        }
        return specifications;
    }

    private RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri("http://" + getProperty("host"));
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setAccept(ContentType.JSON);
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        return reqBuilder;
    }

    public RequestSpecification unauthSpec() {
        return reqBuilder().build();
    }

    public RequestSpecification authSpec(User user) {
        BasicAuthScheme authScheme = new BasicAuthScheme();
        authScheme.setPassword(user.getPassword());
        authScheme.setUserName(user.getUser());
        return reqBuilder().setAuth(authScheme).build();
    }
}
