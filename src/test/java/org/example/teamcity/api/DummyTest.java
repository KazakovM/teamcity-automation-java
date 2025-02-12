package org.example.teamcity.api;

import io.restassured.RestAssured;
import org.example.teamcity.api.models.User;
import org.testng.annotations.Test;

import static org.example.teamcity.api.spec.Specifications.getSpecifications;

public class DummyTest extends BaseApiTest {
    @Test
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
                .given()
                .spec(getSpecifications().authSpec(User.builder().user("admin").password("admin").build()))
                .get("/app/rest/projects");
    }
}
