package net.cokkee.comker.rule;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.server.SMTPServer;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

/**
 * SmtpServerRule - a TestRule wrapping a Wiser instance (a SMTP server in Java)
 * started and stoped right before and after each test.
 * <br/>
 * SmtpServerRule exposes the same methods as the {@link Wiser} instance by
 * delegating the implementation to the instance. These methods, however, can
 * not be used outside a JUnit statement (otherwise a
 * {@link IllegalStateException} is raised).
 * <br/>
 * The {@link Wiser} instance can be directly retrieved but also only from
 * inside a JUnit statement.
 *
 * @author Sébastien Lesaint
 */
public class SmtpServerRule extends ExternalResource implements TestRule {

    private final SmtpServerSupport SmtpServerSupport;
    private Wiser wiser;

    public SmtpServerRule(@Nonnull SmtpServerSupport SmtpServerSupport) {
        this.SmtpServerSupport = Preconditions.checkNotNull(SmtpServerSupport);
    }

    @Override
    protected void before() throws Throwable {
        this.wiser = new Wiser();
        this.wiser.setPort(SmtpServerSupport.getPort());
        this.wiser.setHostname(SmtpServerSupport.getHostname());
        this.wiser.start();
    }

    @Override
    protected void after() {
        this.wiser.stop();
    }

    /**
     * @return the inner {@link Wiser} instance
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     */
    @Nonnull
    public Wiser getWiser() {
        checkState("getWiser()");
        return this.wiser;
    }

    /**
     * @return a {@link List} of {@link WiserMessage}
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     * @see {@link Wiser#getMessages()}
     */
    @Nonnull
    public List<WiserMessage> getMessages() {
        checkState("getWiser()");
        return wiser.getMessages();
    }

    /**
     * @return 
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     * @see {@link Wiser#getServer()}
     */
    @Nonnull
    public SMTPServer getServer() {
        checkState("getServer()");
        return wiser.getServer();
    }

    /**
     * @param from
     * @param recipient
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     * @see {@link Wiser#accept(String, String)}
     */
    public boolean accept(String from, String recipient) {
        checkState("accept(String, String)");
        return wiser.accept(from, recipient);
    }

    /**
     * @param from
     * @param recipient
     * @param data
     * @throws org.subethamail.smtp.TooMuchDataException
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     * @see {@link Wiser#deliver(String, String, java.io.InputStream)}
     */
    public void deliver(String from, String recipient, InputStream data) 
            throws TooMuchDataException, IOException {
        checkState("deliver(String, String, InputStream)");
        wiser.deliver(from, recipient, data);
    }

    /**
     * @param out
     * @throws IllegalStateException is method is not called from a JUnit
     * statement
     * @see {@link Wiser#dumpMessages(java.io.PrintStream)}
     */
    public void dumpMessages(PrintStream out) throws MessagingException {
        checkState("dumpMessages(PrintStream)");
        wiser.dumpMessages(out);
    }

    private void checkState(String method) {
        Preconditions.checkState(this.wiser != null, "%s must not be called outside of a JUnit statement", method);
    }
}
