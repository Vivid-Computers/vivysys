package vivyclient.print.sale;

import vivyclient.model.Sale;
import vivyclient.print.PagePrintTask;
import vivyclient.print.PrintProgress;
import vivyclient.print.table.PrintableTable;

public class InvoicePrintTask extends PagePrintTask {
   public static final String PRINT_INVOICE_INFO_TASK_CODE = "printInvoiceInfo";
   public static final String PRINT_CUSTOMER_DETAILS_TASK_CODE = "printCustomer";
   public static final String PRINT_INVOICE_SALE_LINE_HEADER_TASK_CODE = "printInvoiceSaleLineHeader";
   public static final String PRINT_INVOICE_SALE_LINES_TASK_CODE = "printInvoiceSaleLines";
   private int endIndex;
   private Sale sale;
   private PrintableTable table;
   private PrintProgress printProgress;

   public InvoicePrintTask(String taskCode) {
      super(taskCode);
   }

   public int getEndIndex() {
      return this.endIndex;
   }

   public void setEndIndex(int endIndex) {
      this.endIndex = endIndex;
   }

   public Sale getSale() {
      return this.sale;
   }

   public void setSale(Sale sale) {
      this.sale = sale;
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
