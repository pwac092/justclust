/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.parsing.gml;

/**
 *
 * @author wuaz008
 */
public class KeyValue {

    /**
     *
     */
    public String key;
    /**
     *
     */
    public Object value;

    /**
     * Creates a new KeyValue object.
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValue [key=" + key + ", value=" + value + "]";
    }
}
