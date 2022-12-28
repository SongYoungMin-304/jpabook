package jpashop.jpabook.exception;

public class NotEnoughStockEception extends RuntimeException{

    public NotEnoughStockEception() {
        super();
    }

    public NotEnoughStockEception(String message) {
        super(message);
    }

    public NotEnoughStockEception(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockEception(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockEception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
