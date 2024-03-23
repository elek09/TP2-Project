package simulator.gui;

import javax.swing.*;
import java.awt.*;

public class ViewUtils {

    public static ChangeRegionsDialog getWindow(Component component) {
        return (ChangeRegionsDialog) SwingUtilities.getWindowAncestor(component);
    }

    public static void quit(Component component) {
        Window window = getWindow(component);
        if (window != null) {
            window.dispose();
        }
    }

    public static void showErrorMsg(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
