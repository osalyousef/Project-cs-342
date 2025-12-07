package utils;

import javax.swing.*;

public class MessageBox {

    public static void showError(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Warning",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Info",
            JOptionPane.PLAIN_MESSAGE
        );
    }
}
