package org.example.teamcity.api;

import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildConfigurationTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType for project");
        step("Check buildType was created successfully with correct data");
    }

    @Test(description = "User should not be able to create build type with non-unique id", groups = {"Negative", "CRUD"})
    public void userCreatesBuildTypeWithNonUniqueIdTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType1 for project");
        step("Create buildType2 for project with same Id as buildType1");
        step("Check buildType2 was not created with expected error message");
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildType() {
        step("Create user");
        step("Create project by user");
        step("Grant user PROJECT_ADMIN role to project");
        step("Create buildType for project by user with PROJECT_ADMIN role");
        step("Check buildType was created successfully");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
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
