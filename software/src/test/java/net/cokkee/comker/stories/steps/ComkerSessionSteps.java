package net.cokkee.comker.stories.steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.stories.ComkerRestAssuredConfig;
import net.cokkee.comker.util.ComkerDataUtil;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author drupalex
 */
@Component("sessionSteps")
public class ComkerSessionSteps {

    @Autowired
    protected ComkerRestAssuredConfig rac;

    @BeforeStories
    public void init() {
        
    }

    @AfterStories
    public void shutdown() {
        
    }

    private ComkerRoleDTO roleObject = new ComkerRoleDTO();

    private Response response;

	@When("I request the session information")
	public void when_i_request_the_session_information() {
		response = RestAssured.
                given().
                    contentType("application/json").
                expect().
                    statusCode(200).
                when().
                    get(rac.absurl("session/information"));
	}

	@Then("a session information object should be return")
	public void then_a_session_information_object_should_be_return() {
        Assert.assertTrue(response.getStatusCode() == 200);
        
		String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);

        Assert.assertEquals(0, jsonPath.getInt("checkpoint"));
	}
}
