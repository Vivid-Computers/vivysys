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
import vivyclient.print.sale.InvoicePrintTask;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class InvoicePagePrinter extends BasePagePrinter {
   private static final double INVOICE_DETAILS_HEADER_Y_MARGIN = 5.0D;
   private static final double INVOICE_DETAILS_HEADER_X_MARGIN = 20.0D;
   private static final double ADDRESS_TITLE_X_MARGIN = 0.0D;
   private static final double ADDRESS_LINE_X_MARGIN = 40.0D;
   private Font invoiceInfoFont = new Font("Arial", 1, 10);
   private Font customerDetailsHeaderFont = new Font("Arial", 1, 10);
   private Font customerDetailsFont = new Font("Arial", 0, 10);
   private Font footerThankyouFont = new Font("Arial", 0, 12);
   private Font footerTagFont = new Font("Arial", 1, 10);
   private Font footerPrintDetailsFont = new Font("Arial", 0, 8);

   public InvoicePagePrinter(int pageNumber) throws PrinterException {
      super(pageNumber);
   }

   public PrintContext print(PrintContext printContext, boolean hidden) throws PrinterException {
      Iterator i = this.getPrintTasks().iterator();

      while(i.hasNext()) {
         InvoicePrintTask task = (InvoicePrintTask)i.next();
         double beforeTaskY = printContext.getCurrentY();
         Rectangle2D printArea;
         if(task.getTaskCode().equals("printHeader")) {
            printArea = this.printInvoiceHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printInvoiceInfo")) {
            printArea = this.printInvoiceInfo(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printCustomer")) {
            printArea = this.printCustomerDetails(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printInvoiceSaleLineHeader")) {
            printArea = this.printSaleDetailHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printInvoiceSaleLines")) {
            printArea = this.printSaleDetailTable(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printFooter")) {
            printArea = this.printInvoiceFooter(printContext, task, printContext.getPageCount() == this.getPageNumber() + 1, hidden);
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

   public Rectangle2D printInvoiceHeader(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      HeaderPrintableTable table = new HeaderPrintableTable("Tax Invoice", true, printContext);
      table.initialise(printContext.getGraphics());
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), tableProgress, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printInvoiceInfo(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      String invoiceNumberString = "Invoice Number: " + task.getSale().getObjectId();
      String dateString = "Invoice Date: " + ViewUtil.calendarDisplay(task.getSale().getSaleDate());
      printContext.getGraphics().setFont(this.invoiceInfoFont);
      float fontAscent = printContext.getGraphics().getFontMetrics().getLineMetrics(dateString + invoiceNumberString, printContext.getGraphics()).getAscent();
      float fontDescent = printContext.getGraphics().getFontMetrics().getLineMetrics(dateString + invoiceNumberString, printContext.getGraphics()).getDescent();
      double boxHeight = (double)(fontAscent + fontDescent) + 10.0D;
      if(!hidden) {
         if(this.getPageNumber() == 0) {
            printContext.getGraphics().setColor(Color.BLACK);
            printContext.getGraphics().fillRect((int)printContext.getStartX(), (int)printContext.getCurrentY(), (int)printContext.getWidth(), (int)boxHeight);
         }

         printContext.getGraphics().setColor(this.getPageNumber() == 0?Color.WHITE:Color.BLACK);
         printContext.getGraphics().drawString(invoiceNumberString, 20.0F, (float)(printContext.getCurrentY() + 5.0D + (double)fontAscent));
         double dateStringWidth = printContext.getGraphics().getFontMetrics().getStringBounds(dateString, printContext.getGraphics()).getWidth();
         printContext.getGraphics().drawString(dateString, (float)(printContext.getWidth() - 20.0D - dateStringWidth), (float)(printContext.getCurrentY() + 5.0D + (double)fontAscent));
      }

      return new Double(printContext.getStartX(), printContext.getCurrentY(), printContext.getWidth(), boxHeight);
   }

   public Rectangle2D printCustomerDetails(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
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

   private Rectangle2D printBillToAddress(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      List billingLines = task.getSale().getBillingAddress().getLines();
      PrintableTable billingLinesTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, billingLinesTable);
      TableColumn column2 = new TableColumn(1.0D, billingLinesTable);
      TableBlock billToBlock = new TableBlock(billingLinesTable);

      DefaultTableRow tableProgress;
      for(int customerPhone = 0; customerPhone < billingLines.size(); ++customerPhone) {
         tableProgress = new DefaultTableRow(billToBlock);
         if(customerPhone == 0) {
            new TextTableCell("Bill to:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         }

         new TextTableCell(billingLines.get(customerPhone), tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      String var12 = task.getSale().getCustomer().getPhone();
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

   private Rectangle2D printDeliverToAddress(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      List deliveryLines = task.getSale().getDeliveryAddress().getLines();
      PrintableTable table = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, table);
      TableColumn column2 = new TableColumn(1.0D, table);
      TableBlock block = new TableBlock(table);

      DefaultTableRow tableProgress;
      for(int ref = 0; ref < deliveryLines.size(); ++ref) {
         tableProgress = new DefaultTableRow(block);
         if(ref == 0) {
            new TextTableCell("Deliver to:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         }

         new TextTableCell(deliveryLines.get(ref), tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      String var12 = task.getSale().getCustref();
      if(var12 != null && var12.trim().length() > 0) {
         tableProgress = new DefaultTableRow(block);
         new TextTableCell("Ref:", tableProgress, column1, 1, this.customerDetailsHeaderFont, Color.BLACK);
         new TextTableCell(var12, tableProgress, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      table.initialise(printContext.getGraphics());
      PrintProgress var11 = new PrintProgress();
      var11.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), var11, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printSaleDetailHeader(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      PrintableTable table = task.getTable();
      return table.printColumnTitles(printContext.getGraphics(), printContext.getCurrentY(), hidden);
   }

   public Rectangle2D printSaleDetailTable(PrintContext printContext, InvoicePrintTask task, boolean hidden) throws PrinterException {
      double currentY = printContext.getCurrentY();
      PrintableTable table = task.getTable();
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      tableProgress.setPartsCompleted(task.getStartIndex());
      double heightRemaining = printContext.getMaxHeight() - currentY;
      currentY += table.print(printContext.getGraphics(), tableProgress, currentY, hidden, heightRemaining).getHeight();
      return new Double(printContext.getCurrentY(), printContext.getStartX(), printContext.getWidth(), currentY - printContext.getCurrentY());
   }

   public Rectangle2D printInvoiceFooter(PrintContext printContext, InvoicePrintTask task, boolean finalPage, boolean hidden) throws PrinterException {
      PrintableTable footerTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 0.0D);
      TableColumn column1 = new TableColumn(0.5D, "", this.footerThankyouFont, Color.WHITE, "C", footerTable);
      column1.setAlign("W");
      TableColumn column2 = new TableColumn(0.5D, "", this.footerThankyouFont, Color.WHITE, "C", footerTable);
      column2.setAlign("E");
      TableBlock block = new TableBlock(footerTable);
      Object blankRow = null;
      DefaultTableRow tagRow = new DefaultTableRow(block);
      Font tagFont = getFontForWidth((String)task.getValue(), "Arial", 1, 10, printContext);
      TextTableCell tagCell = new TextTableCell(task.getValue(), tagRow, column1, 2, tagFont, Color.BLACK);
      tagCell.setAlign("C");
      new BlankTableRow(block, (Color)null, 5.0D);
      String printDetails = "Page " + (this.getPageNumber() + 1) + " of " + printContext.getPageCount() + ".  Printed " + ViewUtil.calendarDateTimeDisplay(Calendar.getInstance()) + ".";
      DefaultTableRow printInfoRow = new DefaultTableRow(block);
      TextTableCell printInfoCell = new TextTableCell(printDetails, printInfoRow, column1, 2, this.footerPrintDetailsFont, Color.BLACK);
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
