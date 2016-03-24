package vivyclient.print.sale;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import vivyclient.print.BasePagePrinter;
import vivyclient.print.PrintContext;
import vivyclient.print.PrintProgress;
import vivyclient.print.common.HeaderPrintableTable;
import vivyclient.print.sale.DispatchItemsPrintableTable;
import vivyclient.print.sale.DispatchPrintTask;
import vivyclient.print.table.BlankTableCell;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class DispatchSlipPagePrinter extends BasePagePrinter {
   private static final double DISPATCH_DETAILS_HEADER_Y_MARGIN = 5.0D;
   private static final double DISPATCH_DETAILS_HEADER_X_MARGIN = 10.0D;
   private static final double ADDRESS_TITLE_X_MARGIN = 0.0D;
   private static final double ADDRESS_LINE_X_MARGIN = 40.0D;
   private Font dispatchInfoFont = new Font("Arial", 1, 10);
   private Font customerDetailsHeaderFont = new Font("Arial", 1, 10);
   private Font customerDetailsFont = new Font("Arial", 0, 10);
   private Font shippingInfoTitleFont = new Font("Arial", 1, 10);
   private Font shippingInfoFont = new Font("Arial", 0, 10);
   private Font confirmationFont = new Font("Arial", 0, 10);
   private Font customerSignitureFont = new Font("Arial", 0, 6);
   private Font footerTagFont = new Font("Arial", 1, 10);
   private Font footerPrintDetailsFont = new Font("Arial", 0, 8);

   public DispatchSlipPagePrinter(int pageNumber) {
      super(pageNumber);
   }

   public PrintContext print(PrintContext printContext, boolean hidden) throws PrinterException {
      Iterator i = this.getPrintTasks().iterator();

      while(i.hasNext()) {
         DispatchPrintTask task = (DispatchPrintTask)i.next();
         Rectangle2D printArea;
         if(task.getTaskCode().equals("printHeader")) {
            printArea = this.printDispatchHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printDispatchInfo")) {
            printArea = this.printDispatchInfo(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printCustomerDetails")) {
            printArea = this.printCustomerDetails(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printShippingDetails")) {
            printArea = this.printShippingInfo(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printDispatchItemHeader")) {
            printArea = this.printDispatchItemsTableHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printDispatchItems")) {
            printArea = this.printDispatchItemsTable(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printFooter")) {
            printArea = this.printDispatchFooter(printContext, task, printContext.getPageCount() == this.getPageNumber() + 1, hidden);
            printContext.setMaxHeight(printContext.getMaxHeight() - printArea.getHeight());
         } else {
            if(!task.getTaskCode().equals("printFeed")) {
               throw new PrinterException("Illegal Print Task \"" + task.getTaskCode() + "\"");
            }

            printContext.setCurrentY(printContext.getCurrentY() + task.getAmount());
         }
      }

      return printContext;
   }

   public Rectangle2D printDispatchHeader(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      HeaderPrintableTable table = new HeaderPrintableTable("Packing Slip", false, printContext);
      table.initialise(printContext.getGraphics());
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), tableProgress, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printDispatchInfo(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      String dispatchString = "Dispatch: " + task.getDispatch().getObjectId() + " (" + ViewUtil.calendarDisplay(task.getDispatch().getDispatchDate()) + ")";
      String invoiceString = "For Invoice: " + task.getDispatch().getSale().getObjectId() + " (" + ViewUtil.calendarDisplay(task.getDispatch().getSale().getSaleDate()) + ")";
      printContext.getGraphics().setFont(this.dispatchInfoFont);
      float fontAscent = printContext.getGraphics().getFontMetrics().getLineMetrics(dispatchString + invoiceString, printContext.getGraphics()).getAscent();
      float fontDescent = printContext.getGraphics().getFontMetrics().getLineMetrics(dispatchString + invoiceString, printContext.getGraphics()).getDescent();
      double boxHeight = (double)(fontAscent + fontDescent) + 10.0D;
      if(!hidden) {
         if(this.getPageNumber() == 0) {
            printContext.getGraphics().setColor(Color.BLACK);
            printContext.getGraphics().fillRect((int)printContext.getStartX(), (int)printContext.getCurrentY(), (int)printContext.getWidth(), (int)boxHeight);
         }

         printContext.getGraphics().setColor(this.getPageNumber() == 0?Color.WHITE:Color.BLACK);
         printContext.getGraphics().drawString(dispatchString, 10.0F, (float)(printContext.getCurrentY() + 5.0D + (double)fontAscent));
         double invoiceStringWidth = printContext.getGraphics().getFontMetrics().getStringBounds(invoiceString, printContext.getGraphics()).getWidth();
         printContext.getGraphics().drawString(invoiceString, (float)(printContext.getWidth() - 10.0D - invoiceStringWidth), (float)(printContext.getCurrentY() + 5.0D + (double)fontAscent));
      }

      return new Double(printContext.getStartX(), printContext.getCurrentY(), printContext.getWidth(), boxHeight);
   }

   public Rectangle2D printCustomerDetails(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      List deliveryLines = task.getDispatch().getSale().getDeliveryAddress().getLines();
      PrintContext billToContext = printContext.createClone();
      billToContext.setWidth(billToContext.getWidth() / 2.0D);
      Rectangle2D billToBounds = this.printBillToAddress(billToContext, task, hidden);
      PrintContext deliverToContext = printContext.createClone();
      deliverToContext.setWidth(deliverToContext.getWidth() / 2.0D);
      deliverToContext.setStartX(deliverToContext.getStartX() + deliverToContext.getWidth());
      Rectangle2D deliverToBounds = this.printDeliverToAddress(deliverToContext, task, hidden);
      billToBounds.add(deliverToBounds);
      return billToBounds;
   }

   private Rectangle2D printBillToAddress(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      List billingLines = task.getDispatch().getSale().getBillingAddress().getLines();
      PrintableTable billingLinesTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, (String)null, (Font)null, (Color)null, (String)null, billingLinesTable);
      TableColumn column2 = new TableColumn(1.0D, (String)null, (Font)null, (Color)null, (String)null, billingLinesTable);
      TableBlock billToBlock = new TableBlock(billingLinesTable);

      DefaultTableRow tableProgress;
      for(int customerPhone = 0; customerPhone < billingLines.size(); ++customerPhone) {
         tableProgress = new DefaultTableRow(billToBlock);
         if(customerPhone == 0) {
            new TextTableCell("Bill to:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         }

         new TextTableCell(billingLines.get(customerPhone), tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      String var12 = task.getDispatch().getSale().getCustomer().getPhone();
      if(var12 != null && var12.trim().length() > 0) {
         tableProgress = new DefaultTableRow(billToBlock);
         new TextTableCell("Phone:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         new TextTableCell(var12, tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      billingLinesTable.initialise(printContext.getGraphics());
      PrintProgress var11 = new PrintProgress();
      var11.setTotalParts(billingLinesTable.getBlocks().size());
      return billingLinesTable.print(printContext.getGraphics(), var11, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   private Rectangle2D printDeliverToAddress(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      List deliveryLines = task.getDispatch().getSale().getDeliveryAddress().getLines();
      PrintableTable table = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableColumn column2 = new TableColumn(1.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableBlock block = new TableBlock(table);

      DefaultTableRow ref;
      for(int attention = 0; attention < deliveryLines.size(); ++attention) {
         ref = new DefaultTableRow(block);
         if(attention == 0) {
            new TextTableCell("Deliver to:", ref, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         }

         new TextTableCell(deliveryLines.get(attention), ref, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      String var12 = task.getDispatch().getAttention();
      if(var12 != null && var12.trim().length() > 0) {
         ref = new DefaultTableRow(block);
         new TextTableCell("Attn:", ref, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         new TextTableCell(var12, ref, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      String var13 = task.getDispatch().getSale().getCustref();
      if(var13 != null && var13.trim().length() > 0) {
         DefaultTableRow tableProgress = new DefaultTableRow(block);
         new TextTableCell("Ref:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         new TextTableCell(var13, tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      table.initialise(printContext.getGraphics());
      PrintProgress var14 = new PrintProgress();
      var14.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), var14, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printShippingInfo(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      PrintableTable table = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 5.0D);
      TableColumn column1 = new TableColumn(0.0D, table);
      column1.setAlign("W");
      TableColumn column2 = new TableColumn(0.5D, table);
      column1.setAlign("W");
      TableColumn column3 = new TableColumn(0.0D, table);
      column1.setAlign("W");
      TableColumn column4 = new TableColumn(0.5D, table);
      column1.setAlign("W");
      TableBlock block = new TableBlock(table);
      DefaultTableRow shippedWithRow = new DefaultTableRow(block);
      new TextTableCell("Shipped with:", shippedWithRow, column1, 1, this.shippingInfoTitleFont, Color.BLACK);
      new TextTableCell(task.getDispatch().getSupplier().getName(), shippedWithRow, column2, 1, this.shippingInfoFont, Color.BLACK);
      new TextTableCell("Tracking Number:", shippedWithRow, column3, 1, this.shippingInfoTitleFont, Color.BLACK);
      new TextTableCell(task.getDispatch().getTrackingId(), shippedWithRow, column4, 1, this.shippingInfoFont, Color.BLACK);
      table.initialise(printContext.getGraphics());
      PrintProgress progress = new PrintProgress();
      progress.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), progress, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printDispatchItemsTableHeader(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      DispatchItemsPrintableTable table = task.getTable();
      return table.printColumnTitles(printContext.getGraphics(), printContext.getCurrentY(), hidden);
   }

   public Rectangle2D printDispatchItemsTable(PrintContext printContext, DispatchPrintTask task, boolean hidden) throws PrinterException {
      double currentY = printContext.getCurrentY();
      DispatchItemsPrintableTable table = task.getTable();
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      tableProgress.setPartsCompleted(task.getStartIndex());
      double heightRemaining = printContext.getMaxHeight() - currentY;
      currentY += table.print(printContext.getGraphics(), tableProgress, currentY, hidden, heightRemaining).getHeight();
      return new Double(printContext.getCurrentY(), printContext.getStartX(), printContext.getWidth(), currentY - printContext.getCurrentY());
   }

   public Rectangle2D printDispatchFooter(PrintContext printContext, DispatchPrintTask task, boolean finalPage, boolean hidden) throws PrinterException {
      PrintableTable footerTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 0.0D);
      TableColumn column1 = new TableColumn(0.0D, footerTable);
      column1.setAlign("W");
      TableColumn column2 = new TableColumn(0.0D, footerTable);
      column2.setAlign("C");
      new TableColumn(1.0D, footerTable);
      TableBlock block = new TableBlock(footerTable);
      Object blankRow = null;
      DefaultTableRow tagRow;
      if(this.getPageNumber() + 1 == printContext.getPageCount()) {
         tagRow = new DefaultTableRow(block);
         new TextTableCell("All items received in good condition", tagRow, column1, 1, this.confirmationFont, Color.BLACK);
         DefaultTableRow tagFont = new DefaultTableRow(block);
         new BlankTableCell(tagFont, column2, 1, 150.0D, 1.0D, Color.BLACK);
         DefaultTableRow tagCell = new DefaultTableRow(block);
         new TextTableCell("Customer\'s signature", tagCell, column2, 1, this.customerSignitureFont, Color.BLACK);
         new BlankTableRow(block, (Color)null, 5.0D);
      }

      tagRow = new DefaultTableRow(block);
      Font tagFont1 = getFontForWidth((String)task.getValue(), "Arial", 1, 10, printContext);
      TextTableCell tagCell1 = new TextTableCell(task.getValue(), tagRow, column1, 3, tagFont1, Color.BLACK);
      tagCell1.setAlign("C");
      new BlankTableRow(block, (Color)null, 5.0D);
      String printDetails = "Page " + (this.getPageNumber() + 1) + " of " + printContext.getPageCount() + ".  Printed " + ViewUtil.calendarDateTimeDisplay(Calendar.getInstance()) + ".";
      DefaultTableRow printInfoRow = new DefaultTableRow(block);
      TextTableCell printInfoCell = new TextTableCell(printDetails, printInfoRow, column1, 3, this.footerPrintDetailsFont, Color.BLACK);
      printInfoCell.setAlign("C");
      footerTable.initialise(printContext.getGraphics());
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(footerTable.getBlocks().size());
      double heightRemaining = printContext.getMaxHeight() - printContext.getCurrentY();
      Rectangle2D footerBounds = footerTable.print(printContext.getGraphics(), tableProgress, printContext.getCurrentY(), true, heightRemaining);
      double tableStartY = printContext.getStartY() + printContext.getMaxHeight() - footerBounds.getHeight();
      return (Rectangle2D)(!hidden?footerTable.print(printContext.getGraphics(), tableProgress, tableStartY, false, footerBounds.getHeight()):new Double(printContext.getStartX(), tableStartY, printContext.getWidth(), footerBounds.getHeight()));
   }
}
