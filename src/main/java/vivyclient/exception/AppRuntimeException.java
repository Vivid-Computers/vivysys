package vivyclient.exception;

public class AppRuntimeException extends RuntimeException {
   public AppRuntimeException() {
   }

   public AppRuntimeException(String msg) {
      super(msg);
   }
}
