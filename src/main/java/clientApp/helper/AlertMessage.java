package clientApp.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * Created by adam on 16.01.17.
 */

/**
 * Klasa tworząca okienko z alertami
 */
public class AlertMessage {

    public static void infoBox(String infoMessage, String titleBar) {
        createAlertBox(infoMessage, titleBar, AlertType.INFORMATION);
    }

    public static void errorBox(String infoMessage, String titleBar) {
        createAlertBox(infoMessage, titleBar, AlertType.ERROR);
    }

    private static void createAlertBox(String infoMessage, String titleBar, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titleBar);
        alert.setContentText(infoMessage);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
