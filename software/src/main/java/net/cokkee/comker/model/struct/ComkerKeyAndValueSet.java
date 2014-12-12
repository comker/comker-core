package net.cokkee.comker.model.struct;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerKeyAndValueSet {

    public ComkerKeyAndValueSet() {
    }

    public ComkerKeyAndValueSet(String key, Collection<String> valueList) {
        this.key = key;
        this.values = valueList.toArray(new String[0]);
    }

    public ComkerKeyAndValueSet(String key, String[] valueArray) {
        this.key = key;
        this.values = valueArray;
    }

    private String key;
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
