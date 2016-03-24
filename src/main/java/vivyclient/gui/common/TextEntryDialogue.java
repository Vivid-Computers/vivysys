package vivyclient.gui.common;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import vivyclient.Client;
import vivyclient.util.Settings;

public class TextEntryDialogue extends JDialog {
   private static final String NAME = "TextEntryDialogue";
   private Object lock;
   private String enteredText;
   private boolean cancelled;
   private JSeparator jSeparator2;
   private JScrollPane jScrollPane1;
   private JButton bOkay;
   private JSeparator jSeparator1;
   private JPanel jPanel1;
   private JLabel lTitle;
   private JTextArea textArea;
   private JButton bCancel;

   public TextEntryDialogue(Frame parent, boolean modal, String text, String title, Object lock) {
      super(parent, modal);
      this.lock = lock;
      this.initComponents();
      this.textArea.setText(text);
      this.lTitle.setText(title);
      this.setTitle("Enter Text");
      this.enteredText = null;
      this.cancelled = false;
      this.setSize(Settings.getWidth("TextEntryDialogue"), Settings.getHeight("TextEntryDialogue"));
      this.setLocation(Settings.getXPos("TextEntryDialogue"), Settings.getYPos("TextEntryDialogue"));
   }

   public static String getStringValue(String initialText, String title) {
      Object lock = new Object();
      TextEntryDialogue dialogue = new TextEntryDialogue(Client.getMainFrame(), true, initialText, title, lock);
      dialogue.show();

      while(dialogue.getEnteredText() == null && !dialogue.isCancelled()) {
         try {
            synchronized(lock) {
               lock.wait();
            }
         } catch (InterruptedException var7) {
            ;
         }
      }

      return dialogue.getEnteredText();
   }

   private void notifiedClose() {
      Object var1 = this.lock;
      synchronized(this.lock) {
         Settings.setWidth("TextEntryDialogue", (int)this.getSize().getWidth());
         Settings.setHeight("TextEntryDialogue", (int)this.getSize().getHeight());
         Settings.setXPos("TextEntryDialogue", this.getLocation().x);
         Settings.setYPos("TextEntryDialogue", this.getLocation().y);
         this.setVisible(false);
         this.dispose();
         this.lock.notifyAll();
      }
   }

   private String getEnteredText() {
      return this.enteredText;
   }

   private boolean isCancelled() {
      return this.cancelled;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.jScrollPane1 = new JScrollPane();
      this.textArea = new JTextArea();
      this.jSeparator2 = new JSeparator();
      this.bOkay = new JButton();
      this.bCancel = new JButton();
      this.jPanel1 = new JPanel();
      this.getContentPane().setLayout(new GridBagLayout());
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            TextEntryDialogue.this.closeDialog(evt);
         }
      });
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Title");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.getContentPane().add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.textArea.setLineWrap(true);
      this.textArea.setWrapStyleWord(true);
      this.jScrollPane1.setViewportView(this.textArea);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.getContentPane().add(this.jScrollPane1, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.getContentPane().add(this.jSeparator2, gridBagConstraints);
      this.bOkay.setText("Okay");
      this.bOkay.setMargin(new Insets(2, 5, 2, 5));
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            TextEntryDialogue.this.bOkayMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(5, 0, 10, 0);
      this.getContentPane().add(this.bOkay, gridBagConstraints);
      this.bCancel.setText("Cancel");
      this.bCancel.setMargin(new Insets(2, 5, 2, 5));
      this.bCancel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            TextEntryDialogue.this.bCancelMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(5, 5, 10, 5);
      this.getContentPane().add(this.bCancel, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.jPanel1, gridBagConstraints);
      this.pack();
   }

   private void bCancelMouseClicked(MouseEvent evt) {
      this.closeDialog((WindowEvent)null);
   }

   private void bOkayMouseClicked(MouseEvent evt) {
      this.enteredText = this.textArea.getText();
      this.notifiedClose();
   }

   private void closeDialog(WindowEvent evt) {
      this.cancelled = true;
      this.notifiedClose();
   }
}
