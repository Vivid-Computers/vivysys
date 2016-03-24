package vivyclient.print.table;

import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;
import vivyclient.print.table.TableRow;

public abstract class AbstractTableCell extends TableRegion implements TableCell {
   private int xSpan;
   private TableRow row;
   private TableColumn startColumn;
   private double preferredWidth;

   public AbstractTableCell(TableRow row, TableColumn startColumn, int xSpan) {
      super((TableRegion)row);
      this.row = row;
      this.startColumn = startColumn;
      this.xSpan = xSpan;
      row.addCell(this);
   }

   public String getEffectiveAlign() {
      return this.getAlign() != null?this.getAlign():(this.startColumn.getAlign() != null?this.startColumn.getAlign():super.getEffectiveAlign());
   }

   public double getWidth() {
      return this.xSpan == 1?this.startColumn.getWidth():this.startColumn.getTable().getSpanWidth(this.startColumn, this.xSpan);
   }

   public int getXSpan() {
      return this.xSpan;
   }

   public void setXSpan(int xSpan) {
      this.xSpan = xSpan;
   }

   public TableRow getRow() {
      return this.row;
   }

   public void setRow(TableRow row) {
      this.row = row;
   }

   public TableColumn getStartColumn() {
      return this.startColumn;
   }

   public void setStartColumn(TableColumn startColumn) {
      this.startColumn = startColumn;
   }

   public double getPreferredWidth() {
      return this.preferredWidth;
   }

   public void setPreferredWidth(double preferredWidth) {
      this.preferredWidth = preferredWidth;
   }
}
