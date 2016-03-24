package vivyclient.gui.sales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.UserInputException;
import vivyclient.model.Dispatch;
import vivyclient.model.DispatchDetail;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.shared.Constants;
import vivyclient.util.DialogueUtil;
import vivyclient.util.ViewUtil;

public class DispatchDetailTableModel extends AbstractTableModel {
   private JTable tableView;
   private DispatchDetailTableModel.Column[] columns;
   private Dispatch dispatch = null;
   private List dispatchItems;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Integer;

   public DispatchDetailTableModel(Dispatch dispatch, JTable tableView) throws Exception {
      this.dispatch = dispatch;
      this.tableView = tableView;
      this.columns = new DispatchDetailTableModel.Column[5];
      this.columns[0] = new DispatchDetailTableModel.Column("Product", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, false);
      this.columns[1] = new DispatchDetailTableModel.Column("Quantity", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, false);
      this.columns[2] = new DispatchDetailTableModel.Column("Dispatched", class$java$lang$Integer == null?(class$java$lang$Integer = class$("java.lang.Integer")):class$java$lang$Integer, false);
      this.columns[3] = new DispatchDetailTableModel.Column("This Dispatch", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.columns[4] = new DispatchDetailTableModel.Column("Serial Numbers", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.fireTableStructureChanged();
      this.refreshContent();
   }

   public void refreshContent() throws Exception {
      if(this.dispatch.getSale() == null) {
         this.dispatchItems = null;
      } else {
         Sale sale = this.dispatch.getSale();
         List saleLines = sale.getSaleDetailsForDispatch((TransactionContainer)null);
         this.dispatchItems = new ArrayList(saleLines.size());

         for(int i = 0; i < saleLines.size(); ++i) {
            SaleDetail line = (SaleDetail)saleLines.get(i);
            DispatchDetail dispatchDetail = null;

            for(int j = 0; j < this.dispatch.getDispatchContentCount(); ++j) {
               if(this.dispatch.getDispatchContent(j).getShippedSaleDetail().equals(line)) {
                  dispatchDetail = this.dispatch.getDispatchContent(j);
                  dispatchDetail.setShippedSaleDetail(line);
                  line.setDispatchedQuantity(line.getDispatchedQuantity().subtract(dispatchDetail.getQuantity()));
                  break;
               }
            }

            if(dispatchDetail == null) {
               dispatchDetail = new DispatchDetail();
               dispatchDetail.setDispatch(this.dispatch);
               dispatchDetail.setShippedSaleDetail(line);
               dispatchDetail.setQuantity(Constants.ZERO_BIG_DECIMAL);
               dispatchDetail.setSerialNumbers("");
            }

            this.dispatchItems.add(dispatchDetail);
         }
      }

      this.fireTableDataChanged();
   }

   public int getColumnCount() {
      return this.columns.length;
   }

   public String getColumnName(int columnIndex) {
      return this.columns[columnIndex].getName();
   }

   public int getRowCount() {
      return this.dispatchItems == null?0:this.dispatchItems.size();
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      try {
         if(columnIndex == 0) {
            return ((DispatchDetail)this.dispatchItems.get(rowIndex)).getShippedSaleDetail().getProduct();
         } else if(columnIndex == 1) {
            return ViewUtil.decimalDisplay(((DispatchDetail)this.dispatchItems.get(rowIndex)).getShippedSaleDetail().getQuantity());
         } else if(columnIndex == 2) {
            return ViewUtil.decimalDisplay(((DispatchDetail)this.dispatchItems.get(rowIndex)).getShippedSaleDetail().getDispatchedQuantity());
         } else if(columnIndex == 3) {
            return ViewUtil.decimalDisplay(((DispatchDetail)this.dispatchItems.get(rowIndex)).getQuantity());
         } else if(columnIndex == 4) {
            return ((DispatchDetail)this.dispatchItems.get(rowIndex)).getSerialNumbers();
         } else {
            throw new AppRuntimeException();
         }
      } catch (Exception var4) {
         DialogueUtil.handleException(var4, "Error displaying Dispatch lines", "ERROR", true, Client.getMainFrame());
         return "ERROR!!!";
      }
   }

   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return this.columns[columnIndex].getIsEditable();
   }

   public void setValueAt(Object obj, int rowIndex, int columnIndex) {
      try {
         DispatchDetail e = (DispatchDetail)this.dispatchItems.get(rowIndex);
         if(columnIndex == 3) {
            try {
               BigDecimal serialNumbers = ViewUtil.parseDecimalAmount((String)obj);
               if(serialNumbers.compareTo(Constants.ZERO_BIG_DECIMAL) < 0) {
                  throw new UserInputException("Dispatch quantity cannot be negative", (JComponent)null);
               }

               BigDecimal totalQuantity = serialNumbers.add(e.getShippedSaleDetail().getDispatchedQuantity());
               if(totalQuantity.compareTo(e.getShippedSaleDetail().getQuantity()) > 0) {
                  throw new UserInputException("Quantity dispatched cannot exceed that sold", (JComponent)null);
               }

               e.setQuantity(serialNumbers);
            } catch (NumberFormatException var7) {
               throw new UserInputException("Decimal expected", (JComponent)null);
            }
         } else if(columnIndex == 4) {
            String serialNumbers1 = (String)obj;
            e.setSerialNumbers(serialNumbers1);
         }

         this.fireTableCellUpdated(rowIndex, columnIndex);
      } catch (UserInputException var8) {
         DialogueUtil.handleUserInputException(var8, "Invalid input: ", "Error", Client.getMainFrame());
      } catch (Exception var9) {
         DialogueUtil.handleException(var9, "Error updating Dispatch line", "ERROR", true, Client.getMainFrame());
      }

   }

   public Dispatch getDispatch() {
      return this.dispatch;
   }

   public void setDispatch(Dispatch dispatch) {
      this.dispatch = dispatch;
   }

   public List getDispatchItems() {
      return this.dispatchItems;
   }

   public void setDispatchItems(List dispatchItems) {
      this.dispatchItems = dispatchItems;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   class Column {
      private String name;
      private Class type;
      private boolean isEditable;

      public Column(String name, Class type, boolean isEditable) {
         this.name = name;
         this.type = type;
         this.isEditable = isEditable;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public Class getType() {
         return this.type;
      }

      public void setType(Class type) {
         this.type = type;
      }

      public boolean getIsEditable() {
         return this.isEditable;
      }

      public void setIsEditable(boolean isEditable) {
         this.isEditable = isEditable;
      }
   }
}
