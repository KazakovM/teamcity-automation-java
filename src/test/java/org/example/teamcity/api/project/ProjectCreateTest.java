package org.example.teamcity.api.project;

import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.example.teamcity.api.BaseApiTest;
import org.example.teamcity.api.generators.RandomData;
import org.example.teamcity.api.models.*;
import org.example.teamcity.api.requests.CheckedRequests;
import org.example.teamcity.api.requests.unchecked.UncheckedBase;
import org.example.teamcity.common.provider.BooleanProvider;
import org.example.teamcity.common.provider.InvalidIdDataProvider;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.http.HttpStatus.*;
import static org.example.teamcity.api.constants.ErrorMessages.*;
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

    @Test(description = "User should be able to create project with correct data", groups = {"Positive"})
    public void userCreatesProjectWithCorrectData() {
        var projectRequest = testData.getProject();
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create a project if id includes repeating symbols", groups = {"Positive"})
    public void userCreatesProjectWithIdRepeatingSymbols() {
        var projectRequest = testData.getProject();
        projectRequest.setId("AAAAAAA");
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create a project if id includes latin letters, digits", groups = {"Positive"})
    public void userCreatesProjectWithIdLatinDigits() {
        var projectRequest = testData.getProject();
        projectRequest.setId(projectRequest.getId() + "1");
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create project with Id = null", groups = {"Positive"})
    public void userCreatesProjectWithNullId() {
        var projectRequest = testData.getProject();
        projectRequest.setId(null);

        var id = createProject(projectRequest).getId();
        projectRequest.setId(id);

        var createdProject = readProject(id);
        softAssert.assertEquals(createdProject, projectRequest);
    }

    @Test(description = "User should be able to create project with valid Id length (min 1, max 225)", groups = {"Positive"}, dataProvider = "validIds")
    public void userCreatesProjectWithValidIdLength(String id) {
        var projectRequest = testData.getProject();
        projectRequest.setId(id);
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create a project if name has 1 symbol", groups = {"Positive"})
    public void userCreatesProjectWithMinLengthName() {
        var projectRequest = testData.getProject();
        projectRequest.setName("s");
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create a project if name has more than 225 symbols", groups = {"Positive"})
    public void userCreatesProjectWithLongName() {
        var projectRequest = testData.getProject();
        projectRequest.setName("b".repeat(226));
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create a project if name has cyrillic symbols", groups = {"Positive"})
    public void userCreatesProjectWithCyrillicName() {
        var projectRequest = testData.getProject();
        projectRequest.setName(projectRequest.getName() + " кириллица");
        var createdProject = createProject(projectRequest);
        softAssert.assertEquals(projectRequest, createdProject);
    }

    @Test(description = "User should be able to create project with parentProject = null", groups = {"Positive"})
    public void userCreatesProjectWithNullParent() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(null);
        var createdProject = createProject(projectRequest);

        softAssert.assertEquals(projectRequest.getId(), createdProject.getId(), "projectId is not correct");
        softAssert.assertEquals(projectRequest.getName(), createdProject.getName(), "projectName is not correct");
        softAssert.assertNotEquals(projectRequest.getParentProject(), createdProject.getParentProject(), "parentProject is not correct");
    }

    @Test(description = "User should be able to create a copy of a project with or without all associated settings", groups = {"Positive"}, dataProvider = "booleanProvider",  dataProviderClass = BooleanProvider.class)
    public void userCreatesProjectByCopying(boolean copyAllAssociatedSettings) {
        var id = createProject(testData.getProject()).getId();
        var projectRequest = generate(Project.class);
        projectRequest.setSourceProject(SourceProject.builder().locator(id).build());
        projectRequest.setCopyAllAssociatedSettings(copyAllAssociatedSettings);

        var createdProject = createProject(projectRequest);

        assertProjectFields(projectRequest, createdProject);
    }

    @Test(description = "User should not be able to create project with invalid Id", groups = {"Negative"}, dataProvider = "invalidIds", dataProviderClass = InvalidIdDataProvider.class)
    public void userCreatesProjectWithInvalidId(String invalidId) {
        var projectRequest = testData.getProject();
        projectRequest.setId(invalidId);

        assertProjectCreationFails(projectRequest, SC_INTERNAL_SERVER_ERROR, ERROR_ID_STARTS_WITH_LATIN_LETTER);
    }

    @Test(description = "User should not be able to create project with empty Id", groups = {"Negative"})
    public void userCreatesProjectWithEmptyId() {
        var projectRequest = testData.getProject();
        projectRequest.setId("");

        assertProjectCreationFails(projectRequest, SC_INTERNAL_SERVER_ERROR, ERROR_PROJECT_ID_EMPTY);
    }

    @Test(description = "User should not be able to create project with empty Name", groups = {"Negative"})
    public void userCreatesProjectWithEmptyName() {
        var projectRequest = testData.getProject();
        projectRequest.setName("");

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, ERROR_PROJECT_NAME_EMPTY);
    }

    @Test(description = "User should not be able to create project with duplicate Id (same case)", groups = {"Negative"})
    public void userCreatesProjectWithSameId() {
        createProject(testData.getProject());
        var projectWithSameId = generate(Project.class, testData.getProject().getId());

        assertProjectCreationFails(projectWithSameId, SC_BAD_REQUEST, ERROR_ID_ALREADY_USED);
    }

    @Test(description = "User should not be able to create project with duplicate Id (different case)", groups = {"Negative"})
    public void userCreatesProjectWithSameIdDifferentCase() {
        createProject(testData.getProject());
        var projectWithSameId = generate(Project.class, testData.getProject().getId().toUpperCase());

        assertProjectCreationFails(projectWithSameId, SC_BAD_REQUEST, ERROR_ID_ALREADY_USED);
    }

    @Test(description = "User should not be able to create project with duplicate Name (same case)", groups = {"Negative"})
    public void userCreatesProjectWithSameName() {
        createProject(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName());

        assertProjectCreationFails(projectWithSameName, SC_BAD_REQUEST, ERROR_PROJECT_NAME_EXISTS);
    }

    @Test(description = "User should not be able to create project with duplicate Name (different case)", groups = {"Negative"})
    public void userCreatesProjectWithSameNameDifferentCase() {
        createProject(testData.getProject());
        var projectWithSameName = generate(Project.class);
        projectWithSameName.setName(testData.getProject().getName().toUpperCase());

        assertProjectCreationFails(projectWithSameName, SC_BAD_REQUEST, ERROR_PROJECT_NAME_EXISTS);
    }

    @Test(description = "User should not be able to create project with non existent parent locator", groups = {"Negative"})
    public void userCreatesProjectWithNonExistentParentLocator() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator(RandomData.getString() + RandomData.getString()).build());

        assertProjectCreationFails(projectRequest, SC_NOT_FOUND, ERROR_PARENT_PROJECT_NOT_FOUND);
    }

    @Test(description = "User should not be able to create project with unknown dimension of parent locator", groups = {"Negative"})
    public void userShouldNotCreateProjectWithUnknownParentLocatorDimension() {
        var projectRequest = testData.getProject();
        var locator = randomAlphabetic(4);
        projectRequest.setParentProject(ParentProject.builder().locator(locator + ":").build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, getLocatorErrorMessage(locator));
    }

    @Test(description = "User should not be able to create project with invalid dimension of parent locator", groups = {"Negative"})
    public void userShouldNotCreateProjectWithInvalidParentLocatorDimension() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator(RandomData.getString() + ":").build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, ERROR_BAD_LOCATOR);
    }

    @Test(description = "User should not be able to create project with empty parent locator", groups = {"Negative"})
    public void userCreatesProjectWithEmptyParentLocator() {
        var projectRequest = testData.getProject();
        projectRequest.setParentProject(ParentProject.builder().locator("").build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, ERROR_NO_PROJECT_SPECIFIED);
    }

    @Test(description = "User should not be able to create a copy of non existing project", groups = {"Negative"})
    public void userCreatesCopyOfNonExistingProject() {
        var projectRequest = testData.getProject();
        projectRequest.setSourceProject(SourceProject.builder().locator(RandomData.getString() + RandomData.getString()).build());

        assertProjectCreationFails(projectRequest, SC_NOT_FOUND, ERROR_PARENT_PROJECT_NOT_FOUND);
    }

    @Test(description = "User should not be able to create a copy with empty info about source project", groups = {"Negative"})
    public void userCreatesCopyWithEmptyInfo() {
        var projectRequest = testData.getProject();
        projectRequest.setSourceProject(SourceProject.builder().locator(null).build());

        assertProjectCreationFails(projectRequest, SC_BAD_REQUEST, ERROR_NO_PROJECT_SPECIFIED);
    }

    @Test(description = "Project creation should not be available without authorization", groups = {"Negative", "Authorization"})
    public void unauthorizedUserCreatesProject() {
        var projectRequest = testData.getProject();

        new UncheckedBase(unauthSpec(), PROJECTS)
                .create(projectRequest)
                .then()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString(ERROR_AUTHENTICATION_REQUIRED));
    }

    @Test(description = "User should not be able to create project without privileged role", groups = {"Negative", "Roles"}, dataProvider = "rolesProvider")
    public void userCreatesProjectWithDifferentRoles(String roleId) {
        var userRequest = generate(User.class);

        if (roleId != null) {
            var role = Role.builder().roleId(roleId).build();
            userRequest.setRoles(Roles.builder().role(singletonList(role)).build());
        } else {
            userRequest.setRoles(null);
        }

        superUserCheckedRequests.getRequest(USERS).create(userRequest);
        var projectRequest = testData.getProject();
        new UncheckedBase(authSpec(userRequest), PROJECTS)
                .create(projectRequest)
                .then()
                .assertThat().statusCode(SC_FORBIDDEN)
                .body(Matchers.containsString(ERROR_NO_CREATE_SUBPROJECT_PERMISSION));
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

    @DataProvider(name = "rolesProvider")
    public Object[][] rolesProvider() {
        return new Object[][]{
                {null},
                {"PROJECT_VIEWER"},
                {"PROJECT_DEVELOPER"},
//                {"AGENT_MANAGER"}
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
        return checkedRequests.<Project>getRequest(PROJECTS).read("id:" + projectId);
    }
}
