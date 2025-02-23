package org.example.teamcity.api;

import org.apache.http.HttpStatus;
import org.example.teamcity.api.models.BuildType;
import org.example.teamcity.api.models.Project;
import org.example.teamcity.api.models.User;
import org.example.teamcity.api.requests.CheckedRequests;
import org.example.teamcity.api.requests.unchecked.UncheckedBase;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.qameta.allure.Allure.step;
import static org.example.teamcity.api.enums.Endpoint.*;
import static org.example.teamcity.api.factory.EntityFactory.*;
import static org.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.example.teamcity.api.spec.Specifications.authSpec;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(authSpec(testData.getUser()));
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckedRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softAssert.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "buildTypeName is not correct");
        softAssert.assertEquals(testData.getBuildType().getId(), createdBuildType.getId(), "buildId is not correct");
    }

    /**
     * Вариант теста при добавлении класса для создания часто используемых сущностей.
     * Хочу получить фидбек по такому решению
     */
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest2() {
        var user = createUser();
        var project = createProject(user);
        var buildType = createBuildType(user, project);

        var createdBuildType = new CheckedRequests(authSpec(user))
                .<BuildType>getRequest(BUILD_TYPES).read(buildType.getId());

        softAssert.assertEquals(buildType.getName(), createdBuildType.getName(), "buildTypeName is not correct");
        softAssert.assertEquals(buildType.getId(), createdBuildType.getId(), "buildId is not correct");
    }

    @Test(description = "User should not be able to create build type with non-unique id", groups = {"Negative", "CRUD"})
    public void userCreatesBuildTypeWithNonUniqueIdTest() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(authSpec(testData.getUser()));
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
        new UncheckedBase(authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("already used by another configuration"));

    }

    @Test(enabled = false, description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildType() {
        step("Create user");
        step("Create project by user");
        step("Grant user PROJECT_ADMIN role to project");
        step("Create buildType for project by user with PROJECT_ADMIN role");
        step("Check buildType was created successfully");
    }

    @Test(enabled = false, description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProject() {
        step("Create user1");
        step("Create project1 by user1");
        step("Grant user1 PROJECT_ADMIN role to project1");

        step("Create user2");
        step("Create project2 by user2");
        step("Grant user2 PROJECT_ADMIN role to project2");

        step("Create buildType for project2 by user1");
        step("Check buildType was not created with expected error message");
    }
}
