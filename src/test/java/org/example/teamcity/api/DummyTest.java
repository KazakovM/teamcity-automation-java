package org.example.teamcity.api;

import io.restassured.RestAssured;
import org.example.teamcity.api.models.User;
import org.testng.annotations.Test;

import static org.example.teamcity.api.spec.Specifications.authSpec;

public class DummyTest extends BaseApiTest {
    @Test(enabled = false)
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
                .given()
                .spec(authSpec(User.builder().username("admin").password("admin").build()))
                .get("/app/rest/projects");
    }
}
