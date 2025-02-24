package org.example.teamcity.common.provider;

import org.testng.annotations.DataProvider;

public class BooleanProvider {

    @DataProvider(name = "booleanProvider")
    public Object[][] booleanProvider() {
        return new Object[][]{
                {true},
                {false}
        };
    }
}
