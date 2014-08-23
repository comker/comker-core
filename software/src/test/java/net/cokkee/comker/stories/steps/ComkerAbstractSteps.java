package net.cokkee.comker.stories.steps;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractSteps {

    private String serviceUrl = "http://localhost:7777/comker-app/ws/comker/api";

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String serviceUrl(String path) {
        StringBuilder url = new StringBuilder();
        if(serviceUrl != null) {
            url.append(serviceUrl);
            if (!serviceUrl.endsWith("/")) {
                url.append("/");
            }
        }
        url.append(path);
        return url.toString();
    }
}
