package vivyclient.util;

import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import vivyclient.error.ErrorWriter;
import vivyclient.exception.StaleModelException;
import vivyclient.exception.UserInputException;

public class DialogueUtil {
   public static void handleUserInputException(UserInputException e, String contextMessage, String title, Component parent) {
      handleException(e, contextMessage, title, false, parent);
      if(e.getRelatedControl() != null) {
         e.getRelatedControl().grabFocus();
      }

   }

   public static void handleException(Exception e, String contextMessage, String title, boolean attemptLog, Component parent) {
      String message = "<b>" + contextMessage + "</b>";
      if(e.getMessage() != null) {
         String exceptionMessage = e.getMessage().replace('\n', ' ');
         message = message + "<br><i>" + exceptionMessage + "</i>";
      }

      if(attemptLog && ErrorWriter.writeException(e)) {
         message = message + "<br><br>The error was logged.";
      } else {
         message = message + "<br><br><b>The error was not logged.</b>";
      }

      message = "<html>" + message + "</html>";
      Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(parent, message, title, 0);
   }

   public static int getActionForStaleModelException(StaleModelException e, Component parent) {
      String message = "<b>" + e.getObjectType() + " could not be saved:</b><br>" + e.getMessage();
      message = message + "<br><br>Do you wish to refresh the " + e.getObjectType() + " from the database? (Your changes will be lost)";
      message = "<html>" + message + "</html>";
      return JOptionPane.showConfirmDialog(parent, message, "Error Saving " + e.getObjectType(), 0, 0);
   }

   public static boolean confirmForDelete(String entityName, Component parent) {
      String message = "Delete " + entityName + "?";
      return 0 == JOptionPane.showConfirmDialog(parent, message, "Confirm Delete", 0, 3);
   }
}
