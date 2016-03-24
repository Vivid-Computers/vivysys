package vivyclient.error;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import vivyclient.util.Settings;

public class ErrorWriter {
   public static synchronized boolean writeException(Exception e) {
      File outputFile = new File(Settings.getErrorLogFilePath());
      boolean successful = false;
      FileOutputStream fileOutputStream = null;

      try {
         fileOutputStream = new FileOutputStream(outputFile, true);
         PrintWriter ex = new PrintWriter(fileOutputStream);
         e.printStackTrace(ex);
         ex.println();
         ex.flush();
         successful = true;
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         try {
            if(fileOutputStream != null) {
               fileOutputStream.close();
            }
         } catch (Exception var13) {
            ;
         }

      }

      return successful;
   }

   public static void initialise() {
      if(Settings.getLogErrors()) {
         String initString = "\n\n********************************************************************\nLogging \"Errors\"\nVIVYCLIENT Version \"3.0.3b\"\nBeginning at " + DateFormat.getDateTimeInstance(0, 0).format(Calendar.getInstance().getTime()) + "\n";
         write(initString);
      }

   }

   private static void write(String message) {
      FileOutputStream fileOutputStream = null;

      try {
         fileOutputStream = new FileOutputStream(Settings.getErrorLogFilePath(), true);
         PrintWriter ex = new PrintWriter(fileOutputStream);
         ex.write(message);
         ex.println();
         ex.flush();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         try {
            if(fileOutputStream != null) {
               fileOutputStream.close();
            }
         } catch (Exception var11) {
            ;
         }

      }

   }
}
