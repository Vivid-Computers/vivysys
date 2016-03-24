package vivyclient.print.reports;

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
import vivyclient.print.reports.CustomerBalances;
import vivyclient.print.reports.CustomerBalancesPagePrinter;
import vivyclient.print.reports.CustomerBalancesPrintTask;
import vivyclient.print.reports.CustomerBalancesPrintableTable;
import vivyclient.print.table.PrintableTable;
import vivyclient.util.Settings;

public class CustomerBalancesReportPrinter extends BasePrinter {
   private CustomerBalances details;

   public CustomerBalancesReportPrinter(CustomerBalances details) {
      this.details = details;
   }

   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      Graphics2D g2 = (Graphics2D)graphics;
      AffineTransform originalTransform = g2.getTransform();

      byte var13;
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
         if(pageIndex >= this.getPrintPages().size()) {
            byte pagePrinter1 = 1;
            return pagePrinter1;
         }

         BasePagePrinter pagePrinter = (BasePagePrinter)this.getPrintPages().get(pageIndex);
         pagePrinter.print(printContext, false);
         var13 = 0;
      } catch (PrinterException var18) {
         ErrorWriter.writeException(var18);
         throw var18;
      } catch (Exception var19) {
         ErrorWriter.writeException(var19);
         throw new PrinterException("Error Rendering Statement " + var19.getClass().getName() + ": " + var19.getMessage());
      } finally {
         g2.setTransform(originalTransform);
      }

      return var13;
   }

   private List getPagePrintList(PrintContext printContext) throws PrinterException {
      printContext = printContext.createClone();
      ArrayList pages = new ArrayList();
      CustomerBalancesPrintableTable table = new CustomerBalancesPrintableTable(this.details, printContext);
      table.setForeColour(Color.BLACK);
      table.initialise(printContext.getGraphics());
      PrintProgress tablePrintProgress = new PrintProgress();
      tablePrintProgress.setTotalParts(table.getBlocks().size());

      boolean finalPage;
      do {
         CustomerBalancesPagePrinter page = new CustomerBalancesPagePrinter(pages.size());
         CustomerBalancesPrintTask task = new CustomerBalancesPrintTask("printHeader");
         task.setReportDetails(this.details);
         page.addPrintTask(task);
         task = new CustomerBalancesPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         task = new CustomerBalancesPrintTask("printReportInfo");
         task.setReportDetails(this.details);
         page.addPrintTask(task);
         task = new CustomerBalancesPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         if(tablePrintProgress.getPartsCompleted() < tablePrintProgress.getTotalParts()) {
            task = new CustomerBalancesPrintTask("printTableHeader");
            task.setTable(table);
            page.addPrintTask(task);
            task = new CustomerBalancesPrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
            PrintContext pageTopPrinted = page.print(printContext.createClone(), true);
            CustomerBalancesPrintTask footerTask = new CustomerBalancesPrintTask("printFooter");
            footerTask.setValue(Settings.getRandomFooterTag());
            task.setReportDetails(this.details);
            page.addPrintTask(footerTask);
            finalPage = this.getTableItemsProgressableThisPage(page, table, tablePrintProgress, pageTopPrinted, footerTask);
            task = new CustomerBalancesPrintTask("printTableContent");
            task.setReportDetails(this.details);
            task.setTable(table);
            task.setStartIndex(tablePrintProgress.getPartsCompleted());
            page.addPrintTask(task);
            tablePrintProgress.setPartsCompleted(tablePrintProgress.getPartsCompleteable());
         } else {
            task = new CustomerBalancesPrintTask("printFooter");
            page.addPrintTask(task);
            finalPage = true;
         }

         pages.add(page);
      } while(!finalPage);

      return pages;
   }

   private boolean getTableItemsProgressableThisPage(CustomerBalancesPagePrinter page, PrintableTable table, PrintProgress progress, PrintContext printContext, CustomerBalancesPrintTask footerTask) throws PrinterException {
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
}
