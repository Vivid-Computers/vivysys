package vivyclient.error;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import vivyclient.data.TransactionContainer;
import vivyclient.util.Settings;

public class SQLLog {
   public static synchronized void writeQuery(String query, TransactionContainer transaction) {
      if(Settings.getLogSQLReads()) {
         if(transaction != null) {
            write("[" + transaction.getIdentifier() + "]  " + query);
         } else {
            write("[NA]  " + query);
         }
      }

   }

   public static synchronized void writeUpdate(String updateStatement, TransactionContainer transaction) {
      if(Settings.getLogSQLWrites()) {
         if(transaction != null) {
            write("[" + transaction.getIdentifier() + "]  " + updateStatement);
         } else {
            write("[NA]  " + updateStatement);
         }
      }

   }

   public static void initialise() {
      if(Settings.getLogSQLWrites() || Settings.getLogSQLReads()) {
         String initString = "\n\n********************************************************************\nSQL Logging " + (Settings.getLogSQLReads()?"\"Reads\" ":"") + (Settings.getLogSQLWrites()?"\"Writes\" ":"") + "\nVIVYCLIENT Version \"" + "3.0.3b" + "\"" + "\nBeginning at " + DateFormat.getDateTimeInstance(0, 0).format(Calendar.getInstance().getTime()) + "\n";
         write(initString);
      }

   }

   private static void write(String sql) {
      FileOutputStream fileOutputStream = null;

      try {
         fileOutputStream = new FileOutputStream(Settings.getSqlLogFilePath(), true);
         PrintWriter ex = new PrintWriter(fileOutputStream);
         ex.write(sql);
         ex.println();
         ex.flush();
      } catch (Exception var12) {
         ;
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
