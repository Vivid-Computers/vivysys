package vivyclient.print.sale;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import vivyclient.error.ErrorWriter;
import vivyclient.model.Sale;
import vivyclient.print.BasePrinter;
import vivyclient.print.PrintContext;
import vivyclient.print.PrintProgress;
import vivyclient.print.sale.InvoicePagePrinter;
import vivyclient.print.sale.InvoicePrintTask;
import vivyclient.print.sale.InvoiceSaleDetailsPrintableTable;
import vivyclient.util.Settings;

public class InvoicePrinter extends BasePrinter {
   private static final double SALE_LINE_DIVIDER_MARGIN_WIDTH = 5.0D;
   private Sale sale;

   public InvoicePrinter(Sale sale) {
      this.sale = sale;
   }

   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      Graphics2D g2 = (Graphics2D)graphics;
      AffineTransform originalTransform = g2.getTransform();

      byte pagePrinter;
      try {
         System.out.println("\n\n\n*********************************************");
         System.out.println("[InvoicePrinter] print page " + pageIndex + " called");
         AffineTransform e = new AffineTransform();
         e.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
         g2.transform(e);
         g2.setColor(Color.BLACK);
         double width = pageFormat.getImageableWidth();
         double height = (double)((int)pageFormat.getImageableHeight());
         PrintContext printContext = new PrintContext(g2, 0.0D, 0.0D, width, height);
         if(this.getPrintPages() == null) {
            System.out.println("\nCalculate invoice requirements");
            this.setPrintPages(this.getPagePrintList(printContext));
            System.out.println("Requirements calculated\n");
         }

         printContext.setPageCount(this.getPrintPages().size());
         if(pageIndex < this.getPrintPages().size()) {
            InvoicePagePrinter pagePrinter1 = (InvoicePagePrinter)this.getPrintPages().get(pageIndex);
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
         throw new PrinterException("Invoice rendering exception " + var19.getClass().getName() + ": " + var19.getMessage());
      } finally {
         g2.setTransform(originalTransform);
      }

      return pagePrinter;
   }

   private List getPagePrintList(PrintContext printContext) throws PrinterException {
      printContext = printContext.createClone();
      ArrayList pages = new ArrayList();
      InvoiceSaleDetailsPrintableTable table = new InvoiceSaleDetailsPrintableTable(this.sale, printContext.getStartX(), printContext.getWidth(), 5.0D);
      table.setForeColour(Color.BLACK);
      table.initialise(printContext.getGraphics());
      PrintProgress tablePrintProgress = new PrintProgress();
      tablePrintProgress.setTotalParts(table.getBlocks().size());

      boolean finalPage;
      do {
         InvoicePagePrinter page = new InvoicePagePrinter(pages.size());
         InvoicePrintTask task = new InvoicePrintTask("printHeader");
         task.setSale(this.sale);
         page.addPrintTask(task);
         task = new InvoicePrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         task = new InvoicePrintTask("printInvoiceInfo");
         task.setSale(this.sale);
         page.addPrintTask(task);
         task = new InvoicePrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         if(page.getPageNumber() == 0) {
            task = new InvoicePrintTask("printCustomer");
            task.setSale(this.sale);
            page.addPrintTask(task);
            task = new InvoicePrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
         }

         if(tablePrintProgress.getPartsCompleted() < tablePrintProgress.getTotalParts()) {
            task = new InvoicePrintTask("printInvoiceSaleLineHeader");
            task.setTable(table);
            page.addPrintTask(task);
            task = new InvoicePrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
            PrintContext pageTopPrinted = page.print(printContext.createClone(), true);
            InvoicePrintTask footerTask = new InvoicePrintTask("printFooter");
            footerTask.setValue(Settings.getRandomFooterTag());
            page.addPrintTask(footerTask);
            finalPage = this.getSaleLinesProgressableThisPage(page, table, tablePrintProgress, pageTopPrinted, footerTask);
            task = new InvoicePrintTask("printInvoiceSaleLines");
            task.setSale(this.sale);
            task.setTable(table);
            task.setStartIndex(tablePrintProgress.getPartsCompleted());
            page.addPrintTask(task);
            tablePrintProgress.setPartsCompleted(tablePrintProgress.getPartsCompleteable());
         } else {
            task = new InvoicePrintTask("printFooter");
            page.addPrintTask(task);
            finalPage = true;
         }

         pages.add(page);
      } while(!finalPage);

      return pages;
   }

   private boolean getSaleLinesProgressableThisPage(InvoicePagePrinter page, InvoiceSaleDetailsPrintableTable table, PrintProgress progress, PrintContext printContext, InvoicePrintTask footerTask) throws PrinterException {
      double lastPageFooterHeight = page.printInvoiceFooter(printContext, footerTask, true, true).getHeight();
      double lastPageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - lastPageFooterHeight;
      table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, lastPageAvailableTableHeight);
      if(progress.getPartsCompleteable() == progress.getTotalParts()) {
         return true;
      } else {
         double pageFooterHeight = page.printInvoiceFooter(printContext, footerTask, false, true).getHeight();
         double pageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - pageFooterHeight;
         table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, pageAvailableTableHeight);
         return false;
      }
   }
}
