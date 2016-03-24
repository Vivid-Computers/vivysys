package vivyclient.exception;

import javax.swing.JComponent;

public class UserInputException extends Exception {
   private JComponent relatedControl;

   public UserInputException(String message, JComponent relatedControl) {
      super(message);
      this.relatedControl = relatedControl;
   }

   public JComponent getRelatedControl() {
      return this.relatedControl;
   }
}
