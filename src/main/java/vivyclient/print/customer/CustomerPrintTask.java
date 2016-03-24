package vivyclient.print.customer;

import vivyclient.print.PagePrintTask;
import vivyclient.print.PrintProgress;
import vivyclient.print.table.PrintableTable;

public class CustomerPrintTask extends PagePrintTask {
   private PrintableTable table;
   private PrintProgress printProgress;

   public CustomerPrintTask(String taskCode) {
      super(taskCode);
   }

   public PrintableTable getTable() {
      return this.table;
   }

   public void setTable(PrintableTable table) {
      this.table = table;
   }

   public PrintProgress getPrintProgress() {
      return this.printProgress;
   }

   public void setPrintProgress(PrintProgress printProgress) {
      this.printProgress = printProgress;
   }
}
