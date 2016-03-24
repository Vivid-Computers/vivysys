package vivyclient.gui.common;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JMenuBar;
import vivyclient.model.BaseModel;

public interface ModelDisplayContainer {
   String getName();

   JMenuBar getJMenuBar();

   void addToJMenuBar(Component var1);

   void resetJMenuBar();

   void modelSelectionChange(BaseModel var1);

   void setContainerCursor(Cursor var1);
}
