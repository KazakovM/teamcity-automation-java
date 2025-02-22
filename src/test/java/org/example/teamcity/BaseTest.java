package org.example.teamcity;

import org.example.teamcity.api.generators.TestDataStorage;
import org.example.teamcity.api.models.TestData;
import org.example.teamcity.api.requests.CheckedRequests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.example.teamcity.api.spec.Specifications.superUserSpec;

public class BaseTest {
    protected SoftAssert softAssert;
    protected CheckedRequests superUserCheckedRequests = new CheckedRequests(superUserSpec());
    protected TestData testData = generate();

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softAssert = new SoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softAssert.assertAll();
        TestDataStorage.getTestDataStorage().deleteCreatedEntities();
    }
}
