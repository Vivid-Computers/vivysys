package vivyclient.gui.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.UserInputException;
import vivyclient.model.BaseModel;
import vivyclient.shared.Constants;
import vivyclient.util.ViewUtil;

public class PanelUtil {
   public static final int ALL = 0;
   public static final int POSITIVE = 1;
   public static final int GREATER_THAN_ZERO = 2;

   public static void populateComboList(JComboBox select, BaseModel type, boolean allowNull) throws Exception {
      List options = BaseModel.cachedFindAll(type);
      Object[] items;
      if(allowNull) {
         ArrayList allOptions = new ArrayList();
         allOptions.add((Object)null);
         allOptions.addAll(options);
         items = allOptions.toArray();
      } else {
         items = options.toArray();
      }

      select.setModel(new DefaultComboBoxModel(items));
   }

   public static void setComboSelection(JComboBox select, BaseModel selected) {
      if(selected != null) {
         ComboBoxModel model = select.getModel();

         for(int i = 0; i < model.getSize(); ++i) {
            if(model.getElementAt(i) == null) {
               if(selected == null) {
                  select.setSelectedIndex(i);
                  return;
               }
            } else if(model.getElementAt(i).equals(selected)) {
               select.setSelectedIndex(i);
               return;
            }
         }
      }

   }

   public static void validatedObjectIdRead(BaseModel model, JTextField component) throws Exception {
      if(component.getText().trim().length() == 0) {
         model.setObjectId(-1);
      } else {
         try {
            model.setObjectId(Integer.parseInt(component.getText()));
         } catch (Exception var3) {
            throw new UserInputException("Invalid " + model.getTableMap().getTableName() + " Id format: integer required", component);
         }

         if(model.getObjectId() <= 0) {
            throw new UserInputException("Invalid " + model.getTableMap().getTableName() + " Id: positive value expected", component);
         }

         if(model.exists((TransactionContainer)null)) {
            throw new UserInputException("Invalid " + model.getTableMap().getTableName() + " Id: this Id is already in use", component);
         }
      }

   }

   public static Calendar getValidatedCalendarRead(JTextField component, String fieldName, boolean mandatory) throws Exception {
      if(component.getText().trim().length() == 0) {
         if(mandatory) {
            throw new UserInputException("Enter " + fieldName + " value", component);
         } else {
            return null;
         }
      } else {
         try {
            return ViewUtil.parseDate(component.getText());
         } catch (Exception var4) {
            throw new UserInputException("Invalid " + fieldName + " Format: Date " + "dd-MM-yyyy" + " expected", component);
         }
      }
   }

   public static Object getValidatedComboItem(JComboBox component, String fieldName, boolean mandatory) throws Exception {
      if(component.getSelectedItem() == null) {
         if(mandatory) {
            throw new UserInputException("Enter " + fieldName + " value", component);
         } else {
            return null;
         }
      } else {
         return component.getSelectedItem();
      }
   }

   public static String getValidatedStringValue(JTextField component, String fieldName, boolean mandatory) throws Exception {
      if(component.getText().trim().length() == 0) {
         if(mandatory) {
            throw new UserInputException("Enter " + fieldName + " value", component);
         } else {
            return "";
         }
      } else {
         return component.getText();
      }
   }

   public static int getValidatedIntValue(JTextField component, String fieldName, boolean mandatory, int validation) throws Exception {
      if(component.getText().trim().length() == 0) {
         if(mandatory) {
            throw new UserInputException("Enter " + fieldName + " value", component);
         } else {
            return -1;
         }
      } else {
         int input;
         try {
            input = Integer.parseInt(component.getText());
         } catch (Exception var6) {
            throw new UserInputException("Invalid " + fieldName + " Format: Integer expected", component);
         }

         if(validation == 1) {
            if(input < 0) {
               throw new UserInputException("Invalid " + fieldName + ": Positive Integer expected", component);
            }
         } else if(validation == 2 && input <= 0) {
            throw new UserInputException("Invalid " + fieldName + ": Above zero Integer expected", component);
         }

         return input;
      }
   }

   public static BigDecimal getValidatedCurrencyValue(JTextField component, String fieldName, boolean mandatory, int validation) throws Exception {
      if(component.getText().trim().length() == 0) {
         if(mandatory) {
            throw new UserInputException("Enter " + fieldName + " value", component);
         } else {
            return null;
         }
      } else {
         BigDecimal input;
         try {
            input = ViewUtil.parseCurrencyAmount(component.getText());
         } catch (Exception var6) {
            throw new UserInputException("Invalid " + fieldName + " Format: Decimal expected", component);
         }

         if(validation == 1) {
            if(input.compareTo(Constants.ZERO_BIG_DECIMAL) < 0) {
               throw new UserInputException("Invalid " + fieldName + ": Positive Decimal expected", component);
            }
         } else if(validation == 2 && input.compareTo(Constants.ZERO_BIG_DECIMAL) <= 0) {
            throw new UserInputException("Invalid " + fieldName + ": Above zero Decimal expected", component);
         }

         return input;
      }
   }
}
