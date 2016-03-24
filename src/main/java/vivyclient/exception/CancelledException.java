package vivyclient.exception;

public class CancelledException extends Exception {
   public CancelledException() {
   }

   public CancelledException(String message) {
      super(message);
   }
}
