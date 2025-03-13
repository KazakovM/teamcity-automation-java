package org.example.teamcity.ui;

import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.ui.pages.LoginPage;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        // подготовка окружения
        step("Login as user");

        // взаимодействие с UI
        step("Open `Create Project Page` ()");
        step("Send all project parameters (repo url)");
        step("Click `Proceed`");
        step("Fix project name and build type name values");
        step("Click `Proceed`");

        // проверка состояния API (корректность отправки данных с UI на API)
        step("Check via API that all entities (project, build type) were successfully created with correct data");

        // проверка состояния UI (корректность считывания данных с API и их отображение на UI)
        step("Check that project is visible on project page");
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        // подготовка окружения
        step("Login as user");
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        LoginPage.open()
                .login(testData.getUser());
        step("Get number of projects");

        // взаимодействие с UI
        step("Open `Create Project Page` ()");
        step("Send all project parameters (repo url)");
        step("Click `Proceed`");
        step("Set project name value empty");
        step("Click `Proceed`");

        // проверка состояния API (корректность отправки данных с UI на API)
        step("Check that number of projects did not change");

        step("Check that error appears `Project name must not be empty`");

    }


}
