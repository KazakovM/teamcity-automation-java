package org.example.teamcity.ui;

import org.example.teamcity.ui.pages.setup.FirstStartPage;
import org.testng.annotations.Test;

public class SetupServerTest extends BaseUiTest {
    @Test(groups = {"Setup"})
    public void setupTeamCityServerTest() {
        FirstStartPage.open().setupFirstStart();
    }
}
