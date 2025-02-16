package org.example.teamcity;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

public class BaseTest {
    protected SoftAssert softAssert;
    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softAssert = new SoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softAssert.assertAll();
    }
}
