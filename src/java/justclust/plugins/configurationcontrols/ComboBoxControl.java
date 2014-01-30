/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.configurationcontrols;

import java.util.ArrayList;

/**
 *
 * @author wuaz008
 */
public class ComboBoxControl implements PluginConfigurationControl {
    
    public String label;
    public ArrayList<String> options;
    public int selectedOptionIndex;
}
