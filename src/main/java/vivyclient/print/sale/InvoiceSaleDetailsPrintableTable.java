package vivyclient.print.sale;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.util.Calendar;
import vivyclient.data.TransactionContainer;
import vivyclient.error.ErrorWriter;
import vivyclient.model.DurationType;
import vivyclient.model.ProductMakeup;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.print.table.BlankTableCell;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class InvoiceSaleDetailsPrintableTable extends PrintableTable {
   private Sale sale;
   private Font saleLineTitleFont = new Font("Arial", 1, 10);
   private Font saleLineFont = new Font("Arial", 0, 10);
   private Font saleLinePartFont = new Font("Arial", 0, 8);
   private Font saleLineCommentsFont = new Font("Arial", 2, 8);
   private Font totalsFont = new Font("Arial", 0, 11);
   private Font grandTotalFont = new Font("Arial", 1, 11);
   private Font paymentTermsFont = new Font("Arial", 1, 9);
   private Font conditionsFont = new Font("Arial", 0, 9);
   private static final double COLUMN_TITLES_INTERNAL_Y_PADDING = 5.0D;

   public InvoiceSaleDetailsPrintableTable(Sale sale, double startX, double width, double columnDivider) throws PrinterException {
      super(startX, width, columnDivider);
      this.sale = sale;
      this.setup();
   }

   private void setup() throws PrinterException {
      try {
         TableColumn e = new TableColumn(0.0D, " Code", this.saleLineTitleFont, Color.WHITE, "C", this);
         e.setAlign("E");
         TableColumn column2 = new TableColumn(1.0D, "Item", this.saleLineTitleFont, Color.WHITE, "W", this);
         column2.setAlign("W");
         TableColumn column3 = new TableColumn(0.0D, "Warranty", this.saleLineTitleFont, Color.WHITE, "C", this);
         column3.setAlign("W");
         TableColumn column4 = new TableColumn(0.0D, "Quantity", this.saleLineTitleFont, Color.WHITE, "C", this);
         column4.setAlign("E");
         TableColumn column5 = new TableColumn(0.0D, "Unit Price", this.saleLineTitleFont, Color.WHITE, "C", this);
         column5.setAlign("E");
         TableColumn column6 = new TableColumn(0.0D, "Extended ", this.saleLineTitleFont, Color.WHITE, "C", this);
         column6.setAlign("E");

         TableBlock totalsBlock;
         DefaultTableRow subTotalRow;
         DefaultTableRow var26;
         for(int block = 0; block < this.sale.getSaleLineCount(); ++block) {
            SaleDetail row = this.sale.getSaleLine(block);
            totalsBlock = new TableBlock(this);
            DefaultTableRow fillerRow = new DefaultTableRow(totalsBlock);
            new TextTableCell(String.valueOf(row.getProduct().getObjectId()), fillerRow, e, 1, this.saleLineFont, Color.BLACK);
            new TextTableCell(String.valueOf(row.getProduct().getName()), fillerRow, column2, 1, this.saleLineFont, Color.BLACK);
            if(row.getWarrantyDuration() != null) {
               String lineRow;
               if(row.getWarrantyDuration().equals(DurationType.LIFETIME_DURATION_TYPE)) {
                  lineRow = row.getWarrantyDuration().getShortDisplayLetter();
               } else {
                  lineRow = row.getWarrantyDurationMultiplier() + row.getWarrantyDuration().getShortDisplayLetter();
               }

               if(row.getWarrantyComments() != null && row.getWarrantyComments().trim().length() > 0) {
                  lineRow = lineRow + " - " + row.getWarrantyComments();
               }

               new TextTableCell(lineRow, fillerRow, column3, 1, this.saleLineFont, Color.BLACK);
            }

            new TextTableCell(ViewUtil.decimalDisplay(row.getQuantity()), fillerRow, column4, 1, this.saleLineFont, Color.BLACK);
            new TextTableCell(ViewUtil.currencyDisplay(row.getUnitPrice()), fillerRow, column5, 1, this.saleLineFont, Color.BLACK);
            new TextTableCell(ViewUtil.currencyDisplay(row.getLineTotal()), fillerRow, column6, 1, this.saleLineFont, Color.BLACK);

            for(int var25 = 0; var25 < row.getProduct().getPartCount(); ++var25) {
               ProductMakeup lineCell = row.getProduct().getPart(var25);
               subTotalRow = new DefaultTableRow(totalsBlock);
               new TextTableCell(String.valueOf(lineCell.getPart().getObjectId()), subTotalRow, e, 1, this.saleLinePartFont, Color.BLACK);
               new TextTableCell(lineCell.getPart().getName(), subTotalRow, column2, 1, this.saleLinePartFont, Color.BLACK);
               if(lineCell.getWarrantyDurationType() != null) {
                  String subTotalNameCell;
                  if(lineCell.getWarrantyDurationType().equals(DurationType.LIFETIME_DURATION_TYPE)) {
                     subTotalNameCell = lineCell.getWarrantyDurationType().getShortDisplayLetter();
                  } else {
                     subTotalNameCell = lineCell.getWarrantyDurationMultiplier() + lineCell.getWarrantyDurationType().getShortDisplayLetter();
                  }

                  if(lineCell.getWarrantyComments() != null && lineCell.getWarrantyComments().trim().length() > 0) {
                     subTotalNameCell = subTotalNameCell + " - " + lineCell.getWarrantyComments();
                  }

                  new TextTableCell(subTotalNameCell, subTotalRow, column3, 1, this.saleLinePartFont, Color.BLACK);
               }

               new TextTableCell(String.valueOf(lineCell.getQuantity()), subTotalRow, column4, 1, this.saleLinePartFont, Color.BLACK);
            }

            if(row.getComments() != null) {
               var26 = new DefaultTableRow(totalsBlock);
               new TextTableCell(row.getComments(), var26, column2, 1, this.saleLineCommentsFont, Color.BLACK);
            }
         }

         TableBlock var23 = new TableBlock(this);
         DefaultTableRow var24 = new DefaultTableRow(var23);
         new TextTableCell("Freight", var24, column2, 1, this.saleLineFont, Color.BLACK);
         new TextTableCell(ViewUtil.currencyDisplay(this.sale.getFreightCostForAddition()), var24, column6, 1, this.saleLineFont, Color.BLACK);
         totalsBlock = new TableBlock(this);
         new BlankTableRow(totalsBlock, (Color)null, 1.0D);
         var26 = new DefaultTableRow(totalsBlock);
         new BlankTableCell(var26, column6, 1, 0.0D, 1.0D, Color.BLACK);
         new BlankTableRow(totalsBlock, (Color)null, 3.0D);
         subTotalRow = new DefaultTableRow(totalsBlock);
         TextTableCell var27 = new TextTableCell("SubTotal", subTotalRow, column2, 1, this.saleLineFont, Color.BLACK);
         var27.setAlign("E");
         new TextTableCell(ViewUtil.currencyDisplay(this.sale.getSubTotal()), subTotalRow, column6, 1, this.saleLineFont, Color.BLACK);
         DefaultTableRow gstRow = new DefaultTableRow(totalsBlock);
         TextTableCell gstNameCell = new TextTableCell("GST", gstRow, column2, 1, this.saleLineFont, Color.BLACK);
         gstNameCell.setAlign("E");
         new TextTableCell(ViewUtil.currencyDisplay(this.sale.getGSTCost()), gstRow, column6, 1, this.saleLineFont, Color.BLACK);
         new BlankTableRow(totalsBlock, (Color)null, 1.0D);
         var26 = new DefaultTableRow(totalsBlock);
         new BlankTableCell(var26, column6, 1, 0.0D, 2.0D, Color.BLACK);
         new BlankTableRow(totalsBlock, (Color)null, 3.0D);
         DefaultTableRow totalRow = new DefaultTableRow(totalsBlock);
         TextTableCell totalNameCell = new TextTableCell("Total", totalRow, column2, 1, this.grandTotalFont, Color.BLACK);
         totalNameCell.setAlign("E");
         new TextTableCell(ViewUtil.currencyDisplay(this.sale.getTotalCost()), totalRow, column6, 1, this.grandTotalFont, Color.BLACK);
         new BlankTableRow(totalsBlock, (Color)null, 10.0D);
         if(this.sale.getPaymentDurationType() != null) {
            DefaultTableRow conditions = new DefaultTableRow(totalsBlock);
            TextTableCell conditionsRow = new TextTableCell(this.getPaymentTerms(), conditions, e, 6, this.paymentTermsFont, Color.BLACK);
            conditionsRow.setAlign("C");
            new BlankTableRow(totalsBlock, (Color)null, 3.0D);
         }

         String var28 = "Warranty durations for goods are specified in Days (D), Months (M), Years (Y), or Lifetime (L).  All goods remain the property of Vivid Computers Ltd until complete payment has been received.";
         DefaultTableRow var29 = new DefaultTableRow(totalsBlock);
         TextTableCell termsCell = new TextTableCell(var28, var29, e, 6, this.conditionsFont, Color.BLACK);
         termsCell.setAlign("W");
      } catch (Exception var22) {
         ErrorWriter.writeException(var22);
         throw new PrinterException("Holding Exception \"" + var22.getMessage() + "\"");
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

   public Sale getSale() {
      return this.sale;
   }

   public void setSale(Sale sale) {
      this.sale = sale;
   }

   private String getPaymentTerms() throws Exception {
      Calendar dueDate = Calendar.getInstance();
      dueDate.setTime(this.sale.getSaleDate().getTime());
      dueDate.setLenient(true);
      int multiplier = this.sale.getPaymentTermsMultiplier();
      DurationType durationType = this.sale.getPaymentDurationType();
      if(durationType.equals(DurationType.DAYS_DURATION_TYPE)) {
         dueDate.add(5, multiplier);
      } else if(durationType.equals(DurationType.MONTHS_DURATION_TYPE)) {
         dueDate.add(2, multiplier);
      } else if(durationType.equals(DurationType.YEARS_DURATION_TYPE)) {
         dueDate.add(1, multiplier);
      } else if(durationType.equals(DurationType.BILLING_MONTH_DURATION_TYPE)) {
         dueDate.add(2, multiplier);
         dueDate.set(5, 20);
      }

      if(this.sale.getPaymentDurationType().getName() == null) {
         this.sale.getPaymentDurationType().populate((TransactionContainer)null);
      }

      return "Payment terms are " + this.sale.getPaymentTermsMultiplier() + " " + this.sale.getPaymentDurationType().getName() + " from the Invoice Date.  Payment should be complete by " + ViewUtil.calendarDisplay(dueDate) + ".";
   }
}
