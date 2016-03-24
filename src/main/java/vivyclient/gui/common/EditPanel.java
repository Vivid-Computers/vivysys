package vivyclient.gui.common;

import vivyclient.model.BaseModel;

public interface EditPanel {
   void refresh() throws Exception;

   boolean save() throws Exception;

   boolean exit() throws Exception;

   BaseModel getModel();
}
