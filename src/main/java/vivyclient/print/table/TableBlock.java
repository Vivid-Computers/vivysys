package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;
import vivyclient.print.table.TableRow;

public class TableBlock extends TableRegion {
   private PrintableTable table;
   private List tableRows;

   public TableBlock(PrintableTable table) {
      super(table);
      this.table = table;
      table.addBlock(this);
      this.tableRows = new ArrayList();
   }

   public void addRow(TableRow row) {
      this.tableRows.add(row);
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double width = 0.0D;
      double currentY = startY;

      for(int i = 0; i < this.tableRows.size(); ++i) {
         Rectangle2D rowSize = ((TableRow)this.tableRows.get(i)).print(g, currentY, hidden);
         if(rowSize.getWidth() > width) {
            width = rowSize.getWidth();
         }

         currentY += rowSize.getHeight();
      }

      return new Double(0.0D, 0.0D, width, currentY - startY);
   }

   public List getCellsForColumn(TableColumn column) {
      ArrayList columnCells = new ArrayList();

      for(int i = 0; i < this.tableRows.size(); ++i) {
         columnCells.addAll(((TableRow)this.tableRows.get(i)).getCellsForColumn(column));
      }

      return columnCells;
   }

   public List getAllMultiColumnCells() {
      ArrayList cells = new ArrayList();

      for(int i = 0; i < this.tableRows.size(); ++i) {
         cells.addAll(((TableRow)this.tableRows.get(i)).getAllMultiColumnCells());
      }

      return cells;
   }

   public PrintableTable getTable() {
      return this.table;
   }

   public void setTable(PrintableTable table) {
      this.table = table;
   }

   public void initialise(Graphics2D g) {
   }

   public List getTableRows() {
      return this.tableRows;
   }

   public void setTableRows(List tableRows) {
      this.tableRows = tableRows;
   }
}
