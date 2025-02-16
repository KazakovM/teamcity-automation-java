package org.example.teamcity.api.factory;

import org.example.teamcity.api.models.BuildType;
import org.example.teamcity.api.models.Project;
import org.example.teamcity.api.models.User;
import org.example.teamcity.api.requests.checked.CheckedBase;

import static io.qameta.allure.Allure.step;

import static org.example.teamcity.api.enums.Endpoint.*;
import static org.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.example.teamcity.api.spec.Specifications.authSpec;
import static org.example.teamcity.api.spec.Specifications.superUserSpec;

/**
 * Класс с методами-хелперами для удобного создания часто используемых сущностей
 */
public class EntityFactory {
    public static User createUser() {
        var user = generate(User.class);
        return createUser(user);
    }

    public static User createUser(User user) {
        step("Create user", () -> {
            new CheckedBase<User>(superUserSpec(), USERS).create(user);
        });
        return user;
    }

    public static Project createProject(User user) {
        var project = generate(Project.class);
        return createProject(user, project);
    }

    public static Project createProject(User user, Project project) {
        step("Create project by user", () -> {
            var requester = new CheckedBase<Project>(authSpec(user), PROJECTS);
            project.setId(requester.create(project).getId());
        });
        return project;
    }

    public static BuildType createBuildType(User user, Project project) {
        var buildType = generate(BuildType.class);
        return createBuildType(user, project, buildType);
    }

    public static BuildType createBuildType(User user, Project project, BuildType buildType) {
        step("Create buildType for project", () -> {
            buildType.setProject(Project.builder().id(project.getId()).locator(null).build());
            var requester = new CheckedBase<BuildType>(authSpec(user), BUILD_TYPES);
            buildType.setId(requester.create(buildType).getId());
        });
        return buildType;
    }
}
