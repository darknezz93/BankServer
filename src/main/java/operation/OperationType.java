package operation;

/**
 * Created by adam on 06.01.17.
 */
public enum OperationType {

    /**
     * Wpłata własna
     */
    Payment,

    /**
     * Wypłata własna
     */
    Withdraw,

    /**
     * Przelew zewnętrzny
     */
    ExternalTransfer,

    /**
     * Przelew wewnętrzny
     */
    InternalTransfer,


}
