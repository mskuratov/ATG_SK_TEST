package com.griddynamics.deming.atg.jbehave.steps

import org.jbehave.core.annotations.BeforeStories

class GlobalSteps {

    @BeforeStories
    def beforeStories() {
        System.setProperty("browser", "HTMLUNIT")
        System.setProperty("webdriver.htmlunit.javascriptEnabled", "false")
    }
}
