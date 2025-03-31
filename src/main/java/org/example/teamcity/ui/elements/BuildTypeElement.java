package org.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;

public class BuildTypeElement extends BasePageElement{
    private SelenideElement name;

    public BuildTypeElement(SelenideElement element) {
        super(element);
        this.name = find("span[class*='MiddleEllipsis']");
    }

    public String getName() {
        return name.getText();
    }
}
