package net.cokkee.comker.stories.specs;

import java.util.List;
import net.cokkee.comker.stories.ComkerAbstractScenario;
import net.cokkee.comker.stories.steps.ComkerSessionSteps;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

/**
 *
 * @author drupalex
 */
public class ComkerSessionSpecs extends ComkerAbstractScenario {
    
    @Override
	public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new ComkerSessionSteps());
	}

    @Override
	protected List<String> storyPaths() {
        return new StoryFinder().findPaths(
                CodeLocations.codeLocationFromClass(getClass()),
                "net/cokkee/comker/stories/ComkerSessionScenario*.story", "");
	}
}
