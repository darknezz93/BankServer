package clientApp.helper;


import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;


/**
 * Created by adam on 16.01.17.
 */
public class AmountTextField extends TextField {
    public AmountTextField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                int length = AmountTextField.super.getText().length();
                if (!(ch >= '0' && ch <= '9') || length > 9) {
                    t.consume();
                }
            }
        });
    }
}