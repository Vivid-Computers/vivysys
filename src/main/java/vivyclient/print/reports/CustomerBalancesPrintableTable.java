package vivyclient.print.reports;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.util.Iterator;
import vivyclient.error.ErrorWriter;
import vivyclient.model.Customer;
import vivyclient.print.PrintContext;
import vivyclient.print.reports.CustomerBalances;
import vivyclient.print.table.BlankTableCell;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.shared.Constants;
import vivyclient.util.ViewUtil;

public class CustomerBalancesPrintableTable extends PrintableTable {
   private static final double COLUMN_DIVIDER_WIDTH = 5.0D;
   private static final double COLUMN_TITLES_INTERNAL_Y_PADDING = 5.0D;
   private Font tableTitleFont = new Font("Arial", 1, 10);
   private Font tableContentFont = new Font("Arial", 0, 10);
   private Font totalsFont = new Font("Arial", 1, 12);
   private CustomerBalances details;

   public CustomerBalancesPrintableTable(CustomerBalances details, PrintContext printContext) throws PrinterException {
      super(printContext.getStartX(), printContext.getWidth(), 5.0D);
      this.details = details;
      this.setup();
   }

   private void setup() throws PrinterException {
      try {
         TableColumn e = new TableColumn(0.0D, this);
         TableColumn column1 = new TableColumn(0.0D, "Id", this.tableTitleFont, Color.WHITE, "C", this);
         column1.setAlign("W");
         TableColumn column2 = new TableColumn(1.0D, "Name", this.tableTitleFont, Color.WHITE, "W", this);
         column2.setAlign("W");
         TableColumn column3 = new TableColumn(0.0D, "Sales", this.tableTitleFont, Color.WHITE, "C", this);
         column3.setAlign("E");
         TableColumn column4 = new TableColumn(0.0D, "Payments", this.tableTitleFont, Color.WHITE, "C", this);
         column4.setAlign("E");
         TableColumn column5 = new TableColumn(0.0D, "Overdue Balance", this.tableTitleFont, Color.WHITE, "C", this);
         column5.setAlign("E");
         TableColumn column6 = new TableColumn(0.0D, "Balance", this.tableTitleFont, Color.WHITE, "C", this);
         column6.setAlign("E");
         TableColumn column7 = new TableColumn(0.0D, "Limit", this.tableTitleFont, Color.WHITE, "C", this);
         column7.setAlign("E");
         TableColumn eastMarginColumn = new TableColumn(0.0D, this);
         BigDecimal overdueBalanceTotal = Constants.ZERO_BIG_DECIMAL;
         BigDecimal netBalanceTotal = Constants.ZERO_BIG_DECIMAL;
         BigDecimal balancesDueTotal = Constants.ZERO_BIG_DECIMAL;
         Iterator totalsBlock = this.details.getCustomers().iterator();

         while(true) {
            Customer balancesRow;
            do {
               if(!totalsBlock.hasNext()) {
                  TableBlock totalsBlock1 = new TableBlock(this);
                  new BlankTableRow(totalsBlock1, (Color)null, 10.0D);
                  new BlankTableRow(totalsBlock1, (Color)null, 20.0D);
                  DefaultTableRow balancesRow1 = new DefaultTableRow(totalsBlock1);
                  new TextTableCell("TOTAL OVERDUE BALANCES", balancesRow1, column2, 3, this.totalsFont, Color.BLACK);
                  new TextTableCell(ViewUtil.currencyDisplay(overdueBalanceTotal), balancesRow1, column5, 2, this.totalsFont, Color.BLACK);
                  if(!this.details.getShowType().equals("overdue")) {
                     new BlankTableRow(totalsBlock1, (Color)null, 5.0D);
                     balancesRow1 = new DefaultTableRow(totalsBlock1);
                     new TextTableCell("TOTAL BALANCES DUE", balancesRow1, column2, 3, this.totalsFont, Color.BLACK);
                     new TextTableCell(ViewUtil.currencyDisplay(balancesDueTotal), balancesRow1, column5, 2, this.totalsFont, Color.BLACK);
                  }

                  if(this.details.getShowType().equals("all")) {
                     new BlankTableRow(totalsBlock1, (Color)null, 5.0D);
                     balancesRow1 = new DefaultTableRow(totalsBlock1);
                     new TextTableCell("TOTAL NET BALANCES DUE", balancesRow1, column2, 3, this.totalsFont, Color.BLACK);
                     new TextTableCell(ViewUtil.currencyDisplay(netBalanceTotal), balancesRow1, column5, 2, this.totalsFont, Color.BLACK);
                  }

                  return;
               }

               balancesRow = (Customer)totalsBlock.next();
            } while(!this.details.getShowType().equals("all") && (!this.details.getShowType().equals("debt") || balancesRow.getBalance().compareTo(Constants.ZERO_BIG_DECIMAL) <= 0) && (!this.details.getShowType().equals("overdue") || balancesRow.getOverdueBalance().compareTo(Constants.ZERO_BIG_DECIMAL) <= 0));

            DefaultTableRow customerRow = new DefaultTableRow(new TableBlock(this));
            new BlankTableCell(customerRow, e, 1, 5.0D, 1.0D, (Color)null);
            new TextTableCell(String.valueOf(balancesRow.getObjectId()), customerRow, column1, 1, this.tableContentFont, Color.black);
            new TextTableCell(balancesRow.getDefaultDeliveryName(), customerRow, column2, 1, this.tableContentFont, Color.black);
            new TextTableCell(ViewUtil.currencyDisplay(balancesRow.getSaleTotal()), customerRow, column3, 1, this.tableContentFont, Color.black);
            new TextTableCell(ViewUtil.currencyDisplay(balancesRow.getPaymentTotal()), customerRow, column4, 1, this.tableContentFont, Color.black);
            if(balancesRow.getOverdueBalance().compareTo(Constants.ZERO_BIG_DECIMAL) > 0) {
               new TextTableCell(ViewUtil.balanceCurrencyDisplay(balancesRow.getOverdueBalance()), customerRow, column5, 1, this.tableContentFont, Color.black);
               overdueBalanceTotal = overdueBalanceTotal.add(balancesRow.getOverdueBalance());
            }

            new TextTableCell(ViewUtil.balanceCurrencyDisplay(balancesRow.getBalance()), customerRow, column6, 1, this.tableContentFont, Color.black);
            netBalanceTotal = netBalanceTotal.add(balancesRow.getBalance());
            if(balancesRow.getBalance().compareTo(Constants.ZERO_BIG_DECIMAL) > 0) {
               balancesDueTotal = balancesDueTotal.add(balancesRow.getBalance());
            }

            if(balancesRow.getCreditLimit().compareTo(Constants.ZERO_BIG_DECIMAL) > 0) {
               new TextTableCell(ViewUtil.currencyDisplay(balancesRow.getCreditLimit()), customerRow, column7, 1, this.tableContentFont, Color.black);
            }

            new BlankTableCell(customerRow, eastMarginColumn, 1, 5.0D, 1.0D, (Color)null);
         }
      } catch (Exception var16) {
         ErrorWriter.writeException(var16);
         throw new PrinterException("Root cause: \"" + var16.getMessage() + "\"");
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
