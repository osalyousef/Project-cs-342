package exceptions;

import javax.swing.*;

public class MessageBox {
    public static void showError(String msg) {// static so it can be called without obj
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
