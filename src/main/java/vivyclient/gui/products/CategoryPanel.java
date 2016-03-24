package vivyclient.gui.products;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.UserInputException;
import vivyclient.gui.ModifyPanel;
import vivyclient.gui.ModifyPanelListener;
import vivyclient.model.Category;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class CategoryPanel extends JPanel implements ModifyPanel {
   Category category;
   private ModifyPanelListener listener;
   boolean isNew;
   private JLabel lDisplayOrder;
   private JSeparator jSeparator1;
   private JLabel lNotes;
   private JTextField tId;
   private JTextField tNotes;
   private JLabel lId;
   private JTextField tName;
   private JLabel lName;
   private JTextField tLastUpdate;
   private JLabel lTitle;
   private JLabel lLastUpdate;
   private JTextField tDisplayOrder;

   public CategoryPanel(Category category, ModifyPanelListener listener) throws Exception {
      this.category = category;
      this.listener = listener;
      this.initComponents();
      this.setValues();
   }

   private void setValues() throws Exception {
      this.isNew = !this.category.exists((TransactionContainer)null);
      if(this.isNew) {
         this.lTitle.setText("New Category");
         this.tId.setText("");
         this.tId.setEnabled(true);
         this.tDisplayOrder.setText("");
         this.tName.setText("");
      } else {
         this.lTitle.setText("Editing " + this.category.toString());
         this.tId.setText(Integer.toString(this.category.getObjectId()));
         this.tId.setEnabled(false);
         this.tDisplayOrder.setText(Integer.toString(this.category.getDisplayOrder()));
         this.tName.setText(this.category.getName());
      }

      this.tLastUpdate.setText(ViewUtil.calendarDisplay(this.category.getLastUpdate()));
      this.tNotes.setText(this.category.getNotes());
   }

   public boolean exit() throws Exception {
      return true;
   }

   public Component getOuterFrame() {
      return Client.getMainFrame();
   }

   public void save() throws Exception {
      String id = this.tId.getText().trim();
      if(this.isNew) {
         if(id.length() == 0) {
            this.category.setObjectId(Settings.getNullInt());
         } else {
            try {
               this.category.setObjectId(Integer.parseInt(id));
            } catch (Exception var4) {
               throw new UserInputException("Invalid Category Id format: integer required", this.tId);
            }

            if(this.category.exists((TransactionContainer)null)) {
               throw new UserInputException("Invalid Category Id: this Id is already in use", this.tId);
            }
         }
      }

      this.category.setName(this.tName.getText().trim());

      try {
         this.category.setDisplayOrder(Integer.parseInt(this.tDisplayOrder.getText()));
      } catch (Exception var3) {
         throw new UserInputException("Invalid Display Order format: integer required", this.tDisplayOrder);
      }

      this.category.setNotes(this.tNotes.getText().trim());
      this.category.save((TransactionContainer)null);
      this.category.setLastUpdate(Calendar.getInstance());
      this.listener.saveCompleted(this.category, this.isNew);
      if(this.isNew) {
         this.setValues();
      }

   }

   private void initComponents() {
      this.lName = new JLabel();
      this.tName = new JTextField();
      this.lId = new JLabel();
      this.tId = new JTextField();
      this.lDisplayOrder = new JLabel();
      this.tDisplayOrder = new JTextField();
      this.lLastUpdate = new JLabel();
      this.tLastUpdate = new JTextField();
      this.lNotes = new JLabel();
      this.tNotes = new JTextField();
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
      this.lName.setHorizontalAlignment(11);
      this.lName.setText("Name:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lName, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tName, gridBagConstraints);
      this.lId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lId, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.tId, gridBagConstraints);
      this.lDisplayOrder.setText("Display Order:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lDisplayOrder, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tDisplayOrder, gridBagConstraints);
      this.lLastUpdate.setText("Last Update:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lLastUpdate, gridBagConstraints);
      this.tLastUpdate.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tLastUpdate, gridBagConstraints);
      this.lNotes.setText("Notes:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(5, 0, 10, 5);
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.lNotes, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 10, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.tNotes, gridBagConstraints);
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("SubCategory");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(0, 15, 3, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipady = 3;
      gridBagConstraints.insets = new Insets(0, 0, 10, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.jSeparator1, gridBagConstraints);
   }
}
