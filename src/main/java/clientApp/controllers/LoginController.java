package clientApp.controllers;

import clientApp.helper.ClientAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;
import service.UserService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Kontroler odpowiedzialny za rejestrację i logowanie użytkownika
 */
public class LoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    /**
     * Metoda wywoływana po naciśnięciu przycisku logowania
     * @throws Exception
     */
    @FXML
    public void listenLoginButton() throws Exception {
        resetLabels();
        if(!validateUserNameAndPassword()) {
            return;
        }
        UserService webService = getUserService();
        String userNameAndPassword = userNameTextField.getText() + ":" + passwordTextField.getText();
        byte[] bytesEncoded = Base64.encodeBase64(userNameAndPassword.getBytes());
        String encodedUserNameAndPassword = new String(bytesEncoded);
        boolean result = false;
        try {
            result = webService.login(encodedUserNameAndPassword);
        } catch(Exception e) {
            errorLabel.setText("Błąd serwera");
        }
        if(result) {
            successLabel.setText("Pomyślnie zalogowano do systemu.");
            ClientAuth.setEncodedAuth(encodedUserNameAndPassword);
            openNewWindow("src/main/java/clientApp/layouts/menu.fxml", "Klient konta bankowego - menu");
        } else {
            errorLabel.setText("Niepoprawna nazwa użytkownika lub hasło");
        }
    }

    /**
     * Metoda wywoływana po naciśnięciu przycisku rejestracji
     * @throws Exception
     */
    @FXML
    public void listenRegisterButton() throws Exception {
        resetLabels();
        if(!validateUserNameAndPassword()) {
            return;
        }
        UserService webService = getUserService();
        String userNameAndPassword = userNameTextField.getText() + ":" + passwordTextField.getText();
        byte[] bytesEncoded = Base64.encodeBase64(userNameAndPassword.getBytes());
        String encodedUserNameAndPassword = new String(bytesEncoded);
        boolean result = webService.registerUser(encodedUserNameAndPassword);
        if(result) {
            successLabel.setText("Pomyślnie zarejestrowano użytkownika. Zaloguj się.");
        } else {
            errorLabel.setText("Wystąpił błąd lub użytkownik juz istnieje.");
        }
    }

    /**
     * Wstępna walidacja pól userName i password
     * @return
     */
    public boolean validateUserNameAndPassword() {
        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();
        if(userName.equals("") || password.equals("")) {
            errorLabel.setText("Nazwa użytkownika oraz hasło nie mogą być puste");
            return false;
        }
        errorLabel.setText("");
        return true;
    }

    /**
     * Metoda zwaracjaca userService
     * @return
     * @throws MalformedURLException
     */
    private UserService getUserService() throws MalformedURLException {
        URL url = new URL("http://localhost:8000/user?wsdl");
        QName qname = new QName("http://service/", "UserServiceImplService");
        Service service = Service.create(url, qname);
        UserService webService = service.getPort(UserService.class);
        return webService;
    }

    /**
     * Metoda otwierająca nowe okno
     * @param fxmlFilePath - scieżka do pliku fxml opisującego nowe okno
     * @param windowTitle - tytuł okna
     * @throws IOException
     */
    private void openNewWindow(String fxmlFilePath, String windowTitle) throws IOException {
        URL url = new File(fxmlFilePath).toURL();
        Parent root1 = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    /**
     * Metoda resetująca zawartość labelek z błędami lub informacjami dla użytkownika
     */
    private void resetLabels() {
        successLabel.setText("");
        errorLabel.setText("");
    }

}
