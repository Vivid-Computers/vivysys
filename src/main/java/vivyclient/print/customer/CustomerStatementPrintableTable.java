package vivyclient.print.customer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import vivyclient.error.ErrorWriter;
import vivyclient.exception.AppRuntimeException;
import vivyclient.model.Customer;
import vivyclient.model.Payment;
import vivyclient.model.PaymentMethodType;
import vivyclient.model.Sale;
import vivyclient.print.PrintContext;
import vivyclient.print.customer.CustomerStatement;
import vivyclient.print.table.BlankTableCell;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.shared.Constants;
import vivyclient.util.Util;
import vivyclient.util.ViewUtil;

public class CustomerStatementPrintableTable extends PrintableTable {
   private static final double COLUMN_DIVIDER_WIDTH = 5.0D;
   private static final double COLUMN_TITLES_INTERNAL_Y_PADDING = 5.0D;
   private Font tableTitleFont = new Font("Arial", 1, 10);
   private Font tableContentFont = new Font("Arial", 0, 10);
   private Font closingBalanceFont = new Font("Arial", 1, 12);
   private Font overdueFont = new Font("Arial", 1, 12);
   private Font overdueCommentFont = new Font("Arial", 2, 9);
   private CustomerStatement details;

   public CustomerStatementPrintableTable(CustomerStatement details, PrintContext printContext) throws PrinterException {
      super(printContext.getStartX(), printContext.getWidth(), 5.0D);
      this.details = details;
      this.setup();
   }

   private void setup() throws PrinterException {
      try {
         TableColumn e = new TableColumn(0.0D, this);
         TableColumn column1 = new TableColumn(0.0D, "Date", this.tableTitleFont, Color.WHITE, "C", this);
         column1.setAlign("W");
         TableColumn column2 = new TableColumn(1.0D, "Transaction", this.tableTitleFont, Color.WHITE, "W", this);
         column2.setAlign("W");
         TableColumn column3 = new TableColumn(0.0D, "Debit", this.tableTitleFont, Color.WHITE, "C", this);
         column3.setAlign("E");
         TableColumn column4 = new TableColumn(0.0D, "Credit", this.tableTitleFont, Color.WHITE, "C", this);
         column4.setAlign("E");
         TableColumn column5 = new TableColumn(0.0D, "Balance", this.tableTitleFont, Color.WHITE, "C", this);
         column5.setAlign("E");
         TableColumn eastMarginColumn = new TableColumn(0.0D, this);
         Customer customer = this.details.getCustomer();
         BigDecimal overdueBalance = Constants.ZERO_BIG_DECIMAL;
         BigDecimal runningBalance = customer.getOpeningBalance();
         if(customer.getOpeningBalanceDueDate() != null && Util.getStartOfToday().after(customer.getOpeningBalanceDueDate())) {
            overdueBalance = overdueBalance.add(customer.getOpeningBalance());
         }

         Iterator openingBalanceRow = this.details.getOpeningSalesList().iterator();

         while(openingBalanceRow.hasNext()) {
            Sale totalsBlock = (Sale)openingBalanceRow.next();
            BigDecimal closingBalanceRow = totalsBlock.getTotalCost();
            runningBalance = runningBalance.add(closingBalanceRow);
            if(totalsBlock.paymentOverdue()) {
               overdueBalance = overdueBalance.add(closingBalanceRow);
            }
         }

         BigDecimal totalsBlock1;
         for(openingBalanceRow = this.details.getOpeningPaymentsList().iterator(); openingBalanceRow.hasNext(); overdueBalance = overdueBalance.subtract(totalsBlock1)) {
            totalsBlock1 = ((Payment)openingBalanceRow.next()).getAmount();
            runningBalance = runningBalance.subtract(totalsBlock1);
         }

         DefaultTableRow openingBalanceRow1 = new DefaultTableRow(new TableBlock(this));
         new BlankTableCell(openingBalanceRow1, e, 1, 5.0D, 1.0D, (Color)null);
         new TextTableCell(ViewUtil.calendarDisplay(this.details.getStatementFromDate()), openingBalanceRow1, column1, 1, this.tableContentFont, Color.black);
         new TextTableCell("Opening Balance", openingBalanceRow1, column2, 1, this.tableContentFont, Color.black);
         new TextTableCell(ViewUtil.currencyDisplay(runningBalance), openingBalanceRow1, column5, 1, this.tableContentFont, Color.black);
         new BlankTableCell(openingBalanceRow1, eastMarginColumn, 1, 5.0D, 1.0D, (Color)null);

         DefaultTableRow row;
         for(Iterator totalsBlock2 = this.details.getCombinedList().iterator(); totalsBlock2.hasNext(); new TextTableCell(ViewUtil.balanceCurrencyDisplay(runningBalance), row, column5, 1, this.tableContentFont, Color.BLACK)) {
            Object closingBalanceRow1 = totalsBlock2.next();
            Calendar overdueRow;
            String overdueCommentRow;
            String details;
            BigDecimal debit;
            BigDecimal credit;
            if(!(closingBalanceRow1 instanceof Sale)) {
               if(!(closingBalanceRow1 instanceof Payment)) {
                  throw new AppRuntimeException();
               }

               Payment block1 = (Payment)closingBalanceRow1;
               overdueRow = block1.getDate();
               overdueCommentRow = "Payment " + block1.getObjectId() + " - " + PaymentMethodType.findPopulatedPaymentMethodType(block1.getMethod()).getName();
               details = "TODO";
               debit = null;
               credit = block1.getAmount();
               runningBalance = runningBalance.subtract(credit);
               overdueBalance = overdueBalance.subtract(credit);
            } else {
               Sale block = (Sale)closingBalanceRow1;
               overdueRow = block.getSaleDate();
               overdueCommentRow = "Invoice " + block.getObjectId() + (block.getCustref() != null && block.getCustref().trim().length() > 0?" - Ref " + block.getCustref():"");
               details = "TODO";
               debit = block.getTotalCost();
               credit = null;
               runningBalance = runningBalance.add(debit);
               if(block.paymentOverdue()) {
                  overdueBalance = overdueBalance.add(debit);
               }
            }

            TableBlock block2 = new TableBlock(this);
            row = new DefaultTableRow(block2);
            new TextTableCell(ViewUtil.calendarDisplay(overdueRow), row, column1, 1, this.tableContentFont, Color.BLACK);
            new TextTableCell(overdueCommentRow, row, column2, 1, this.tableContentFont, Color.BLACK);
            if(debit != null) {
               new TextTableCell(ViewUtil.currencyDisplay(debit), row, column3, 1, this.tableContentFont, Color.BLACK);
            }

            if(credit != null) {
               new TextTableCell(ViewUtil.currencyDisplay(credit), row, column4, 1, this.tableContentFont, Color.BLACK);
            }
         }

         TableBlock totalsBlock3 = new TableBlock(this);
         new BlankTableRow(totalsBlock3, (Color)null, 10.0D);
         new BlankTableCell(new DefaultTableRow(totalsBlock3), column5, 1, 1.0D, 1.0D, Color.black);
         new BlankTableRow(totalsBlock3, (Color)null, 5.0D);
         DefaultTableRow closingBalanceRow2 = new DefaultTableRow(totalsBlock3);
         new TextTableCell("Closing Balance", closingBalanceRow2, column2, 1, this.closingBalanceFont, Color.BLACK);
         new TextTableCell(ViewUtil.balanceCurrencyDisplay(runningBalance), closingBalanceRow2, column5, 1, this.overdueFont, Color.BLACK);
         new BlankTableRow(totalsBlock3, (Color)null, 5.0D);
         if(overdueBalance.compareTo(Constants.ZERO_BIG_DECIMAL) > 0) {
            DefaultTableRow overdueRow1 = new DefaultTableRow(totalsBlock3);
            new TextTableCell("OVERDUE AMOUNT", overdueRow1, column2, 1, this.overdueFont, Color.BLACK);
            new TextTableCell(ViewUtil.currencyDisplay(overdueBalance), overdueRow1, column5, 1, this.overdueFont, Color.BLACK);
            DefaultTableRow overdueCommentRow1 = new DefaultTableRow(totalsBlock3);
            new TextTableCell("Please pay immediately", overdueCommentRow1, column2, 1, this.overdueCommentFont, Color.BLACK);
         }

      } catch (Exception var21) {
         ErrorWriter.writeException(var21);
         throw new PrinterException("Root cause: \"" + var21.getMessage() + "\"");
      }
   }

   public Rectangle2D printColumnTitles(Graphics2D g, double startY, boolean hidden) {
      double height = super.printColumnTitles(g, startY + 5.0D, true).getHeight();
      height += 10.0D;
      if(!hidden) {
         g.setColor(Color.BLACK);
         g.fillRect((int)this.getStartX(), (int)startY, (int)this.getWidth(), (int)height);
         super.printColumnTitles(g, startY + 5.0D, false);
      }

      return new Double(this.getStartX(), startY, this.getWidth(), height);
   }
}
