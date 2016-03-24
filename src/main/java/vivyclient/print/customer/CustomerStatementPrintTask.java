package vivyclient.print.customer;

import vivyclient.print.PagePrintTask;
import vivyclient.print.customer.CustomerStatement;
import vivyclient.print.customer.CustomerStatementPrintableTable;

public class CustomerStatementPrintTask extends PagePrintTask {
   public static final String PRINT_STATEMENT_INFO_TASK_CODE = "printStatementInfo";
   public static final String PRINT_CUSTOMER_DETAILS_TASK_CODE = "printCustomerDetails";
   public static final String PRINT_TABLE_HEADER_TASK_CODE = "printTableHeader";
   public static final String PRINT_TABLE_CONTENT_TASK_CODE = "printTableItems";
   private CustomerStatement statementDetails;
   private CustomerStatementPrintableTable table;

   public CustomerStatementPrintTask(String taskCode) {
      super(taskCode);
   }

   public CustomerStatement getStatementDetails() {
      return this.statementDetails;
   }

   public void setStatementDetails(CustomerStatement statementDetails) {
      this.statementDetails = statementDetails;
   }

   public CustomerStatementPrintableTable getTable() {
      return this.table;
   }

   public void setTable(CustomerStatementPrintableTable table) {
      this.table = table;
   }
}
