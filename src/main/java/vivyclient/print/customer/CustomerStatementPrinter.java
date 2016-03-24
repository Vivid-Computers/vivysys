package vivyclient.print.customer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import vivyclient.error.ErrorWriter;
import vivyclient.print.BasePagePrinter;
import vivyclient.print.BasePrinter;
import vivyclient.print.PrintContext;
import vivyclient.print.PrintProgress;
import vivyclient.print.customer.CustomerStatement;
import vivyclient.print.customer.CustomerStatementPagePrinter;
import vivyclient.print.customer.CustomerStatementPrintTask;
import vivyclient.print.customer.CustomerStatementPrintableTable;
import vivyclient.print.table.PrintableTable;
import vivyclient.util.Settings;

public class CustomerStatementPrinter extends BasePrinter {
   private CustomerStatement customerStatement;

   public CustomerStatementPrinter(CustomerStatement customerStatement) {
      this.customerStatement = customerStatement;
   }

   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      Graphics2D g2 = (Graphics2D)graphics;
      AffineTransform originalTransform = g2.getTransform();

      byte pagePrinter;
      try {
         AffineTransform e = new AffineTransform();
         e.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
         g2.transform(e);
         g2.setColor(Color.BLACK);
         double width = pageFormat.getImageableWidth();
         double height = (double)((int)pageFormat.getImageableHeight());
         PrintContext printContext = new PrintContext(g2, 0.0D, 0.0D, width, height);
         if(this.getPrintPages() == null) {
            this.setPrintPages(this.getPagePrintList(printContext));
         }

         printContext.setPageCount(this.getPrintPages().size());
         if(pageIndex < this.getPrintPages().size()) {
            BasePagePrinter pagePrinter1 = (BasePagePrinter)this.getPrintPages().get(pageIndex);
            pagePrinter1.print(printContext, false);
            byte var13 = 0;
            return var13;
         }

         pagePrinter = 1;
      } catch (PrinterException var18) {
         ErrorWriter.writeException(var18);
         throw var18;
      } catch (Exception var19) {
         ErrorWriter.writeException(var19);
         throw new PrinterException("Error Rendering Statement " + var19.getClass().getName() + ": " + var19.getMessage());
      } finally {
         g2.setTransform(originalTransform);
      }

      return pagePrinter;
   }

   private List getPagePrintList(PrintContext printContext) throws PrinterException {
      printContext = printContext.createClone();
      ArrayList pages = new ArrayList();
      CustomerStatementPrintableTable table = new CustomerStatementPrintableTable(this.customerStatement, printContext);
      table.setForeColour(Color.BLACK);
      table.initialise(printContext.getGraphics());
      PrintProgress tablePrintProgress = new PrintProgress();
      tablePrintProgress.setTotalParts(table.getBlocks().size());

      boolean finalPage;
      do {
         CustomerStatementPagePrinter page = new CustomerStatementPagePrinter(pages.size());
         CustomerStatementPrintTask task = new CustomerStatementPrintTask("printHeader");
         task.setStatementDetails(this.customerStatement);
         page.addPrintTask(task);
         task = new CustomerStatementPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         task = new CustomerStatementPrintTask("printStatementInfo");
         task.setStatementDetails(this.customerStatement);
         page.addPrintTask(task);
         task = new CustomerStatementPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         if(page.getPageNumber() == 0) {
            task = new CustomerStatementPrintTask("printCustomerDetails");
            task.setStatementDetails(this.customerStatement);
            page.addPrintTask(task);
            task = new CustomerStatementPrintTask("printFeed");
            task.setAmount(15.0D);
            page.addPrintTask(task);
         }

         if(tablePrintProgress.getPartsCompleted() < tablePrintProgress.getTotalParts()) {
            task = new CustomerStatementPrintTask("printTableHeader");
            task.setTable(table);
            page.addPrintTask(task);
            task = new CustomerStatementPrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
            PrintContext pageTopPrinted = page.print(printContext.createClone(), true);
            CustomerStatementPrintTask footerTask = new CustomerStatementPrintTask("printFooter");
            footerTask.setValue(Settings.getRandomFooterTag());
            task.setStatementDetails(this.customerStatement);
            page.addPrintTask(footerTask);
            finalPage = this.getTableItemsProgressableThisPage(page, table, tablePrintProgress, pageTopPrinted, footerTask);
            task = new CustomerStatementPrintTask("printTableItems");
            task.setStatementDetails(this.customerStatement);
            task.setTable(table);
            task.setStartIndex(tablePrintProgress.getPartsCompleted());
            page.addPrintTask(task);
            tablePrintProgress.setPartsCompleted(tablePrintProgress.getPartsCompleteable());
         } else {
            task = new CustomerStatementPrintTask("printFooter");
            page.addPrintTask(task);
            finalPage = true;
         }

         pages.add(page);
      } while(!finalPage);

      return pages;
   }

   private boolean getTableItemsProgressableThisPage(CustomerStatementPagePrinter page, PrintableTable table, PrintProgress progress, PrintContext printContext, CustomerStatementPrintTask footerTask) throws PrinterException {
      double lastPageFooterHeight = page.printFooter(printContext, footerTask, true, true).getHeight();
      double lastPageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - lastPageFooterHeight;
      table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, lastPageAvailableTableHeight);
      if(progress.getPartsCompleteable() == progress.getTotalParts()) {
         return true;
      } else {
         double pageFooterHeight = page.printFooter(printContext, footerTask, false, true).getHeight();
         double pageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - pageFooterHeight;
         table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, pageAvailableTableHeight);
         return false;
      }
   }

   public CustomerStatement getCustomerStatement() {
      return this.customerStatement;
   }

   public void setCustomerStatement(CustomerStatement customerStatement) {
      this.customerStatement = customerStatement;
   }
}
