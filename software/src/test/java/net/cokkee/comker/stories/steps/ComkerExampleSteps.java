package net.cokkee.comker.stories.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.stereotype.Component;

/**
 *
 * @author drupalex
 */
@Component("exampleSteps")
public class ComkerExampleSteps extends ComkerAbstractSteps {

    int x;

	@Given("a variable x with value <x_value>")
	public void givenXValue(@Named("x_value") int value) {
		x = value;
	}

	@When("I multiply x by <y_time>")
	public void whenImultiplyXBy(@Named("y_time") int value) {
		x = x * value;
	}

	@Then("x should equal <result>")
	public void thenXshouldBe(@Named("result") int value) {
		if (value != x)
			throw new RuntimeException("x is " + x + ", but should be " + value);
	}
}
