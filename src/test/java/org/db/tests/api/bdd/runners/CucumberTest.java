package org.db.tests.api.bdd.runners;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/api",
		glue = {"org.db.tests.api.bdd.stepsdefs"},
		junit = "--step-notifications"
		)
public class CucumberTest {

}
