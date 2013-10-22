package com.griddynamics.deming.atg.jbehave.steps;

import com.griddynamics.deming.atg.jbehave.Settings;
import com.griddynamics.deming.atg.jbehave.utils.WebClient;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigationPanelSteps {

    public String pageUrl;
    public String pageContent;

    @Given("crs site")
    public void given() {
        pageUrl = Settings.crsUrl;
    }

    @When("I open '$pagePath'")
    public void when(String pagePath) throws IOException {
        this.pageUrl += pagePath;
        this.pageContent = WebClient.downloadContent(new URL(this.pageUrl));
    }

    @Then("navigation panel contains '$title' link to '$link'")
    public void then(String title, String link) {
        Pattern pattern = Pattern.compile("<a title=\"" + title + "\" href=\"([^\"]+)\">");
        Matcher matcher = pattern.matcher(this.pageContent);

        if (matcher.find()) {
            String href = matcher.group(1);

            if (href.contains(";jsessionid")) {
                href = href.substring(0, href.indexOf(";jsessionid"));
            }

            if (!link.equals(href)) {
                throw new RuntimeException("Founded href (" + href + ") not equal to link: " + link);
            }
        }
    }
}
