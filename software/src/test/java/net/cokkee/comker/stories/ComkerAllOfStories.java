package net.cokkee.comker.stories;

import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;

/**
 *
 * @author drupalex
 */
public class ComkerAllOfStories extends ComkerAbstractScenario {

    @Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(
                CodeLocations.codeLocationFromClass(getClass()), 
                "net/cokkee/comker/stories/**/*.story", "");
	}
}
