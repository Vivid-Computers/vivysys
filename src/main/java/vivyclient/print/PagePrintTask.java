package vivyclient.print;

public class PagePrintTask {
   public static final String PRINT_HEADER_TASK_CODE = "printHeader";
   public static final String PRINT_VERTICAL_SPACE_TASK_CODE = "printFeed";
   public static final String PRINT_FOOTER_TASK_CODE = "printFooter";
   private String taskCode;
   private int startIndex;
   private double amount;
   private Object value;

   public PagePrintTask(String taskCode) {
      this.taskCode = taskCode;
   }

   public String getTaskCode() {
      return this.taskCode;
   }

   public void setTaskCode(String taskCode) {
      this.taskCode = taskCode;
   }

   public int getStartIndex() {
      return this.startIndex;
   }

   public void setStartIndex(int startIndex) {
      this.startIndex = startIndex;
   }

   public double getAmount() {
      return this.amount;
   }

   public void setAmount(double amount) {
      this.amount = amount;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
