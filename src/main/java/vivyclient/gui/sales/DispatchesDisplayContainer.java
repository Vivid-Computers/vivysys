package vivyclient.gui.sales;

import java.awt.Component;
import javax.swing.JMenuBar;
import vivyclient.model.Dispatch;

public interface DispatchesDisplayContainer {
   String getName();

   JMenuBar getJMenuBar();

   void addToJMenuBar(Component var1);

   void resetJMenuBar();

   void dispatchSelectionChange(Dispatch var1);
}
