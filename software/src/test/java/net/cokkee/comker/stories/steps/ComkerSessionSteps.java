package net.cokkee.comker.stories.steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 *
 * @author drupalex
 */
@Component("sessionSteps")
public class ComkerSessionSteps extends ComkerAbstractSteps {

    private Response response;

    @Given("<username> and <password>")
    @Alias("$username and $password")
    public void given_username_and_password(@Named("username") String username,
            @Named("password") String password) {
        response = RestAssured.
                given().
                    auth().form(username, password).
                when().
                    get("/secured/basic");
        
    }

	@When("I request the session information")
	public void when_i_request_the_session_information() {
		response = RestAssured.
                given().
                    contentType("application/json").
                when().
                    get(serviceUrl("session/information"));
	}

	@Then("a session information object should be return")
	public void then_a_session_information_object_should_be_return() {
        Assert.assertTrue(response.getStatusCode() == 200);
        
		String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);

        Assert.assertEquals(0, jsonPath.getInt("checkpoint"));
	}
}
