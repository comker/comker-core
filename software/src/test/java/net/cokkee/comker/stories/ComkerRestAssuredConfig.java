package net.cokkee.comker.stories;

/**
 *
 * @author drupalex
 */
public class ComkerRestAssuredConfig {

    private String serverAddress = "";

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String absurl(String path) {
        return serverAddress + "/" + path;
    }
}
