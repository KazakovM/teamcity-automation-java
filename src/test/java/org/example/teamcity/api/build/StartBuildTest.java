package org.example.teamcity.api.build;

import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.example.teamcity.api.BaseApiTest;
import org.example.teamcity.api.models.Build;
import org.example.teamcity.api.models.BuildLog;
import org.example.teamcity.api.models.Project;
import org.example.teamcity.api.requests.CheckedRequests;
import org.example.teamcity.common.WireMock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.example.teamcity.api.constants.BuildState.BUILD_STATE_FINISHED;
import static org.example.teamcity.api.constants.BuildStatus.BUILD_STATUS_SUCCESS;
import static org.example.teamcity.api.enums.Endpoint.*;
import static org.example.teamcity.api.spec.Specifications.authSpec;
import static org.example.teamcity.api.spec.Specifications.mockSpec;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {
    private CheckedRequests checkedRequests;
    private CheckedRequests checkedMockRequest;

    @BeforeMethod(alwaysRun = true)
    public void setupWireMockServer() {
        setupBuildQueueMock();
        createTestDataAndInitRequests();
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }


    @Test(description = "User should be able to start build (with WireMock)", groups = {"Regression"})
    public void userStartsBuildWithWireMockTest() {
        var build = startBuild();
        assertBuildHappyEnding(build);
    }

    @Test(description = "User should be able to start build and retrieve logs (with Wiremock)", groups = {"Regression"})
    public void userStartsBuildAndReadsLogsWithWiremock() {
        setupBuildLogsMock(testData.getBuild().getId());
        var build = startBuild();
        assertBuildHappyEnding(build);

        var buildLogs = retrieveBuildLogs(testData.getBuild().getId());
        softAssert.assertTrue(buildLogs.getLogs().contains("Hello, world!"), "Build logs do not contain 'Hello, world!'");
    }


    private void setupBuildQueueMock() {
        var mockBuild = Build.builder()
                .state(BUILD_STATE_FINISHED)
                .status(BUILD_STATUS_SUCCESS)
                .build();
        WireMock.setupServer(post(BUILD_QUEUE.getUrl()), HttpStatus.SC_OK, mockBuild);
    }

    private void setupBuildLogsMock(String id) {
        var mockBuildLog = new BuildLog();
        mockBuildLog.setLogs("Hello, world!");
        WireMock.setupServer(get(urlEqualTo("/app/rest/builds/id:" + id + "/log")), HttpStatus.SC_OK, mockBuildLog);
    }


    private Build startBuild() {
        return checkedMockRequest.<Build>getRequest(BUILD_QUEUE).create(Build.builder()
                .buildType(testData.getBuildType())
                .build());
    }

    private BuildLog retrieveBuildLogs(String buildId) {
        return checkedMockRequest.<BuildLog>getRequest(BUILD_LOGS).read(Map.of("id", buildId));
    }

    private void createTestDataAndInitRequests() {
        // Create a test user
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        // Initialize requests with the created user
        checkedRequests = new CheckedRequests(authSpec(testData.getUser()));
        // Create a test project
        checkedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        // Create a test build type
        checkedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        // Initialize mock requests
        checkedMockRequest = new CheckedRequests(mockSpec());
    }

    private void assertBuildHappyEnding(Build build) {
        softAssert.assertEquals(build.getState(), BUILD_STATE_FINISHED);
        softAssert.assertEquals(build.getStatus(), BUILD_STATUS_SUCCESS);
    }
}
