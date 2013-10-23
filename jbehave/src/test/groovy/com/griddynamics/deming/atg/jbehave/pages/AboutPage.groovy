package com.griddynamics.deming.atg.jbehave.pages

import com.griddynamics.deming.atg.jbehave.Settings
import org.jbehave.web.selenium.WebDriverPage
import org.jbehave.web.selenium.WebDriverProvider
import org.openqa.selenium.By

import java.util.concurrent.TimeUnit

class AboutPage extends WebDriverPage {

    AboutPage(WebDriverProvider driverProvider) {
        super(driverProvider)
    }

    def open(String pagePath) {
        get("${Settings.crsUrl}/${pagePath}");
        manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    def findLink(String linkText) {
        return findElement(By.linkText(linkText)).getAttribute("href")
    }
}
