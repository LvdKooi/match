package nl.kooi.match.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "nl.kooi.match.integration",
        plugin = "pretty")
public class CucumberTest {
}