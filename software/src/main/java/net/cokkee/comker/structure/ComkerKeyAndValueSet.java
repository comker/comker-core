package net.cokkee.comker.structure;

import java.util.HashSet;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerKeyAndValueSet {

    public ComkerKeyAndValueSet() {
    }

    public ComkerKeyAndValueSet(String key, HashSet<String> valueSet) {
        this.key = key;
        this.valueSet = valueSet;
    }

    private String key;
    private HashSet<String> valueSet;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashSet<String> getValueSet() {
        return valueSet;
    }

    public void setValueSet(HashSet<String> valueSet) {
        this.valueSet = valueSet;
    }
}
