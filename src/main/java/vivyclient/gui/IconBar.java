package vivyclient.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import vivyclient.gui.products.ProductFrame;

public class IconBar extends JPanel {
   private ActionListener bNewListener;
   private GridBagConstraints gridBagConstraints;
   private ProductFrame productFrame;
   private JButton bSave;
   private JPanel buttonHolder;
   private JPanel spacerPanel;
   private JButton bCopy;
   private JButton bDelete;
   private JButton bNew;
   private JButton bPaste;
   private JButton bCut;

   public IconBar(ProductFrame productFrame) {
      this.productFrame = productFrame;
      this.initComponents();
   }

   public void resetBar() {
      this.productFrame.validate();
      this.buttonHolder.removeAll();
      this.validate();
      this.repaint();
   }

   public void addSaveButton(ActionListener actionListener, String toolTipText) {
      this.bSave = new JButton();
      this.bSave.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      this.bSave.setMargin(new Insets(2, 2, 2, 2));
      this.gridBagConstraints = new GridBagConstraints();
      this.gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.gridBagConstraints.anchor = 17;
      this.bSave.setToolTipText(toolTipText);
      this.bSave.addActionListener(actionListener);
      this.buttonHolder.add(this.bSave, this.gridBagConstraints);
      this.productFrame.validate();
      this.validate();
      this.repaint();
   }

   public void addNewButton(ActionListener actionListener, String toolTipText) {
      this.bNew = new JButton();
      this.bNew.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      this.bNew.setMargin(new Insets(2, 2, 2, 2));
      this.gridBagConstraints = new GridBagConstraints();
      this.gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.gridBagConstraints.anchor = 17;
      this.bNew.setToolTipText(toolTipText);
      this.bNew.addActionListener(actionListener);
      this.buttonHolder.add(this.bNew, this.gridBagConstraints);
      this.productFrame.validate();
      this.validate();
      this.repaint();
   }

   private void initComponents() {
      this.buttonHolder = new JPanel();
      this.bNew = new JButton();
      this.bSave = new JButton();
      this.bCopy = new JButton();
      this.bCut = new JButton();
      this.bPaste = new JButton();
      this.bDelete = new JButton();
      this.spacerPanel = new JPanel();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(4, 4, 4, 4)));
      this.buttonHolder.setLayout(new GridBagLayout());
      this.bNew.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      this.bNew.setHorizontalAlignment(2);
      this.bNew.setMargin(new Insets(2, 2, 2, 2));
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bNew, gridBagConstraints);
      this.bSave.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      this.bSave.setMargin(new Insets(2, 2, 2, 2));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bSave, gridBagConstraints);
      this.bCopy.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/copy16.gif")));
      this.bCopy.setMargin(new Insets(2, 2, 2, 2));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bCopy, gridBagConstraints);
      this.bCut.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Cut16.gif")));
      this.bCut.setMargin(new Insets(2, 2, 2, 2));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bCut, gridBagConstraints);
      this.bPaste.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Paste16.gif")));
      this.bPaste.setMargin(new Insets(2, 2, 2, 2));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bPaste, gridBagConstraints);
      this.bDelete.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Delete16.gif")));
      this.bDelete.setMargin(new Insets(2, 2, 2, 2));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.anchor = 17;
      this.buttonHolder.add(this.bDelete, gridBagConstraints);
      this.add(this.buttonHolder, new GridBagConstraints());
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.spacerPanel, gridBagConstraints);
   }
}
