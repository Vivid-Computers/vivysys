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
import vivyclient.model.Dispatch;
import vivyclient.print.BasePrinter;
import vivyclient.print.PrintContext;
import vivyclient.print.PrintProgress;
import vivyclient.print.sale.DispatchItemsPrintableTable;
import vivyclient.print.sale.DispatchPrintTask;
import vivyclient.print.sale.DispatchSlipPagePrinter;
import vivyclient.util.Settings;

public class DispatchPackingSlipPrinter extends BasePrinter {
   private Dispatch dispatch;

   public DispatchPackingSlipPrinter(Dispatch dispatch) throws Exception {
      this.dispatch = dispatch;
   }

   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      Graphics2D g2 = (Graphics2D)graphics;
      AffineTransform originalTransform = g2.getTransform();

      byte var13;
      try {
         System.out.println("[DispatchPackingSlipPrinter] print page " + pageIndex + " called");
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

         DispatchSlipPagePrinter pagePrinter = (DispatchSlipPagePrinter)this.getPrintPages().get(pageIndex);
         pagePrinter.print(printContext, false);
         var13 = 0;
      } catch (PrinterException var18) {
         ErrorWriter.writeException(var18);
         throw var18;
      } catch (Exception var19) {
         ErrorWriter.writeException(var19);
         throw new PrinterException("Packing slip rendering exception " + var19.getClass().getName() + ": " + var19.getMessage());
      } finally {
         g2.setTransform(originalTransform);
      }

      return var13;
   }

   private List getPagePrintList(PrintContext printContext) throws PrinterException {
      printContext = printContext.createClone();
      ArrayList pages = new ArrayList();
      DispatchItemsPrintableTable table = new DispatchItemsPrintableTable(this.dispatch, printContext);
      table.setForeColour(Color.BLACK);
      table.initialise(printContext.getGraphics());
      PrintProgress dispatchLinesPrintProgress = new PrintProgress();
      dispatchLinesPrintProgress.setTotalParts(table.getBlocks().size());

      boolean finalPage;
      do {
         DispatchSlipPagePrinter page = new DispatchSlipPagePrinter(pages.size());
         DispatchPrintTask task = new DispatchPrintTask("printHeader");
         task.setDispatch(this.dispatch);
         page.addPrintTask(task);
         task = new DispatchPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         task = new DispatchPrintTask("printDispatchInfo");
         task.setDispatch(this.dispatch);
         page.addPrintTask(task);
         task = new DispatchPrintTask("printFeed");
         task.setAmount(10.0D);
         page.addPrintTask(task);
         if(page.getPageNumber() == 0) {
            task = new DispatchPrintTask("printCustomerDetails");
            task.setDispatch(this.dispatch);
            page.addPrintTask(task);
            task = new DispatchPrintTask("printFeed");
            task.setAmount(15.0D);
            page.addPrintTask(task);
            task = new DispatchPrintTask("printShippingDetails");
            task.setDispatch(this.dispatch);
            page.addPrintTask(task);
            task = new DispatchPrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
         }

         if(dispatchLinesPrintProgress.getPartsCompleted() < dispatchLinesPrintProgress.getTotalParts()) {
            task = new DispatchPrintTask("printDispatchItemHeader");
            task.setTable(table);
            page.addPrintTask(task);
            task = new DispatchPrintTask("printFeed");
            task.setAmount(10.0D);
            page.addPrintTask(task);
            PrintContext pageTopPrinted = page.print(printContext.createClone(), true);
            DispatchPrintTask footerTask = new DispatchPrintTask("printFooter");
            footerTask.setValue(Settings.getRandomFooterTag());
            footerTask.setDispatch(this.dispatch);
            page.addPrintTask(footerTask);
            finalPage = this.getDispatchItemsProgressableThisPage(page, table, dispatchLinesPrintProgress, pageTopPrinted, footerTask);
            task = new DispatchPrintTask("printDispatchItems");
            task.setDispatch(this.dispatch);
            task.setTable(table);
            task.setStartIndex(dispatchLinesPrintProgress.getPartsCompleted());
            page.addPrintTask(task);
            dispatchLinesPrintProgress.setPartsCompleted(dispatchLinesPrintProgress.getPartsCompleteable());
         } else {
            task = new DispatchPrintTask("printFooter");
            page.addPrintTask(task);
            finalPage = true;
         }

         pages.add(page);
      } while(!finalPage);

      return pages;
   }

   private boolean getDispatchItemsProgressableThisPage(DispatchSlipPagePrinter page, DispatchItemsPrintableTable table, PrintProgress progress, PrintContext printContext, DispatchPrintTask footerTask) throws PrinterException {
      double lastPageFooterHeight = page.printDispatchFooter(printContext, footerTask, true, true).getHeight();
      double lastPageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - lastPageFooterHeight;
      table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, lastPageAvailableTableHeight);
      if(progress.getPartsCompleteable() == progress.getTotalParts()) {
         return true;
      } else {
         double pageFooterHeight = page.printDispatchFooter(printContext, footerTask, false, true).getHeight();
         double pageAvailableTableHeight = printContext.getMaxHeight() - printContext.getCurrentY() - pageFooterHeight;
         table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), true, pageAvailableTableHeight);
         return false;
      }
   }
}
