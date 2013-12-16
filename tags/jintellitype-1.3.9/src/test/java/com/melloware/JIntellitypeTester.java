/*
 * JIntellitype ----------------- Copyright 2005-2006 Emil A. Lefkof III
 * 
 * I always give it my best shot to make a program useful and solid, but remeber
 * that there is absolutely no warranty for using this program as stated in the
 * following terms:
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.melloware;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * Swing based test application to test all the functions of the JIntellitype library.
 * <p>
 * Copyright (c) 2006 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <elefkof@ksmpartners.com>
 * @version 1.0
 */
public class JIntellitypeTester extends JFrame implements HotkeyListener, IntellitypeListener {

   private static JIntellitypeTester mainFrame;
   private static final int WINDOWS_A = 88;
   private static final int ALT_SHIFT_B = 89;
   private static final int CTRL_SHIFT_C = 90;
   private static final int PRINT_SCREEN = 91;
   private static final int F11 = 92;
   private static final int F12 = 93;
   private static final int SEMICOLON = 94;
   private static final int TICK = 95;
   private final JButton btnRegisterHotKey = new JButton();
   private final JButton btnUnregisterHotKey = new JButton();
   private final JPanel bottomPanel = new JPanel();
   private final JPanel mainPanel = new JPanel();
   private final JPanel topPanel = new JPanel();
   private final JScrollPane scrollPane = new JScrollPane();
   private final JTextArea textArea = new JTextArea();

   /**
    * Creates new form.
    */
   public JIntellitypeTester() {
      initComponents();
   }

   /**
    * Main method to launch this application.
    * <p>
    * @param args any command line arguments
    */
   public static void main(String[] args) {
      System.out.println(new File(".").getAbsolutePath());
      // first check to see if an instance of this application is already
      // running, use the name of the window title of this JFrame for checking
      if (JIntellitype.checkInstanceAlreadyRunning("JIntellitype Test Application")) {
         System.exit(1);
      }

      // next check to make sure JIntellitype DLL can be found and we are on
      // a Windows operating System
      if (!JIntellitype.isJIntellitypeSupported()) {
         System.exit(1);
      }

      mainFrame = new JIntellitypeTester();
      mainFrame.setTitle("JIntellitype Test Application");
      center(mainFrame);
      mainFrame.setVisible(true);
      mainFrame.initJIntellitype();
   }

   /*
    * (non-Javadoc)
    * @see com.melloware.jintellitype.HotkeyListener#onHotKey(int)
    */
   public void onHotKey(int aIdentifier) {
      output("WM_HOTKEY message received " + Integer.toString(aIdentifier));
   }

   /*
    * (non-Javadoc)
    * @see com.melloware.jintellitype.IntellitypeListener#onIntellitype(int)
    */
   public void onIntellitype(int aCommand) {

      switch (aCommand) {
      case JIntellitype.APPCOMMAND_BROWSER_BACKWARD:
         output("BROWSER_BACKWARD message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_FAVOURITES:
         output("BROWSER_FAVOURITES message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_FORWARD:
         output("BROWSER_FORWARD message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_HOME:
         output("BROWSER_HOME message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_REFRESH:
         output("BROWSER_REFRESH message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_SEARCH:
         output("BROWSER_SEARCH message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_BROWSER_STOP:
         output("BROWSER_STOP message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_LAUNCH_APP1:
         output("LAUNCH_APP1 message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_LAUNCH_APP2:
         output("LAUNCH_APP2 message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_LAUNCH_MAIL:
         output("LAUNCH_MAIL message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
         output("MEDIA_NEXTTRACK message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
         output("MEDIA_PLAY_PAUSE message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
         output("MEDIA_PREVIOUSTRACK message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_MEDIA_STOP:
         output("MEDIA_STOP message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_VOLUME_DOWN:
         output("VOLUME_DOWN message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_VOLUME_UP:
         output("VOLUME_UP message received " + Integer.toString(aCommand));
         break;
      case JIntellitype.APPCOMMAND_VOLUME_MUTE:
         output("VOLUME_MUTE message received " + Integer.toString(aCommand));
         break;
      default:
         output("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
         break;
      }
   }

   /**
    * Centers window on desktop.
    * <p>
    * @param aFrame the Frame to center
    */
   private static void center(JFrame aFrame) {
      final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      final Point centerPoint = ge.getCenterPoint();
      final Rectangle bounds = ge.getMaximumWindowBounds();
      final int w = Math.min(aFrame.getWidth(), bounds.width);
      final int h = Math.min(aFrame.getHeight(), bounds.height);
      final int x = centerPoint.x - (w / 2);
      final int y = centerPoint.y - (h / 2);
      aFrame.setBounds(x, y, w, h);
      if ((w == bounds.width) && (h == bounds.height)) {
         aFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
      }
      aFrame.validate();
   }

   /**
    * Method to register a hotkey using the RegisterHotKey Windows API call.
    * <p>
    * @param aEvent the ActionEvent fired.
    */
   private void btnRegisterHotKey_actionPerformed(ActionEvent aEvent) {
      // assign the WINDOWS+A key to the unique id 88 for identification
      JIntellitype.getInstance().registerHotKey(WINDOWS_A, JIntellitype.MOD_WIN, 'A');
      JIntellitype.getInstance().registerHotKey(ALT_SHIFT_B, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, 'B');
      JIntellitype.getInstance().registerSwingHotKey(CTRL_SHIFT_C, Event.CTRL_MASK + Event.SHIFT_MASK, 'C');

      // use a 0 for the modifier if you just want a single keystroke to be a
      // hotkey
      JIntellitype.getInstance().registerHotKey(PRINT_SCREEN, 0, 44);
      JIntellitype.getInstance().registerHotKey(F11, "F11");
      JIntellitype.getInstance().registerHotKey(F12, JIntellitype.MOD_ALT, 123);
      JIntellitype.getInstance().registerHotKey(SEMICOLON, 0, 186);
      JIntellitype.getInstance().registerHotKey(TICK, 0, 192);
      // clear the text area
      textArea.setText("");
      output("RegisterHotKey WINDOWS+A was assigned uniqueID 88");
      output("RegisterHotKey ALT+SHIFT+B was assigned uniqueID 89");
      output("RegisterHotKey CTRL+SHIFT+C was assigned uniqueID 90");
      output("RegisterHotKey PRINT_SCREEN was assigned uniqueID 91");
      output("RegisterHotKey F9 was assigned uniqueID 92");
      output("RegisterHotKey F12 was assigned uniqueID 93");
      output("RegisterHotKey SEMICOLON was assigned uniqueID 94");
      output("Press WINDOWS+A or ALT+SHIFT+B or CTRL+SHIFT+C in another application and you will see the debug output in the textarea.");
   }

   /**
    * Method to unregister a hotkey using the UnregisterHotKey Windows API call.
    * <p>
    * @param aEvent the ActionEvent fired.
    */
   private void btnUnregisterHotKey_actionPerformed(ActionEvent aEvent) {
      JIntellitype.getInstance().unregisterHotKey(WINDOWS_A);
      JIntellitype.getInstance().unregisterHotKey(ALT_SHIFT_B);
      JIntellitype.getInstance().unregisterHotKey(CTRL_SHIFT_C);
      JIntellitype.getInstance().unregisterHotKey(PRINT_SCREEN);
      JIntellitype.getInstance().unregisterHotKey(F11);
      JIntellitype.getInstance().unregisterHotKey(F12);
      JIntellitype.getInstance().unregisterHotKey(SEMICOLON);
      output("UnregisterHotKey WINDOWS+A");
      output("UnregisterHotKey ALT+SHIFT+B");
      output("UnregisterHotKey CTRL+SHIFT+C");
      output("UnregisterHotKey PRINT_SCREEN");
      output("UnregisterHotKey F9");
      output("UnregisterHotKey F12");
      output("UnregisterHotKey SEMICOLON");
      output("Press WINDOWS+A or ALT+SHIFT+B in another application and you will NOT see the debug output in the textarea.");
   }

   /**
    * This method is called from within the constructor to initialize the form.
    */
   private void initComponents() {
      mainPanel.setLayout(new BorderLayout());
      topPanel.setBorder(new EtchedBorder(1));
      bottomPanel.setLayout(new BorderLayout());
      bottomPanel.setBorder(new EtchedBorder(1));
      btnRegisterHotKey.setText("RegisterHotKey");
      btnRegisterHotKey.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            btnRegisterHotKey_actionPerformed(e);
         }
      });
      btnUnregisterHotKey.setText("UnregisterHotKey");
      btnUnregisterHotKey.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            btnUnregisterHotKey_actionPerformed(e);
         }
      });
      topPanel.add(btnRegisterHotKey);
      topPanel.add(btnUnregisterHotKey);
      scrollPane.getViewport().add(textArea);
      bottomPanel.add(scrollPane, BorderLayout.CENTER);
      mainPanel.add(topPanel, BorderLayout.NORTH);
      mainPanel.add(bottomPanel, BorderLayout.CENTER);

      this.addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
         public void windowClosing(java.awt.event.WindowEvent evt) {
            // don't forget to clean up any resources before close
            JIntellitype.getInstance().cleanUp();
            System.exit(0);
         }
      });

      this.getContentPane().add(mainPanel);
      this.pack();
      this.setSize(800, 600);
   }

   /**
    * Initialize the JInitellitype library making sure the DLL is located.
    */
   public void initJIntellitype() {
      try {

         // initialize JIntellitype with the frame so all windows commands can
         // be attached to this window
         JIntellitype.getInstance().addHotKeyListener(this);
         JIntellitype.getInstance().addIntellitypeListener(this);
         output("JIntellitype initialized");
      } catch (RuntimeException ex) {
         output("Either you are not on Windows, or there is a problem with the JIntellitype library!");
      }
   }

   /**
    * Send the output to the log and the text area.
    * <p>
    * @param text the text to output
    */
   private void output(String text) {
      textArea.append(text);
      textArea.append("\n");
   }

}