package org.example.teamcity.ui;

import org.example.teamcity.api.models.BuildType;
import org.example.teamcity.ui.pages.ProjectPage;
import org.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static org.example.teamcity.ui.constants.ErrorMessages.ERROR_BUILD_TYPE_EMPTY_NAME;

@Test(groups = {"Regression"})
public class BuildTypeCreateTest extends BaseUiTest {

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        super.beforeTest();
        loginAs(testData.getUser());
        createProject(testData.getProject());
    }

    @Test(description = "User should be able to create buildType", groups = {"Positive"})
    public void userCreatesBuildTypeTest() {
        step("Create buildType", () -> {
            CreateBuildTypePage.open(testData.getProject().getId())
                    .createForm(REPO_URL)
                    .setupBuildType(testData.getBuildType().getName());
        });

        step("API check that buildType was successfully created", () -> {
            var createdBuildType = superUserCheckedRequests.<BuildType>getRequest(BUILD_TYPES)
                    .read("name:" + testData.getBuildType().getName());
            softAssert.assertEquals(testData.getBuildType().getName(), createdBuildType.getName());
        });

        step("Check that buildType is displayed on Project page", () -> {
            var buildTypeExists = ProjectPage.open(testData.getProject().getId())
                    .getBuildTypes().stream()
                    .anyMatch(buildType -> buildType.getName().equals(testData.getBuildType().getName()));
            softAssert.assertTrue(buildTypeExists);
        });
        softAssert.assertAll();
    }

    @Test(description = "User should not be able to create buildType without name", groups = {"Negative"})
    public void userCreatesBuildTypeWithoutNameTest() {
        step("Create buildType without name", () -> {
            var errorMessage = CreateBuildTypePage.open(testData.getProject().getId())
                    .createForm(REPO_URL)
                    .setupBuildType(null).getErrorMessage();

            softAssert.assertEquals(errorMessage, ERROR_BUILD_TYPE_EMPTY_NAME);
        });

        step("API check that buildType was not created", () -> {
            var response = superUserUncheckedRequests.getRequest(BUILD_TYPES)
                    .read("project:" + testData.getProject().getId());
            softAssert.assertEquals(response.statusCode(), SC_NOT_FOUND);
        });
    }
}
