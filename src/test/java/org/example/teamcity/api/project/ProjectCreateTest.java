package org.example.teamcity.api.project;

import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.example.teamcity.api.BaseApiTest;
import org.example.teamcity.api.generators.RandomData;
import org.example.teamcity.api.models.ParentProject;
import org.example.teamcity.api.models.Project;
import org.example.teamcity.api.models.User;
import org.example.teamcity.api.requests.CheckedRequests;
import org.example.teamcity.api.requests.unchecked.UncheckedBase;
import org.example.teamcity.common.provider.InvalidIdDataProvider;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.teamcity.api.enums.Endpoint.PROJECTS;
import static org.example.teamcity.api.enums.Endpoint.USERS;
import static org.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.example.teamcity.api.spec.Specifications.authSpec;
import static org.example.teamcity.api.spec.Specifications.unauthSpec;


@Feature("Create project")
@Test(groups = {"Regression", "CRUD"})
public class ProjectCreateTest extends BaseApiTest {
    private CheckedRequests checkedRequests;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        super.beforeTest();
        createUserAndInitRequests();
    }

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        var projectRequest = testData.getProject();
        var createdProject = createProject(projectRequest);

        assertProjectFields(projectRequest, createdProject);
    }

    /**
     * Текущая логика - при отсутствии переданного Id он должен генерироваться автоматически на стороне сервера
     * Значение Id должно соответствовать значению Name при удалении пунктуации и приведении к единому регистру
     * Логика не описана в пользовательской документации, но может считаться валидной
     */
    @Test(description = "User should be able to create project with Id = null", groups = {"Positive"})
    public void userCreatesProjectWithNullId() {
        var projectRequest = testData.getProject();
        projectRequest.setId(null);

        var id = createProject(projectRequest).getId();
        var createdProject = readProject(id);

        softAssert.assertEquals(
                projectRequest.getName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase(),
                createdProject.getId().toLowerCase(), "projectId is not correct");
        softAssert.assertEquals(projectRequest.getName(), createdProject.getName(), "projectName is not correct");
        softAssert.assertEquals(projectRequest.getParentProject(), createdProject.getParentProject(), "parentProject is not correct");
    }

    @Test(description = "User should be able to create project with valid Id length (min 1, max 225)", groups = {"Positive"}, dataProvider = "validIds")
    public void userCreatesProjectWithValidIdLength(String id) {
        var projectRequest = testData.getProject();
        projectRequest.setId(id);

        var createdProject = createProject(projectRequest);

        assertProjectFields(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create project with Name of 1 char", groups = {"Positive"})
    public void userCreatesProjectWithMinLengthName() {
        var projectRequest = testData.getProject();
        projectRequest.setName("s");

        var createdProject = createProject(projectRequest);

        assertProjectFields(projectRequest, createdProject);
    }

    /**
     * При отсутствии переданного parentProject должен создаваться дефолтный ParentProject с "locator": "_Root"
     */
    @Test(description = "User should be able to create project with parentProject = null", groups = {"Positive"})
    public void userCreatesProjectWithNullParent() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(null);

        var createdProject = createProject(projectRequest);

        softAssert.assertEquals(projectRequest.getId(), createdProject.getId(), "projectId is not correct");
        softAssert.assertEquals(projectRequest.getName(), createdProject.getName(), "projectName is not correct");
        softAssert.assertNotEquals(projectRequest.getParentProject(), createdProject.getParentProject(), "parentProject is not correct");
    }

    @Test(description = "User should not be able to create project with invalid Id", groups = {"Negative"}, dataProvider = "invalidIds", dataProviderClass = InvalidIdDataProvider.class)
    public void userCreatesProjectWithInvalidId(String invalidId) {
        var projectDto = testData.getProject();
        projectDto.setId(invalidId);

        assertProjectCreationFails(projectDto, SC_INTERNAL_SERVER_ERROR, "ID should start with a latin letter");
        // todo 500 вместо 400 - баг?
    }

    @Test(description = "User should not be able to create project with empty Id", groups = {"Negative"})
    public void userCreatesProjectWithEmptyId() {
        var projectDto = testData.getProject();
        projectDto.setId("");

        assertProjectCreationFails(projectDto, SC_INTERNAL_SERVER_ERROR, "Project ID must not be empty" );
        // todo 500 вместо 400 - баг?
    }

    @Test(description = "User should not be able to create project with empty Name", groups = {"Negative"})
    public void userCreatesProjectWithEmptyName() {
        var projectDto = testData.getProject();
        projectDto.setName("");

        assertProjectCreationFails(projectDto, SC_BAD_REQUEST, "Project name cannot be empty");
    }

    @Test(description = "User should not be able to create project with duplicate Id (same case)", groups = {"Negative"})
    public void userCreatesProjectWithSameId() {
        createProject(testData.getProject());
        var projectWithSameId = generate(Project.class, testData.getProject().getId());

        assertProjectCreationFails(projectWithSameId, SC_BAD_REQUEST, "already used by another project");
    }

    @Test(description = "User should not be able to create project with duplicate Id (different case)", groups = {"Negative"})
    public void userCreatesProjectWithSameIdDifferentCase() {
        createProject(testData.getProject());
        var projectWithSameId = generate(Project.class, testData.getProject().getId().toUpperCase());

        assertProjectCreationFails(projectWithSameId, SC_BAD_REQUEST, "already used by another project");
    }

    @Test(description = "User should not be able to create project with duplicate Name (same case)", groups = {"Negative"})
    public void userCreatesProjectWithSameName() {
        createProject(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName());

        assertProjectCreationFails(projectWithSameName, SC_BAD_REQUEST, "Project with this name already exists");
    }

    @Test(description = "User should not be able to create project with duplicate Name (different case)", groups = {"Negative"})
    public void userCreatesProjectWithSameNameDifferentCase() {
        createProject(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName().toUpperCase());

        assertProjectCreationFails(projectWithSameName, SC_BAD_REQUEST, "Project with this name already exists");
    }

    @Test(description = "User should not be able to create project with non existent parent locator", groups = {"Negative"})
    public void userCreatesProjectWithNonExistentParentLocator() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator(RandomData.getString() + RandomData.getString()).build());

        assertProjectCreationFails(projectRequest, SC_NOT_FOUND, "No project found by name or internal/external id");
    }

    @Test(description = "User should not be able to create project with invalid dimension of parent locator", groups = {"Negative"})
    public void userShouldNotCreateProjectWithInvalidParentLocatorDimension() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator("test:").build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, "Locator dimension [test] is unknown");
    }

    @Test(description = "User should not be able to create project with empty parent locator", groups = {"Negative"})
    public void userCreatesProjectWithEmptyParentLocator() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator("").build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, "No project specified");
    }

    @Test(description = "Project creation should not be available without authorization", groups = {"Negative", "Authorization"})
    public void unauthorizedUserCreatesProject() {
        var projectRequest = testData.getProject();

        new UncheckedBase(unauthSpec(), PROJECTS)
                .create(projectRequest)
                .then()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));
    }

    @Test(description = "User without privileged role should not be able to create project", groups = {"Negative", "Roles"})
    public void userCreatesProjectWithoutPrivilege() {
        var userRequest = generate(User.class);
        userRequest.setRoles(null);
        superUserCheckedRequests.getRequest(USERS).create(userRequest);
        var projectRequest = testData.getProject();

        new UncheckedBase(authSpec(userRequest), PROJECTS)
                .create(projectRequest)
                .then()
                .assertThat().statusCode(SC_FORBIDDEN)
                .body(Matchers.containsString("You do not have \"Create subproject\" permission"));
    }

    private void createUserAndInitRequests() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        checkedRequests = new CheckedRequests(authSpec(testData.getUser()));
    }

    private void assertProjectFields(Project expected, Project actual) {
        softAssert.assertEquals(actual.getId(), expected.getId(), "projectId is not correct");
        softAssert.assertEquals(actual.getName(), expected.getName(), "projectName is not correct");
        softAssert.assertEquals(actual.getParentProject(), expected.getParentProject(), "parentProject is not correct");
        softAssert.assertAll();
    }

    @DataProvider(name = "validIds")
    public Object[][] validIds() {
        return new Object[][]{
                {"z"},
                {"a".repeat(225)}
        };
    }

    private void assertProjectCreationFails(Project projectRequest, int expectedStatusCode, String expectedErrorMessage) {
        new UncheckedBase(authSpec(testData.getUser()), PROJECTS)
                .create(projectRequest)
                .then()
                .assertThat().statusCode(expectedStatusCode)
                .body(Matchers.containsString(expectedErrorMessage));
    }

    private Project createProject(Project projectRequest) {
        return checkedRequests.<Project>getRequest(PROJECTS).create(projectRequest);
    }

    private Project readProject(String projectId) {
        return checkedRequests.<Project>getRequest(PROJECTS).read(projectId);
    }
}

// ВОПРОС: какие из нетестовых методов можно перенести в отдельные классы вне тестов для потенциального переиспользования?
// как эти классы могли бы называться и где находиться?
