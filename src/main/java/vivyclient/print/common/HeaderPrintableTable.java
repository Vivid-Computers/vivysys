package vivyclient.print.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.print.PrinterException;
import vivyclient.print.PrintContext;
import vivyclient.print.common.LogoTableCell;
import vivyclient.print.table.BlankTableRow;
import vivyclient.print.table.DefaultTableRow;
import vivyclient.print.table.NestedTableCell;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TextTableCell;

public class HeaderPrintableTable extends PrintableTable {
   private static final double COLUMN_DIVIDER_WIDTH = 0.0D;
   private static Font vividFont = new Font("Century Gothic", 0, 30);
   public static Font defaultTitleFont = new Font("Arial", 1, 15);
   private static Font gstFont = new Font("Arial", 1, 10);
   private static Font addressFont = new Font("Arial", 0, 10);
   private String title;
   private boolean showGSTNumber;

   public HeaderPrintableTable(String title, boolean showGSTNumber, PrintContext printContext) throws PrinterException {
      super(printContext.getStartX(), printContext.getWidth(), 0.0D);
      this.title = title;
      this.showGSTNumber = showGSTNumber;
      this.setup();
   }

   private void setup() {
      TableColumn column1 = new TableColumn(0.0D, this);
      column1.setAlign("W");
      TableColumn column2 = new TableColumn(0.5D, this);
      column2.setAlign("W");
      TableColumn column3 = new TableColumn(0.5D, this);
      column3.setAlign("C");
      TableColumn column4 = new TableColumn(0.0D, this);
      column4.setAlign("E");
      TableBlock block = new TableBlock(this);
      DefaultTableRow topRow = new DefaultTableRow(block, "S");
      new LogoTableCell(topRow, column1, 1, 75.0D, Color.black);
      new TextTableCell("vivid computers", topRow, column2, 2, vividFont, Color.black);
      if(this.showGSTNumber) {
         PrintableTable addressRow1 = new PrintableTable(0.0D, Double.MAX_VALUE, 0.0D);
         TableColumn addressRow2 = new TableColumn(1.0D, addressRow1);
         addressRow1.setAlign("C");
         TableBlock addressRow3 = new TableBlock(addressRow1);
         DefaultTableRow titleRow = new DefaultTableRow(addressRow3);
         new TextTableCell(this.title, titleRow, addressRow2, 1, defaultTitleFont, Color.black);
         DefaultTableRow gstRow = new DefaultTableRow(addressRow3);
         new TextTableCell("GST # 58 002 615", gstRow, addressRow2, 1, gstFont, Color.black);
         new NestedTableCell(addressRow1, topRow, column4, 1);
      } else {
         new TextTableCell(this.title, topRow, column4, 1, defaultTitleFont, Color.black);
      }

      new BlankTableRow(block, (Color)null, 5.0D);
      DefaultTableRow addressRow11 = new DefaultTableRow(block);
      new TextTableCell("8a Lincoln Road, Henderson", addressRow11, column1, 2, addressFont, Color.black);
      DefaultTableRow addressRow21 = new DefaultTableRow(block);
      new TextTableCell("PO Box 95156 Swanson, AUCKLAND", addressRow21, column1, 2, addressFont, Color.black);
      (new TextTableCell("www.vivid.net.nz", addressRow21, column3, 2, addressFont, Color.black)).setAlign("E");
      DefaultTableRow addressRow31 = new DefaultTableRow(block);
      new TextTableCell("Phone 64 9 838 0030 Fax 64 9 838 0031", addressRow31, column1, 2, addressFont, Color.black);
      (new TextTableCell("sales@vivid.net.nz", addressRow31, column3, 2, addressFont, Color.black)).setAlign("E");
   }
}
