package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import vivyclient.print.table.TableColumn;

public interface TableCell {
   void initialise(Graphics2D var1);

   Rectangle2D print(Graphics2D var1, double var2, boolean var4);

   TableColumn getStartColumn();

   int getXSpan();

   double getPreferredWidth();
}
