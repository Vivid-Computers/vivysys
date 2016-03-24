package vivyclient.gui;

import vivyclient.gui.DisplayPane;

public interface ModifyPanel extends DisplayPane {
   void save() throws Exception;

   boolean exit() throws Exception;
}
