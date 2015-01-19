package net.cokkee.comker.rule;

import java.io.FileNotFoundException;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.springframework.util.Log4jConfigurer;

/**
 *
 * @author drupalex
 */
public class Log4jConfigRule implements TestRule {

    @Override
    public Statement apply(Statement stmnt, Description d) {
        try {
            Log4jConfigurer.initLogging("file:src/main/webapp/WEB-INF/log4j.xml");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
        return stmnt;
    }
    
}
