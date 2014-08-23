package net.cokkee.comker.stories.steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.util.ComkerDataUtil;

import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeStories;
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
@Component("systemRoleSteps")
public class ComkerManageRoleSteps extends ComkerAbstractSteps {

    @BeforeStories
    public void init() {
    }

    @AfterStories
    public void shutdown() {
    }

    private ComkerRoleDTO roleObject = new ComkerRoleDTO();

    private Response response;

    @Given("a role code:<code>")
    @Alias("a role code:$code")
	public void given_a_role_code(@Named("code") String code) {
		roleObject.setCode(code);
	}

	@Given("a role object with code:'<code>', name:'<name>', description:'<description>'")
    @Alias("a role object with code:'$code', name:'$name', description:'$description'")
	public void given_a_role_object(@Named("code") String code,
            @Named("name") String name, @Named("description") String description) {
		roleObject.setCode(code);
        roleObject.setName(name);
        roleObject.setDescription(description);
	}

    @When("I list all of role objects")
	public void when_i_list_all_of_role_objects() {
		response = RestAssured.
                given().
                    contentType("application/json").
                expect().
                when().
                    get(serviceUrl("role/find")).
                then().
                    extract().path("total");
	}
    
	@When("I insert role object to database")
	public void when_i_insert_role_object_to_database() {
		response = RestAssured.
                given().
                    contentType("application/json").
                    body(ComkerDataUtil.convertObjectToJson(roleObject)).
                when().
                    post(serviceUrl("role/crud"));
	}
    
    @When("I update role object to database")
	public void when_i_update_role_object_to_database() {
		response = RestAssured.
                given().
                    contentType("application/json").
                    body(ComkerDataUtil.convertObjectToJson(roleObject)).
                when().
                    put(serviceUrl("role/crud/" + roleObject.getId()));
	}

    @When("I delete role object from database")
	public void when_i_delete_role_object_from_database() {
		response = RestAssured.
                given().
                    contentType("application/json").
                when().
                    delete(serviceUrl("role/crud/" + roleObject.getId()));
	}

	@Then("role object should be insert successful")
	public void thenRoleObjectShouldBeInsertSuccessful() {
        Assert.assertTrue(response.getStatusCode() == 200);
        
		String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);

        Assert.assertTrue(ComkerDataUtil.verifyUUID(jsonPath.getString("id")));
        Assert.assertEquals(roleObject.getName(), jsonPath.getString("name"));
        Assert.assertEquals(roleObject.getCode(), jsonPath.getString("code"));
        Assert.assertEquals(roleObject.getDescription(), jsonPath.getString("description"));
	}

    @Then("role object should not be inserted")
	public void thenRoleObjectShouldNotBeInserted() {
        Assert.assertTrue(response.getStatusCode() == ComkerValidationFailedException.CODE);

		String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        JsonPath jsonPath = new JsonPath(responseBody);

        Assert.assertTrue(ComkerValidationFailedException.CODE == jsonPath.getInt("code"));
        //Assert.assertEquals(roleObject.getDescription(), jsonPath.getString("description"));
	}
}
