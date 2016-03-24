package vivyclient.print.customer;

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
import vivyclient.print.customer.CustomerStatementPrintTask;
import vivyclient.print.customer.CustomerStatementPrintableTable;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class CustomerStatementPagePrinter extends BasePagePrinter {
   private Font statementInfoFont = new Font("Arial", 1, 10);
   private Font customerDetailsHeadingFont = new Font("Arial", 1, 10);
   private Font customerDetailsFont = new Font("Arial", 0, 10);
   private Font footerTagFont = new Font("Arial", 1, 10);
   private Font footerPrintDetailsFont = new Font("Arial", 0, 8);

   public CustomerStatementPagePrinter(int pageNumber) {
      super(pageNumber);
   }

   public PrintContext print(PrintContext printContext, boolean hidden) throws PrinterException {
      Iterator i = this.getPrintTasks().iterator();

      while(i.hasNext()) {
         CustomerStatementPrintTask task = (CustomerStatementPrintTask)i.next();
         Rectangle2D printArea;
         if(task.getTaskCode().equals("printHeader")) {
            printArea = this.printHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printStatementInfo")) {
            printArea = this.printStatementInfo(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printCustomerDetails")) {
            printArea = this.printCustomerDetails(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printTableHeader")) {
            printArea = this.printTableHeader(printContext, task, hidden);
            printContext.setCurrentY(printContext.getCurrentY() + printArea.getHeight());
         } else if(task.getTaskCode().equals("printTableItems")) {
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

   public Rectangle2D printHeader(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      HeaderPrintableTable table = new HeaderPrintableTable("Customer Statement", false, printContext);
      table.initialise(printContext.getGraphics());
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), tableProgress, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printStatementInfo(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      return new Double(printContext.getStartX(), printContext.getCurrentY(), printContext.getWidth(), 0.0D);
   }

   public Rectangle2D printCustomerDetails(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      PrintContext addressContext = printContext.createClone();
      addressContext.setWidth(addressContext.getWidth() / 2.0D);
      Rectangle2D addressBounds = this.printCustomerAddress(addressContext, task, hidden);
      PrintContext contactsContext = printContext.createClone();
      contactsContext.setWidth(contactsContext.getWidth() / 2.0D);
      contactsContext.setStartX(contactsContext.getStartX() + contactsContext.getWidth());
      Rectangle2D contactsBounds = this.printCustomerContactDetails(contactsContext, task, hidden);
      addressBounds.add(contactsBounds);
      return addressBounds;
   }

   private Rectangle2D printCustomerAddress(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      PrintableTable addressTable = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, (String)null, (Font)null, (Color)null, (String)null, addressTable);
      TableColumn column2 = new TableColumn(1.0D, (String)null, (Font)null, (Color)null, (String)null, addressTable);
      TableBlock addressBlock = new TableBlock(addressTable);
      DefaultTableRow nameRow = new DefaultTableRow(addressBlock);
      new TextTableCell("Account Holder:", nameRow, column1, 1, this.customerDetailsHeadingFont, Color.black);
      new TextTableCell(task.getStatementDetails().getCustomer().getFullName(), nameRow, column2, 1, this.customerDetailsFont, Color.black);
      List addressLines = task.getStatementDetails().getAddress().getNamelessLines();

      for(int tableProgress = 0; tableProgress < addressLines.size(); ++tableProgress) {
         DefaultTableRow addressLineRow = new DefaultTableRow(addressBlock);
         new TextTableCell(addressLines.get(tableProgress), addressLineRow, column2, 1, this.customerDetailsFont, Color.BLACK);
      }

      addressTable.initialise(printContext.getGraphics());
      PrintProgress var12 = new PrintProgress();
      var12.setTotalParts(addressTable.getBlocks().size());
      return addressTable.print(printContext.getGraphics(), var12, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   private Rectangle2D printCustomerContactDetails(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      PrintableTable table = new PrintableTable(printContext.getStartX(), printContext.getWidth(), 10.0D);
      TableColumn column1 = new TableColumn(0.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableColumn column2 = new TableColumn(1.0D, (String)null, (Font)null, (Color)null, (String)null, table);
      TableBlock block = new TableBlock(table);
      DefaultTableRow customerNumberRow = new DefaultTableRow(block);
      new TextTableCell("Customer Number:", customerNumberRow, column1, 1, this.customerDetailsHeadingFont, Color.black);
      new TextTableCell(String.valueOf(task.getStatementDetails().getCustomer().getObjectId()), customerNumberRow, column2, 1, this.customerDetailsFont, Color.black);
      String phone = task.getStatementDetails().getCustomer().getPhone();
      if(phone != null && phone.trim().length() > 0) {
         DefaultTableRow fax = new DefaultTableRow(block);
         new TextTableCell("Phone:", fax, column1, 1, this.customerDetailsHeadingFont, Color.black);
         new TextTableCell(phone.trim(), fax, column2, 1, this.customerDetailsFont, Color.black);
      }

      String fax1 = task.getStatementDetails().getCustomer().getFax();
      if(fax1 != null && fax1.trim().length() > 0) {
         DefaultTableRow email = new DefaultTableRow(block);
         new TextTableCell("Fax:", email, column1, 1, this.customerDetailsHeadingFont, Color.black);
         new TextTableCell(fax1.trim(), email, column2, 1, this.customerDetailsFont, Color.black);
      }

      String email1 = task.getStatementDetails().getCustomer().getEmail2();
      if(email1 == null || email1.trim().length() == 0) {
         email1 = task.getStatementDetails().getCustomer().getLoginEmail();
      }

      if(email1 != null && email1.trim().length() > 0) {
         DefaultTableRow tableProgress = new DefaultTableRow(block);
         new TextTableCell("Email:", tableProgress, column1, 1, this.customerDetailsHeadingFont, Color.black);
         new TextTableCell(email1.trim(), tableProgress, column2, 1, this.customerDetailsFont, Color.black);
      }

      table.initialise(printContext.getGraphics());
      PrintProgress tableProgress1 = new PrintProgress();
      tableProgress1.setTotalParts(table.getBlocks().size());
      return table.print(printContext.getGraphics(), tableProgress1, printContext.getCurrentY(), hidden, printContext.getMaxHeight());
   }

   public Rectangle2D printTableHeader(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      CustomerStatementPrintableTable table = task.getTable();
      return table.printColumnTitles(printContext.getGraphics(), printContext.getCurrentY(), hidden);
   }

   public Rectangle2D printTable(PrintContext printContext, CustomerStatementPrintTask task, boolean hidden) throws PrinterException {
      double currentY = printContext.getCurrentY();
      CustomerStatementPrintableTable table = task.getTable();
      PrintProgress tableProgress = new PrintProgress();
      tableProgress.setTotalParts(table.getBlocks().size());
      tableProgress.setPartsCompleted(task.getStartIndex());
      double heightRemaining = printContext.getMaxHeight() - currentY;
      currentY += table.print(printContext.getGraphics(), tableProgress, currentY, hidden, heightRemaining).getHeight();
      return new Double(printContext.getCurrentY(), printContext.getStartX(), printContext.getWidth(), currentY - printContext.getCurrentY());
   }

   public Rectangle2D printFooter(PrintContext printContext, CustomerStatementPrintTask task, boolean finalPage, boolean hidden) throws PrinterException {
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
