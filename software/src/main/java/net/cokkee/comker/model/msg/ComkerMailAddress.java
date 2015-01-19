package net.cokkee.comker.model.msg;

/**
 *
 * @author drupalex
 */
public class ComkerMailAddress {
    
    private String from;
    private String[] toAddrs;
    private String[] ccAddrs;
    private String[] bccAddrs;

    public String getFrom() {
        return from;
    }

    public ComkerMailAddress setFrom(String from) {
        this.from = from;
        return this;
    }

    public String[] getTo() {
        return toAddrs;
    }

    public ComkerMailAddress setTo(String toAddr) {
        this.toAddrs = new String[] {toAddr};
        return this;
    }
    
    public ComkerMailAddress setTo(String[] toAddrs) {
        this.toAddrs = toAddrs;
        return this;
    }

    public String[] getCc() {
        return ccAddrs;
    }

    public ComkerMailAddress setCc(String cc) {
        this.ccAddrs = new String[] {cc};
        return this;
    }
    
    public ComkerMailAddress setCc(String[] cc) {
        this.ccAddrs = cc;
        return this;
    }

    public String[] getBcc() {
        return bccAddrs;
    }

    public ComkerMailAddress setBcc(String bcc) {
        this.bccAddrs = new String[] {bcc};
        return this;
    }
    
    public ComkerMailAddress setBcc(String[] bcc) {
        this.bccAddrs = bcc;
        return this;
    }
}
