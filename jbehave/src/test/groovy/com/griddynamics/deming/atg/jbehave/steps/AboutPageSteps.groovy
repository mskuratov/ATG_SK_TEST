package com.griddynamics.deming.atg.jbehave.steps

import com.griddynamics.deming.atg.jbehave.Settings
import com.griddynamics.deming.atg.jbehave.pages.Pages
import org.jbehave.core.annotations.Given
import org.jbehave.core.annotations.Then
import org.jbehave.core.annotations.When

class AboutPageSteps {

    final Pages pages

    AboutPageSteps(Pages pages) {
        this.pages = pages
    }

    @Given("crs site")
    def givenCrsSite() {
        // nothing
    }

    @When("I open '\$pagePath'")
    def whenIOpenPage(String pagePath) throws IOException {
        pages.aboutPage().open(pagePath)
    }

    @Then("navigation panel contains '\$title' link to '\$link'")
    def thenNavigationPanelContainsLinkWithTitle(String title, String expectedLink) {
        def actualLink = pages.aboutPage().findLink(title)

        assert actualLink != null

        if (actualLink.contains(";jsessionid")) {
            actualLink = actualLink.substring(0, actualLink.indexOf(";jsessionid"));
        }

        if (!expectedLink.startsWith("http")) {
            expectedLink = Settings.crsUrl + expectedLink
        }

        assert actualLink.equals(expectedLink)
    }
}
