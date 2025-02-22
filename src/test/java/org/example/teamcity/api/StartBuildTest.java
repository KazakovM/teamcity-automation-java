package org.example.teamcity.api;

import org.apache.http.HttpStatus;
import io.qameta.allure.Feature;
import org.example.teamcity.api.models.Build;
import org.example.teamcity.api.requests.checked.CheckedBase;
import org.example.teamcity.common.WireMock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.example.teamcity.api.enums.Endpoint.BUILD_QUEUE;
import static org.example.teamcity.api.spec.Specifications.mockSpec;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {
    @BeforeMethod
    public void setupWireMockServer() {
        var fakeBuild = Build.builder()
                .state("finished")
                .status("SUCCESS")
                .build();

        WireMock.setupServer(post(BUILD_QUEUE.getUrl()), HttpStatus.SC_OK, fakeBuild);
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }

    @Test(description = "User should be able to start build (with WireMock)", groups = {"Regression"})
    public void userStartsBuildWithWireMockTest() {
        var checkedBuildQueueRequest = new CheckedBase<Build>(mockSpec(), BUILD_QUEUE);

        var build = checkedBuildQueueRequest.create(Build.builder()
                .buildType(testData.getBuildType())
                .build());

        softAssert.assertEquals(build.getState(), "finished");
        softAssert.assertEquals(build.getStatus(), "SUCCESS");
    }
}
