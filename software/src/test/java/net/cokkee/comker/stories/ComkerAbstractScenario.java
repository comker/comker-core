package net.cokkee.comker.stories;

import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractScenario extends JUnitStories {

    public ComkerAbstractScenario() {
        configuredEmbedder().embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(true).doVerboseFailures(true);
    }

    private ConfigurableApplicationContext context;

    private ApplicationContext context() {
        if (context == null) {
            context = new SpringApplicationContextFactory(
                    "classpath:net/cokkee/comker/stories/"
                    + this.getClass().getSimpleName() + ".xml")
                .createApplicationContext();
        }
        return context;
    }

    // Here we specify the configuration, starting from default
    // MostUsefulConfiguration, and changing only what is needed
    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
            .useStoryReporterBuilder(new StoryReporterBuilder()
                .withCodeLocation(CodeLocations.codeLocationFromClass(getClass()))
                .withDefaultFormats()
                .withFormats(Format.CONSOLE, Format.TXT, Format.HTML, Format.XML));
    }

    @Override
	public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), context());
	}

    @Override
	protected List<String> storyPaths() {
        return new StoryFinder().findPaths(
                CodeLocations.codeLocationFromClass(getClass()),
                "net/cokkee/comker/stories/" +
                    this.getClass().getSimpleName() + "*.story", "");
	}
}
