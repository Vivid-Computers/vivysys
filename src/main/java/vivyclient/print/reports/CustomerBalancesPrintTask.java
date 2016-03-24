package vivyclient.print.reports;

import vivyclient.print.PagePrintTask;
import vivyclient.print.reports.CustomerBalances;
import vivyclient.print.reports.CustomerBalancesPrintableTable;

public class CustomerBalancesPrintTask extends PagePrintTask {
   public static final String PRINT_REPORT_INFO_TASK_CODE = "printReportInfo";
   public static final String PRINT_TABLE_HEADER_TASK_CODE = "printTableHeader";
   public static final String PRINT_TABLE_CONTENT_TASK_CODE = "printTableContent";
   private CustomerBalances reportDetails;
   private CustomerBalancesPrintableTable table;

   public CustomerBalancesPrintTask(String taskCode) {
      super(taskCode);
   }

   public CustomerBalances getReportDetails() {
      return this.reportDetails;
   }

   public void setReportDetails(CustomerBalances reportDetails) {
      this.reportDetails = reportDetails;
   }

   public CustomerBalancesPrintableTable getTable() {
      return this.table;
   }

   public void setTable(CustomerBalancesPrintableTable table) {
      this.table = table;
   }
}
