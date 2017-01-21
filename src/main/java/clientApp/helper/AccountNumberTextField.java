package clientApp.helper;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Created by adam on 17.01.17.
 */

/**
 * Textfield dla numeru rachunku
 */
public class AccountNumberTextField extends TextField {

    public AccountNumberTextField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                int length = AccountNumberTextField.super.getText().length();
                if (!(ch >= '0' && ch <= '9') || length > 26) {
                    t.consume();
                }
            }
        });
    }
}
