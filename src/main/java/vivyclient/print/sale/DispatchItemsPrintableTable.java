package vivyclient.print.sale;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import vivyclient.error.ErrorWriter;
import vivyclient.exception.AppRuntimeException;
import vivyclient.model.Dispatch;
import vivyclient.model.DispatchDetail;
import vivyclient.print.PrintContext;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;
import vivyclient.util.ViewUtil;

public class DispatchItemsPrintableTable extends PrintableTable {
   private static final double COLUMN_DIVIDER_WIDTH = 5.0D;
   private static final double COLUMN_TITLES_INTERNAL_Y_PADDING = 5.0D;
   private Font dispatchLineTitleFont = new Font("Arial", 1, 10);
   private Font dispatchLineFont = new Font("Arial", 0, 10);
   private Font dispatchQuantityFont = new Font("Arial", 1, 10);
   private Dispatch dispatch;

   public DispatchItemsPrintableTable(Dispatch dispatch, PrintContext printContext) throws PrinterException {
      super(printContext.getStartX(), printContext.getWidth(), 5.0D);
      this.dispatch = dispatch;
      this.setup();
   }

   private void setup() throws PrinterException {
      try {
         TableColumn e = new TableColumn(0.0D, " Code", this.dispatchLineTitleFont, Color.WHITE, "C", this);
         e.setAlign("E");
         TableColumn column2 = new TableColumn(1.0D, "Item", this.dispatchLineTitleFont, Color.WHITE, "W", this);
         column2.setAlign("W");
         TableColumn column3 = new TableColumn(0.0D, "Ordered", this.dispatchLineTitleFont, Color.WHITE, "C", this);
         column3.setAlign("E");
         TableColumn column4 = new TableColumn(0.0D, "Previous", this.dispatchLineTitleFont, Color.WHITE, "C", this);
         column4.setAlign("E");
         TableColumn column5 = new TableColumn(0.0D, "This Dispatch", this.dispatchLineTitleFont, Color.WHITE, "C", this);
         column5.setAlign("E");
         TableColumn column6 = new TableColumn(0.0D, "Back Order ", this.dispatchLineTitleFont, Color.WHITE, "C", this);
         column6.setAlign("E");

         for(int i = 0; i < this.dispatch.getDispatchContentCount(); ++i) {
            DispatchDetail line = this.dispatch.getDispatchContent(i);
            TableBlock block = new TableBlock(this);
            DefaultTableRow row = new DefaultTableRow(block);
            new TextTableCell(String.valueOf(line.getShippedSaleDetail().getProduct().getObjectId()), row, e, 1, this.dispatchLineFont, Color.BLACK);
            new TextTableCell(line.getShippedSaleDetail().getProduct().getName(), row, column2, 1, this.dispatchLineFont, Color.BLACK);
            new TextTableCell(ViewUtil.decimalDisplay(line.getShippedSaleDetail().getQuantity()) + " ", row, column3, 1, this.dispatchLineFont, Color.BLACK);
            new TextTableCell(ViewUtil.decimalDisplay(line.getShippedSaleDetail().getDispatchedQuantity()) + " ", row, column4, 1, this.dispatchLineFont, Color.BLACK);
            new TextTableCell(ViewUtil.decimalDisplay(line.getQuantity()) + " ", row, column5, 1, this.dispatchQuantityFont, Color.BLACK);
            if(line.getShippedSaleDetail().getQuantity() == null) {
               throw new AppRuntimeException();
            }

            if(line.getShippedSaleDetail().getDispatchedQuantity() == null) {
               throw new AppRuntimeException();
            }

            BigDecimal backOrdered = line.getShippedSaleDetail().getQuantity().subtract(line.getShippedSaleDetail().getDispatchedQuantity().add(line.getQuantity()));
            new TextTableCell(ViewUtil.decimalDisplay(backOrdered) + " ", row, column6, 1, this.dispatchLineFont, Color.BLACK);
            if(line.getSerialNumbers() != null && line.getSerialNumbers().trim().length() > 0) {
               new DefaultTableRow(block);
               new TextTableCell(line.getSerialNumbers(), row, column2, 1, this.dispatchLineFont, Color.BLACK);
            }
         }

      } catch (Exception var13) {
         ErrorWriter.writeException(var13);
         throw new PrinterException("Holding Exception \"" + var13.getMessage() + "\"");
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
