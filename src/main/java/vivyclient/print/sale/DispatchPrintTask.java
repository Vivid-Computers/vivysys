package vivyclient.print.sale;

import vivyclient.model.Dispatch;
import vivyclient.print.PagePrintTask;
import vivyclient.print.sale.DispatchItemsPrintableTable;

public class DispatchPrintTask extends PagePrintTask {
   public static final String PRINT_DISPATCH_INFO_TASK_CODE = "printDispatchInfo";
   public static final String PRINT_CUSTOMER_DETAILS_TASK_CODE = "printCustomerDetails";
   public static final String PRINT_DISPATCH_ITEM_HEADER_TASK_CODE = "printDispatchItemHeader";
   public static final String PRINT_DISPATCH_ITEMS_TASK_CODE = "printDispatchItems";
   public static final String PRINT_SHIPPING_DETAILS_TASK_CODE = "printShippingDetails";
   private Dispatch dispatch;
   private DispatchItemsPrintableTable table;

   public DispatchPrintTask(String taskCode) {
      super(taskCode);
   }

   public Dispatch getDispatch() {
      return this.dispatch;
   }

   public void setDispatch(Dispatch dispatch) {
      this.dispatch = dispatch;
   }

   public DispatchItemsPrintableTable getTable() {
      return this.table;
   }

   public void setTable(DispatchItemsPrintableTable table) {
      this.table = table;
   }
}
