package operation;

/**
 * Created by adam on 06.01.17.
 */

/**
 * Ty wyliczeniowy określający rodzaj operacji
 */
public enum OperationType {

    /**
     * Wpłata własna
     */
    Payment,

    /**
     * Wypłata własna
     */
    Withdrawal,

    /**
     * Przelew zewnętrzny
     */
    ExternalTransfer,

    /**
     * Przelew wewnętrzny
     */
    InternalTransfer,


}
