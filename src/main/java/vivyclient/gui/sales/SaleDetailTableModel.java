package vivyclient.gui.sales;

import java.math.BigDecimal;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import vivyclient.Client;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.BusinessException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.sales.PriceForProductContainer;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.ProductPrice;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.shared.Constants;
import vivyclient.util.DialogueUtil;
import vivyclient.util.ViewUtil;

public class SaleDetailTableModel extends AbstractTableModel {
   private Sale sale;
   private JTable tableView;
   private SaleDetailTableModel.Column[] columns;
   public static final int PRODUCT_COLUMN = 0;
   public static final int COMMENTS_COLUMN = 1;
   public static final int WARRANTY_COLUMN = 2;
   public static final int QUANTITY_COLUMN = 3;
   public static final int UNIT_PRICE_COLUMN = 4;
   public static final int LINE_TOTAL_COLUMN = 5;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Integer;

   public SaleDetailTableModel(Sale sale, JTable tableView) {
      this.sale = sale;
      this.tableView = tableView;
      this.columns = new SaleDetailTableModel.Column[6];
      this.columns[0] = new SaleDetailTableModel.Column("Product", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.columns[1] = new SaleDetailTableModel.Column("Comments", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.columns[2] = new SaleDetailTableModel.Column("Warranty", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.columns[3] = new SaleDetailTableModel.Column("Quantity", class$java$lang$Integer == null?(class$java$lang$Integer = class$("java.lang.Integer")):class$java$lang$Integer, true);
      this.columns[4] = new SaleDetailTableModel.Column("Unit Price", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, true);
      this.columns[5] = new SaleDetailTableModel.Column("Line Total", class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String, false);
      this.fireTableStructureChanged();
   }

   public int getColumnCount() {
      return this.columns.length;
   }

   public String getColumnName(int columnIndex) {
      return this.columns[columnIndex].getName();
   }

   public int getRowCount() {
      try {
         return this.sale.getSaleLineCount() + 1;
      } catch (Exception var2) {
         DialogueUtil.handleException(var2, "Error displaying Sale lines", "ERROR", true, Client.getMainFrame());
         return 0;
      }
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      try {
         if(rowIndex == this.sale.getSaleLineCount()) {
            return "";
         } else if(columnIndex == 0) {
            return this.sale.getSaleLine(rowIndex).getProduct();
         } else if(columnIndex == 1) {
            return this.sale.getSaleLine(rowIndex).getComments();
         } else if(columnIndex == 2) {
            String e = "";
            if(this.sale.getSaleLine(rowIndex).getWarrantyDuration() != null) {
               e = this.sale.getSaleLine(rowIndex).getWarrantyDurationMultiplier() + this.sale.getSaleLine(rowIndex).getWarrantyDuration().getShortDisplayLetter();
            }

            if(this.sale.getSaleLine(rowIndex).getWarrantyComments() != null && this.sale.getSaleLine(rowIndex).getWarrantyComments().trim().length() > 0) {
               e = e + " - " + this.sale.getSaleLine(rowIndex).getWarrantyComments();
            }

            return e;
         } else if(columnIndex == 3) {
            return ViewUtil.decimalDisplay(this.sale.getSaleLine(rowIndex).getQuantity());
         } else if(columnIndex == 4) {
            return new PriceForProductContainer(this.sale.getSaleLine(rowIndex).getUnitPrice(), this.sale.getSaleLine(rowIndex).getProduct());
         } else if(columnIndex == 5) {
            return ViewUtil.currencyDisplay(this.sale.getSaleLine(rowIndex).getLineTotal());
         } else {
            throw new AppRuntimeException();
         }
      } catch (Exception var4) {
         DialogueUtil.handleException(var4, "Error displaying Sale lines", "ERROR", true, Client.getMainFrame());
         return "ERROR!!!";
      }
   }

   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return this.columns[columnIndex].getIsEditable();
   }

   public void setValueAt(Object obj, int rowIndex, int columnIndex) {
      try {
         SaleDetail e;
         if(rowIndex == this.sale.getSaleLineCount()) {
            e = new SaleDetail();
            e.setQuantity(new BigDecimal("1"));
            e.setUnitPrice(Constants.ZERO_BIG_DECIMAL);
            this.sale.addSaleLine(e);
            this.fireTableRowsInserted(rowIndex + 1, rowIndex + 1);
         }

         if(columnIndex == 0) {
            if(obj != null) {
               if(!(obj instanceof Product)) {
                  throw new AppRuntimeException("Invalid Type: \"" + obj.toString() + "\"");
               }

               e = this.sale.getSaleLine(rowIndex);
               Product durationPart1 = (Product)obj;
               e.setProduct(durationPart1);
               e.setWarrantyDuration(durationPart1.getWarrantyDurationType());
               e.setWarrantyDurationMultiplier(durationPart1.getWarrantyDurationMultiplier());
               e.setWarrantyComments(durationPart1.getWarrantyComments());
               if(durationPart1.getProductPriceCount() > 0) {
                  e.setUnitPrice(durationPart1.getProductPrice(0).getUnitPrice());
               }
            }
         } else if(columnIndex == 1) {
            if(((String)obj).trim().length() == 0) {
               this.sale.getSaleLine(rowIndex).setComments((String)null);
            } else {
               this.sale.getSaleLine(rowIndex).setComments(((String)obj).trim());
            }
         } else if(columnIndex != 2) {
            if(columnIndex == 3) {
               try {
                  BigDecimal e2 = ViewUtil.parseDecimalAmount((String)obj);
                  if(e2.equals(Constants.ZERO_BIG_DECIMAL)) {
                     this.sale.removeSaleLineAt(rowIndex);
                     this.fireTableRowsDeleted(rowIndex, rowIndex);
                     this.tableView.repaint();
                     return;
                  }

                  this.sale.getSaleLine(rowIndex).setQuantity(e2);
               } catch (NumberFormatException var11) {
                  throw new UserInputException("Decimal expected", (JComponent)null);
               }
            } else if(columnIndex == 4) {
               if(obj instanceof ProductPrice) {
                  this.sale.getSaleLine(rowIndex).setUnitPrice(((ProductPrice)obj).getUnitPrice());
               } else if(obj instanceof BigDecimal) {
                  this.sale.getSaleLine(rowIndex).setUnitPrice((BigDecimal)obj);
               } else {
                  try {
                     this.sale.getSaleLine(rowIndex).setUnitPrice(ViewUtil.parseCurrencyAmount((String)obj));
                  } catch (NumberFormatException var10) {
                     throw new UserInputException("Decimal expected", (JComponent)null);
                  }
               }
            }
         } else {
            try {
               String e1 = ((String)obj).trim();
               if(e1.length() == 0) {
                  this.sale.getSaleLine(rowIndex).setWarrantyDuration((DurationType)null);
                  this.sale.getSaleLine(rowIndex).setWarrantyDurationMultiplier(-1);
                  this.sale.getSaleLine(rowIndex).setWarrantyComments((String)null);
               } else {
                  String durationPart = null;
                  String commentPart = null;
                  int dividerLocation = e1.indexOf("-");
                  if(dividerLocation >= 0) {
                     durationPart = e1.substring(0, dividerLocation).trim();
                     commentPart = e1.substring(dividerLocation + 1, e1.length()).trim();
                  } else {
                     durationPart = e1;
                  }

                  if(durationPart != null && durationPart.length() > 0) {
                     DurationType type = DurationType.getDurationTypeForCode(durationPart.substring(durationPart.length() - 1, durationPart.length()));
                     int multiplier = Integer.parseInt(durationPart.substring(0, durationPart.length() - 1));
                     if(multiplier < 0) {
                        throw new BusinessException("Warranty duration cannot be negative");
                     }

                     this.sale.getSaleLine(rowIndex).setWarrantyDuration(type);
                     this.sale.getSaleLine(rowIndex).setWarrantyDurationMultiplier(multiplier);
                  } else {
                     this.sale.getSaleLine(rowIndex).setWarrantyDuration((DurationType)null);
                     this.sale.getSaleLine(rowIndex).setWarrantyDurationMultiplier(-1);
                  }

                  if(commentPart != null && commentPart.length() > 0) {
                     this.sale.getSaleLine(rowIndex).setWarrantyComments(commentPart);
                  } else {
                     this.sale.getSaleLine(rowIndex).setWarrantyComments((String)null);
                  }
               }
            } catch (Exception var12) {
               var12.printStackTrace();
               throw new UserInputException("Enter Warranty in format [999{\'D\'|\'M\'|\'Y\'|\'L\'}][-Comment] eg. 2Y-On Site", (JComponent)null);
            }
         }

         this.fireTableCellUpdated(rowIndex, columnIndex);
      } catch (UserInputException var13) {
         DialogueUtil.handleUserInputException(var13, "Invalid input: ", "Error", Client.getMainFrame());
      } catch (Exception var14) {
         DialogueUtil.handleException(var14, "Error updating Sale line", "ERROR", true, Client.getMainFrame());
      }

   }

   public Sale getSale() {
      return this.sale;
   }

   public void setSale(Sale sale) {
      this.sale = sale;
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
