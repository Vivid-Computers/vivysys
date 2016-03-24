package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;

public interface TableRow {
   void initialise(Graphics2D var1);

   Rectangle2D print(Graphics2D var1, double var2, boolean var4);

   List getCellsForColumn(TableColumn var1);

   List getAllMultiColumnCells();

   void addCell(TableCell var1);
}
