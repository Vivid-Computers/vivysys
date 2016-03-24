package vivyclient.print.reports;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.util.Calendar;
import java.util.Iterator;
import vivyclient.exception.AppRuntimeException;
import vivyclient.print.BasePagePrinter;
import vivyclient.print.PrintContext;
import vivyclient.print.PrintProgress;
import vivyclient.print.common.HeaderPrintableTable;
import vivyclient.print.reports.CustomerBalancesPrintTask;
import vivyclient.print.reports.CustomerBalancesPrintableTable;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class CustomerBalancesPagePrinter extends BasePagePrinter {
   private Font statementInfoFont = new Font("Arial", 1, 10);
   private Font customerDetailsHeadingFont = new Font("Arial", 1, 10);
   private Font customerDetailsFont = new Font("Arial", 0, 10);
   private Font footerTagFont = new Font("Arial", 1, 10);
   private Font footerPrintDetailsFont = new Font("Arial", 0, 8);

   public CustomerBalancesPagePrinter(int pageNumber) {
      super(pageNumber);
   }

   public PrintContext print(PrintContext printContext, boolean hidden) throws PrinterException {
      Iterator i = this.getPrintTasks().iterator();

      while(i.hasNext()) {
         CustomerBalancesPrintTask task = (CustomerBalancesPrintTask)i.next();
         Rectangle2D printArea;
         if(task.getTaskCode().equals("printHeader")) {
            printArea = this.printHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printReportInfo")) {
            printArea = this.printReportInfo(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printTableHeader")) {
            printArea = this.printTableHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printTableContent")) {
            printArea = this.printTable(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printFooter")) {
            printArea = this.printFooter(printContext, task, printContext.getPageCount() == this.getPageNumber() + 1, hidden);
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

   public Rectangle2D printHeader(PrintContext printContext, CustomerBalancesPrintTask task, boolean hidden) throws PrinterException {
      HeaderPrintableTable table = new HeaderPrintableTable("Customer Balances", false, printContext);
      table.initialise(printContext.getGraphics());
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), tableProgress, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printReportInfo(PrintContext printContext, CustomerBalancesPrintTask task, boolean hidden) throws PrinterException {
      PrintableTable table = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableColumn column2 = new TableColumn(1.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableBlock block = new TableBlock(table);
      String viewingCaption;
      if("all".equals(task.getReportDetails().getShowType())) {
         viewingCaption = "All Customers";
      } else if("debt".equals(task.getReportDetails().getShowType())) {
         viewingCaption = "Customers with an Outstanding Balance";
      } else {
         if(!"overdue".equals(task.getReportDetails().getShowType())) {
            throw new AppRuntimeException();
         }

         viewingCaption = "Customers with an Overdue Balance";
      }

      DefaultTableRow row = new DefaultTableRow(block);
      new TextTableCell("Showing: ", row, column1, 1, this.customerDetailsHeadingFont, Color.black);
      new TextTableCell(viewingCaption, row, column2, 1, this.customerDetailsFont, Color.black);
      String orderCaption;
      if("nameOrder".equals(task.getReportDetails().getOrderType())) {
         orderCaption = "Customer Name";
      } else if("balanceOrder".equals(task.getReportDetails().getOrderType())) {
         orderCaption = "Balance";
      } else {
         if(!"overdueOrder".equals(task.getReportDetails().getOrderType())) {
            throw new AppRuntimeException();
         }

         orderCaption = "Overdue Balance";
      }

      row = new DefaultTableRow(block);
      new TextTableCell("Ordered By: ", row, column1, 1, this.customerDetailsHeadingFont, Color.black);
      new TextTableCell(orderCaption, row, column2, 1, this.customerDetailsFont, Color.black);
      table.initialise(printContext.getGraphics());
      return table.print(printContext.getGraphics(), new PrintProgress(), printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printTableHeader(PrintContext printContext, CustomerBalancesPrintTask task, boolean hidden) throws PrinterException {
      CustomerBalancesPrintableTable table = task.getTable();
      return table.printColumnTitles(printContext.getGraphics(), printContext.getCurrentY(), hidden);
   }

   public Rectangle2D printTable(PrintContext printContext, CustomerBalancesPrintTask task, boolean hidden) throws PrinterException {
      double currentY = printContext.getCurrentY();
      CustomerBalancesPrintableTable table = task.getTable();
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      tableProgress.setPartsCompleted(task.getStartIndex());
      double heightRemaining = printContext.getMaxHeight() - currentY;
      currentY += table.print(printContext.getGraphics(), tableProgress, currentY, hidden, heightRemaining).getHeight();
      return new Double(printContext.getCurrentY(), printContext.getStartX(), printContext.getWidth(), currentY - printContext.getCurrentY());
   }

   public Rectangle2D printFooter(PrintContext printContext, CustomerBalancesPrintTask task, boolean finalPage, boolean hidden) throws PrinterException {
      PrintableTable footerTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 0.0D);
      TableColumn column1 = new TableColumn(0.0D, footerTable);
      column1.setAlign("W");
      TableColumn column2 = new TableColumn(0.0D, footerTable);
      column2.setAlign("C");
      new TableColumn(1.0D, footerTable);
      TableBlock block = new TableBlock(footerTable);
      DefaultTableRow tagRow = new DefaultTableRow(block);
      Font tagFont = getFontForWidth((String)task.getValue(), "Arial", 1, 10, printContext);
      TextTableCell tagCell = new TextTableCell(task.getValue(), tagRow, column1, 3, tagFont, Color.BLACK);
      tagCell.setAlign("C");
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
