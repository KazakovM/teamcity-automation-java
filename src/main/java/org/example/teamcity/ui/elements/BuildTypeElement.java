package org.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;

public class BuildTypeElement extends BasePageElement{
    private SelenideElement name;

    public BuildTypeElement(SelenideElement element) {
        super(element);
        this.name = find(".BuildTypeLine__link--MF");
    }

    public String getName() {
        return name.getText();
    }
}
