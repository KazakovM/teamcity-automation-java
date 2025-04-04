package org.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.teamcity.ui.pages.ProjectsPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private SelenideElement projectNameInput = $("#projectName");

    @Step("Open Project Page")
    public static CreateProjectPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        Selenide.sleep(1000);
    }
}
