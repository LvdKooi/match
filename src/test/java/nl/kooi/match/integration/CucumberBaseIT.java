package nl.kooi.match.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import nl.kooi.match.TestcontainersBase;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberBaseIT extends TestcontainersBase {

}