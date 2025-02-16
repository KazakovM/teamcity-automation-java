package org.example.teamcity.api;

import org.example.teamcity.api.models.*;
import org.example.teamcity.api.requests.checked.CheckedBase;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static io.qameta.allure.Allure.step;
import static org.example.teamcity.api.enums.Endpoint.*;
import static org.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.example.teamcity.api.spec.Specifications.authSpec;
import static org.example.teamcity.api.spec.Specifications.superUserSpec;

@Test(groups = {"Regression"})
public class BuildConfigurationTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        var user = generate(User.class);
        step("Create user", () -> {
            new CheckedBase<User>(superUserSpec(), USERS).create(user);
        });

        var project = generate(Project.class);
        AtomicReference<String> projectId = new AtomicReference<>("");
        step("Create project by user", () -> {
            var requester = new CheckedBase<Project>(authSpec(user), PROJECTS);
            projectId.set(requester.create(project).getId());
        });

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(projectId.get()).locator(null).build());
        var buildTyperequester = new CheckedBase<BuildType>(authSpec(user), BUILD_TYPES);
        AtomicReference<String> buildTypeId = new AtomicReference<>("");
        step("Create buildType for project", () -> {
            buildTypeId.set(buildTyperequester.create(buildType).getId());
        });

        step("Check buildType was created successfully with correct data", () -> {
            var createdBuildType = buildTyperequester.read(buildTypeId.get());
            softAssert.assertEquals(buildType.getName(), createdBuildType.getName(), "buildTypeName is not correct");
        });
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
