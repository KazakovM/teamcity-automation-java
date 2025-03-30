package org.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.example.teamcity.ui.elements.ProjectElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectsPage extends BasePage {
    private static final String PROJECTS_URL = "/favorite/projects";

    private SelenideElement header = $(".MainPanel__router--gF > div");

    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    private ElementsCollection projectElements = $$("div[class*='Subproject__container']");

    // ElementsCollection -> List<ProjectElement>
    // UI elements -> List<Object>
    // ElementsCollection -> List<BasePageElement>
    public static ProjectsPage open() {
        Selenide.open(PROJECTS_URL, ProjectsPage.class);
        return Selenide.page(ProjectsPage.class);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectElements, ProjectElement::new);
    }
}
