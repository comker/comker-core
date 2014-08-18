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
@Component("systemRoleSteps")
public class ComkerManageRoleSteps {

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

	@Given("a role object with code:'<code>', name:'<name>', description:'<description>'")
    @Alias("a role object with code:'$code', name:'$name', description:'$description'")
	public void givenARoleObject(@Named("code") String code,
            @Named("name") String name, @Named("description") String description) {
		roleObject.setCode(code);
        roleObject.setName(name);
        roleObject.setDescription(description);
	}

	@When("I insert role object to database")
	public void whenIInsertRoleObjectToDatabase() {
		response = RestAssured.
                given().
                    contentType("application/json").
                    body(ComkerDataUtil.convertObjectToJson(roleObject)).
                expect().
                    statusCode(200).
                when().
                    post(rac.absurl("role/crud"));
	}

	@Then("role object should be insert successful")
	public void thenRoleObjectShouldBe() {
        Assert.assertTrue(response.getStatusCode() == 200);
        
		String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);

        //Assert.assertTrue(ComkerDataUtil.verifyUUID(jsonPath.getString("id")));
        Assert.assertEquals(roleObject.getName(), jsonPath.getString("name"));
        Assert.assertEquals(roleObject.getCode(), jsonPath.getString("code"));
        Assert.assertEquals(roleObject.getDescription(), jsonPath.getString("description"));
	}
}
