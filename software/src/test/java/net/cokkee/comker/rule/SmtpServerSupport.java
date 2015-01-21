package net.cokkee.comker.rule;

import javax.annotation.Nonnull;

/**
 * SmtpServerSupport - Interface usually implemented by the JUnit test class.
 *
 * @author Sébastien Lesaint
 */
public interface SmtpServerSupport {

    /**
     * the SMTP port.
     * 
     * @return 
     */
    int getPort();

    /**
     * The hostname (for example 'localhost')
     *
     * @return a {@link String}
     */
    @Nonnull
    String getHostname();
}
