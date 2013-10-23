package com.griddynamics.deming.atg.jbehave.pages

import org.jbehave.web.selenium.WebDriverProvider

class Pages {

    private final WebDriverProvider driverProvider
    private AboutPage aboutPage

    Pages(WebDriverProvider driverProvider) {
        this.driverProvider = driverProvider
    }

    public AboutPage aboutPage() {
        if (aboutPage == null) {
            aboutPage = new AboutPage(driverProvider)
        }
        return aboutPage
    }
}
