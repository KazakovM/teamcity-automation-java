package org.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.example.teamcity.BaseTest;
import org.example.teamcity.api.enums.Endpoint;
import org.example.teamcity.api.models.User;
import org.example.teamcity.ui.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

import static org.example.teamcity.api.config.Config.getProperty;

public class BaseUiTest extends BaseTest {
    @BeforeSuite(alwaysRun = true)
    public void setupUiTest() {
        Configuration.browser = getProperty("browser");
        Configuration.baseUrl = "http://" + getProperty("host");
        Configuration.remote = getProperty("remote");
        Configuration.browserSize = getProperty("browserSize");
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of("enableVNC", true, "enableLog", true));

        // НЕ ПИСАТЬ UI ТЕСТЫ С ЛОКАЛЬНЫМ БРАУЗЕРОМ
        // СПЕРВА НАСТРОИТЬ CICD И ПОТОМ ЗАПУСКАТЬ ТЕСТЫ
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected void loginAs(User user) {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        LoginPage.open().login(testData.getUser());
    }
}
