package vivyclient.exception;

public class StaleModelException extends Exception {
   private String objectType;

   public StaleModelException(String objectType, String message) {
      super(message);
      this.objectType = objectType;
   }

   public String getObjectType() {
      return this.objectType;
   }

   public void setObjectType(String objectType) {
      this.objectType = objectType;
   }
}
