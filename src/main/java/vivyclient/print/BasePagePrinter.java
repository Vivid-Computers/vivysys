package vivyclient.print;

import java.awt.Font;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import vivyclient.print.PagePrintTask;
import vivyclient.print.PrintContext;

public abstract class BasePagePrinter {
   private List pagePrintTasks;
   private int pageNumber = -1;

   public BasePagePrinter(int pageNumber) {
      this.pageNumber = pageNumber;
      this.pagePrintTasks = new ArrayList();
   }

   public abstract PrintContext print(PrintContext var1, boolean var2) throws PrinterException;

   public void addPrintTask(PagePrintTask task) {
      this.pagePrintTasks.add(task);
   }

   public List getPrintTasks() {
      return this.pagePrintTasks;
   }

   public int getPageNumber() {
      return this.pageNumber;
   }

   public void setPageNumber(int pageNumber) {
      this.pageNumber = pageNumber;
   }

   protected static Font getFontForWidth(String message, String fontName, int style, int startSize, PrintContext printContext) {
      int currentSize = startSize;

      double printWidth;
      do {
         ++currentSize;
         Font currentFont = new Font(fontName, style, currentSize);
         printWidth = printContext.getGraphics().getFontMetrics(currentFont).getStringBounds(message, printContext.getGraphics()).getWidth();
      } while(printWidth <= printContext.getWidth());

      return new Font(fontName, style, currentSize - 1);
   }
}
