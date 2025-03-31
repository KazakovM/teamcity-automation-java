package org.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import org.example.teamcity.api.models.Project;
import org.example.teamcity.ui.pages.ProjectPage;
import org.example.teamcity.ui.pages.ProjectsPage;
import org.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static org.example.teamcity.api.enums.Endpoint.PROJECTS;
import static org.example.teamcity.api.generators.TestDataStorage.getTestDataStorage;

@Test(groups = {"Regression"})
public class ProjectCreateTest extends BaseUiTest {

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() throws InterruptedException {
        // подготовка окружения
        loginAs(testData.getUser());

        // взаимодействие с UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        // проверка состояния API (корректность отправки данных с UI на API)
        var createdProject = superUserCheckedRequests.<Project>getRequest(PROJECTS).read("name:" + testData.getProject().getName());
        softAssert.assertNotNull(createdProject);

        // проверка состояния UI (корректность считывания данных с API и их отображение на UI)
        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var projectExists = ProjectsPage.open()
                .getProjects().stream()
                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));
        softAssert.assertTrue(projectExists);
        getTestDataStorage().addCreatedEntity(PROJECTS, createdProject);
    }
}
