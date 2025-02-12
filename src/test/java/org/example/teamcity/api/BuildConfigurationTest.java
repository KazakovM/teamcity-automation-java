package org.example.teamcity.api;

import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;


public class BuildConfigurationTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType for project");
        step("Check buildType was created successfully with correct data");
    }

    @Test(description = "User should not be able to create build type with non-unique id", groups = {"Regression"})
    public void userCreatesBuildTypeWithNonUniqueIdTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType1 for project");
        step("Create buildType2 for project with same Id as buildType1");
        step("Check buildType2 was not created with expected error message");
    }
}
