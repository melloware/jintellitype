/**
 * JIntellitype ----------------- Copyright 2005-2008 Emil A. Lefkof III,
 * Melloware Inc. I always give it my best shot to make a program useful and
 * solid, but remeber that there is absolutely no warranty for using this
 * program as stated in the following terms: Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.melloware.jintellitype;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

/**
 * JIntellitype A Java Implementation for using the Windows API Intellitype
 * commands and the RegisterHotKey and UnRegisterHotkey API calls for globally
 * responding to key events. Intellitype are commands that are using for Play,
 * Stop, Next on Media keyboards or some laptops that have those special keys.
 * <p>
 * JIntellitype class that is used to call Windows API calls using the
 * JIntellitype.dll.
 * <p>
 * This file comes with native code in JINTELLITYPE.DLL The DLL should go in
 * C:/WINDOWS/SYSTEM or in your current directory
 * <p>
 * <p>
 * Copyright (c) 1999-2008 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.3.1
 */
public final class JIntellitype implements JIntellitypeConstants {

   /**
    * Static variable to hold singleton.
    */
   private static JIntellitype jintellitype = null;

   /**
    * Static variable for double checked thread safety.
    */
   private static boolean isInitialized = false;

   /**
    * Static variable to hold the libary location if set
    */
   private static String libraryLocation = null;

   /**
    * Listeners collection for Hotkey events
    */
   private final List<HotkeyListener> hotkeyListeners = Collections
            .synchronizedList(new CopyOnWriteArrayList<HotkeyListener>());

   /**
    * Listeners collection for Hotkey events
    */
   private final List<IntellitypeListener> intellitypeListeners = Collections
            .synchronizedList(new CopyOnWriteArrayList<IntellitypeListener>());

   /**
    * Handler is used by JNI code to keep different JVM instances separate
    */
   @SuppressWarnings("unused")
   private final int handler = 0;

   /**
    * Map containing key->keycode mapping
    * @see #registerHotKey(int, String)
    * @see #getKey2KeycodeMapping()
    */
   private final HashMap<String, Integer> keycodeMap;

   /**
    * Private Constructor to prevent instantiation. Initialize the library for
    * calling.
    */
   private JIntellitype() {
      try {
         // Load JNI library
         System.loadLibrary("JIntellitype");
      } catch (Throwable ex) {
         try {
            if (getLibraryLocation() != null) {
               System.load(getLibraryLocation());
            } else {
               String jarPath = "com/melloware/jintellitype/";
               String tmpDir = System.getProperty("java.io.tmpdir");
               try {
                  String dll = "JIntellitype.dll";
                  fromJarToFs(jarPath + dll, tmpDir + dll);
                  System.load(tmpDir + dll);
               } catch (UnsatisfiedLinkError e) {
                  String dll = "JIntellitype64.dll";
                  fromJarToFs(jarPath + dll, tmpDir + dll);
                  System.load(tmpDir + dll);
               }
            }
         } catch (Throwable ex2) {
            throw new JIntellitypeException(
                     "Could not load JIntellitype.dll from local file system or from inside JAR", ex2);
         }
      }

      initializeLibrary();
      this.keycodeMap = getKey2KeycodeMapping();
   }

   /**
    * Pulls a file out of the JAR and puts it on the File Path.
    * <p>
    * @param jarPath the path to the JAR
    * @param filePath the file path to extract to
    * @throws IOException if any IO error occurs
    */
   private void fromJarToFs(String jarPath, String filePath) throws IOException {
      File file = new File(filePath);
      if (file.exists()) {
         boolean success = file.delete();
         if (!success) {
            throw new IOException("couldn't delete " + filePath);
         }
      }
      InputStream is = null;
      OutputStream os = null;
      try {
         is = ClassLoader.getSystemClassLoader().getResourceAsStream(jarPath);
         os = new FileOutputStream(filePath);
         byte[] buffer = new byte[8192];
         int bytesRead;
         while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
         }
      } finally {
         if (is != null) {
            is.close();
         }
         if (os != null) {
            os.close();
         }
      }
   }

   /**
    * Gets the singleton instance of the JIntellitype object.
    * <p>
    * But the possibility of creation of more instance is only before the
    * instance is created. Since all code defined inside getInstance method is
    * in the synchronized block, even the subsequent requests will also come and
    * wait in the synchronized block. This is a performance issue. The same can
    * be solved using double-checked lock. Following is the implementation of
    * Singleton with lazy initialization and double-checked lock.
    * <p>
    * @return an instance of JIntellitype class
    */
   public static JIntellitype getInstance() {
      if (!isInitialized) {
         synchronized (JIntellitype.class) {
            if (!isInitialized) {
               jintellitype = new JIntellitype();
               isInitialized = true;
            }
         }
      }
      return jintellitype;
   }

   /**
    * Adds a listener for hotkeys.
    * <p>
    * @param listener the HotKeyListener to be added
    */
   public void addHotKeyListener(HotkeyListener listener) {
      hotkeyListeners.add(listener);
   }

   /**
    * Adds a listener for intellitype commands.
    * <p>
    * @param listener the IntellitypeListener to be added
    */
   public void addIntellitypeListener(IntellitypeListener listener) {
      intellitypeListeners.add(listener);
   }

   /**
    * Cleans up all resources used by JIntellitype.
    */
   public void cleanUp() {
      try {
         terminate();
      } catch (UnsatisfiedLinkError ex) {
         throw new JIntellitypeException(ERROR_MESSAGE, ex);
      } catch (RuntimeException ex) {
         throw new JIntellitypeException(ex);
      }
   }

   /**
    * Registers a Hotkey with windows. This combination will be responded to by
    * all registered HotKeyListeners. Uses the JIntellitypeConstants for MOD,
    * ALT, CTRL, and WINDOWS keys.
    * <p>
    * @param identifier a unique identifier for this key combination
    * @param modifier MOD_SHIFT, MOD_ALT, MOD_CONTROL, MOD_WIN from
    *           JIntellitypeConstants, or 0 if no modifier needed
    * @param keycode the key to respond to in Ascii integer, 65 for A
    */
   public void registerHotKey(int identifier, int modifier, int keycode) {
      try {
         int modifiers = swingToIntelliType(modifier);
         if (modifiers == 0) {
            modifiers = modifier;
         }
         regHotKey(identifier, modifier, keycode);
      } catch (UnsatisfiedLinkError ex) {
         throw new JIntellitypeException(ERROR_MESSAGE, ex);
      } catch (RuntimeException ex) {
         throw new JIntellitypeException(ex);
      }
   }

   /**
    * Registers a Hotkey with windows. This combination will be responded to by
    * all registered HotKeyListeners. Use the Swing InputEvent constants from
    * java.awt.InputEvent.
    * <p>
    * @param identifier a unique identifier for this key combination
    * @param modifier InputEvent.SHIFT_MASK, InputEvent.ALT_MASK,
    *           InputEvent.CTRL_MASK, or 0 if no modifier needed
    * @param keycode the key to respond to in Ascii integer, 65 for A
    */
   public void registerSwingHotKey(int identifier, int modifier, int keycode) {
      try {
         regHotKey(identifier, swingToIntelliType(modifier), keycode);
      } catch (UnsatisfiedLinkError ex) {
         throw new JIntellitypeException(ERROR_MESSAGE, ex);
      } catch (RuntimeException ex) {
         throw new JIntellitypeException(ex);
      }
   }

   /**
    * Registers a Hotkey with windows. This combination will be responded to by
    * all registered HotKeyListeners. Use the identifiers CTRL, SHIFT, ALT
    * and/or WIN.
    * <p>
    * @param identifier a unique identifier for this key combination
    * @param modifierAndKeyCode String with modifiers separated by + and keycode
    *           (e.g. CTRL+SHIFT+A)
    * @see #registerHotKey(int, int, int)
    * @see #registerSwingHotKey(int, int, int)
    */
   public void registerHotKey(int identifier, String modifierAndKeyCode) {
      String[] split = modifierAndKeyCode.split("\\+");
      int mask = 0;
      int keycode = 0;

      for (int i = 0; i < split.length; i++) {
         if ("ALT".equalsIgnoreCase(split[i])) {
            mask += JIntellitype.MOD_ALT;
         } else if ("CTRL".equalsIgnoreCase(split[i]) || "CONTROL".equalsIgnoreCase(split[i])) {
            mask += JIntellitype.MOD_CONTROL;
         } else if ("SHIFT".equalsIgnoreCase(split[i])) {
            mask += JIntellitype.MOD_SHIFT;
         } else if ("WIN".equalsIgnoreCase(split[i])) {
            mask += JIntellitype.MOD_WIN;
         } else if (keycodeMap.containsKey(split[i].toLowerCase())) {
            keycode = keycodeMap.get(split[i].toLowerCase());
         }
      }
      registerHotKey(identifier, mask, keycode);
   }

   /**
    * Removes a listener for hotkeys.
    */
   public void removeHotKeyListener(HotkeyListener listener) {
      hotkeyListeners.remove(listener);
   }

   /**
    * Removes a listener for intellitype commands.
    */
   public void removeIntellitypeListener(IntellitypeListener listener) {
      intellitypeListeners.remove(listener);
   }

   /**
    * Unregisters a previously registered Hotkey identified by its unique
    * identifier.
    * <p>
    * @param identifier the unique identifer of this Hotkey
    */
   public void unregisterHotKey(int identifier) {
      try {
         unregHotKey(identifier);
      } catch (UnsatisfiedLinkError ex) {
         throw new JIntellitypeException(ERROR_MESSAGE, ex);
      } catch (RuntimeException ex) {
         throw new JIntellitypeException(ex);
      }
   }

   /**
    * Checks to see if this application is already running.
    * <p>
    * @param appTitle the name of the application to check for
    * @return true if running, false if not running
    */
   public static boolean checkInstanceAlreadyRunning(String appTitle) {
      return getInstance().isRunning(appTitle);
   }

   /**
    * Checks to make sure the OS is a Windows flavor and that the JIntellitype
    * DLL is found in the path and the JDK is 32 bit not 64 bit. The DLL
    * currently only supports 32 bit JDK.
    * <p>
    * @return true if Jintellitype may be used, false if not
    */
   public static boolean isJIntellitypeSupported() {
      boolean result = false;
      String os = "none";

      try {
         os = System.getProperty("os.name").toLowerCase();
      } catch (SecurityException ex) {
         // we are not allowed to look at this property
         System.err.println("Caught a SecurityException reading the system property "
                  + "'os.name'; the SystemUtils property value will default to null.");
      }

      // only works on Windows OS currently
      if (os.startsWith("windows")) {
         // try an get the instance and if it succeeds then return true
         try {
            getInstance();
            result = true;
         } catch (Exception e) {
            result = false;
         }
      }

      return result;
   }

   /**
    * Gets the libraryLocation.
    * <p>
    * @return Returns the libraryLocation.
    */
   public static String getLibraryLocation() {
      return libraryLocation;
   }

   /**
    * Sets the libraryLocation.
    * <p>
    * @param libraryLocation The libraryLocation to set.
    */
   public static void setLibraryLocation(String libraryLocation) {
      final File dll = new File(libraryLocation);
      if (!dll.isAbsolute()) {
         JIntellitype.libraryLocation = dll.getAbsolutePath();
      } else {
         // absolute path, no further calculation needed
         JIntellitype.libraryLocation = libraryLocation;
      }
   }

   /**
    * Notifies all listeners that Hotkey was pressed.
    * <p>
    * @param identifier the unique identifier received
    */
   protected void onHotKey(final int identifier) {
      for (final HotkeyListener hotkeyListener : hotkeyListeners) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               hotkeyListener.onHotKey(identifier);
            }
         });
      }
   }

   /**
    * Notifies all listeners that Intellitype command was received.
    * <p>
    * @param command the unique WM_APPCOMMAND received
    */
   protected void onIntellitype(final int command) {
      for (final IntellitypeListener intellitypeListener : intellitypeListeners) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               intellitypeListener.onIntellitype(command);
            }
         });
      }
   }

   /**
    * Swing modifier value to Jintellipad conversion. If no conversion needed
    * just return the original value. This lets users pass either the original
    * JIntellitype constants or Swing InputEvent constants.
    * <p>
    * @param swingKeystrokeModifier the Swing KeystrokeModifier to check
    * @return Jintellitype the JIntellitype modifier value
    */
   protected static int swingToIntelliType(int swingKeystrokeModifier) {
      int mask = 0;
      if ((swingKeystrokeModifier & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
         mask += JIntellitype.MOD_SHIFT;
      }
      if ((swingKeystrokeModifier & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) {
         mask += JIntellitype.MOD_ALT;
      }
      if ((swingKeystrokeModifier & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
         mask += JIntellitype.MOD_CONTROL;
      }
      if ((swingKeystrokeModifier & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK) {
         mask += JIntellitype.MOD_SHIFT;
      }
      if ((swingKeystrokeModifier & InputEvent.ALT_DOWN_MASK) == InputEvent.ALT_DOWN_MASK) {
         mask += JIntellitype.MOD_ALT;
      }
      if ((swingKeystrokeModifier & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
         mask += JIntellitype.MOD_CONTROL;
      }
      return mask;
   }

   /**
    * Puts all constants from {@link java.awt.event.KeyEvent} in a keycodeMap.
    * The key is the lower case form of it.
    * @return Map containing key->keycode mapping DOCU Now enables the user to
    *         use all keys specified here instead of just [A-Z],[0-9] as before
    */
   private HashMap<String, Integer> getKey2KeycodeMapping() {
      HashMap<String, Integer> map = new HashMap<String, Integer>();

      map.put("first", KeyEvent.KEY_FIRST);
      map.put("last", KeyEvent.KEY_LAST);
      map.put("typed", KeyEvent.KEY_TYPED);
      map.put("pressed", KeyEvent.KEY_PRESSED);
      map.put("released", KeyEvent.KEY_RELEASED);
      map.put("enter", 13);
      map.put("back_space", KeyEvent.VK_BACK_SPACE);
      map.put("tab", KeyEvent.VK_TAB);
      map.put("cancel", KeyEvent.VK_CANCEL);
      map.put("clear", KeyEvent.VK_CLEAR);
      map.put("pause", KeyEvent.VK_PAUSE);
      map.put("caps_lock", KeyEvent.VK_CAPS_LOCK);
      map.put("escape", KeyEvent.VK_ESCAPE);
      map.put("space", KeyEvent.VK_SPACE);
      map.put("page_up", KeyEvent.VK_PAGE_UP);
      map.put("page_down", KeyEvent.VK_PAGE_DOWN);
      map.put("end", KeyEvent.VK_END);
      map.put("home", KeyEvent.VK_HOME);
      map.put("left", KeyEvent.VK_LEFT);
      map.put("up", KeyEvent.VK_UP);
      map.put("right", KeyEvent.VK_RIGHT);
      map.put("down", KeyEvent.VK_DOWN);
      map.put("comma", 188);
      map.put("minus", 109);
      map.put("period", 110);
      map.put("slash", 191);
      map.put("0", KeyEvent.VK_0);
      map.put("1", KeyEvent.VK_1);
      map.put("2", KeyEvent.VK_2);
      map.put("3", KeyEvent.VK_3);
      map.put("4", KeyEvent.VK_4);
      map.put("5", KeyEvent.VK_5);
      map.put("6", KeyEvent.VK_6);
      map.put("7", KeyEvent.VK_7);
      map.put("8", KeyEvent.VK_8);
      map.put("9", KeyEvent.VK_9);
      map.put("semicolon", 186);
      map.put("equals", 187);
      map.put("a", KeyEvent.VK_A);
      map.put("b", KeyEvent.VK_B);
      map.put("c", KeyEvent.VK_C);
      map.put("d", KeyEvent.VK_D);
      map.put("e", KeyEvent.VK_E);
      map.put("f", KeyEvent.VK_F);
      map.put("g", KeyEvent.VK_G);
      map.put("h", KeyEvent.VK_H);
      map.put("i", KeyEvent.VK_I);
      map.put("j", KeyEvent.VK_J);
      map.put("k", KeyEvent.VK_K);
      map.put("l", KeyEvent.VK_L);
      map.put("m", KeyEvent.VK_M);
      map.put("n", KeyEvent.VK_N);
      map.put("o", KeyEvent.VK_O);
      map.put("p", KeyEvent.VK_P);
      map.put("q", KeyEvent.VK_Q);
      map.put("r", KeyEvent.VK_R);
      map.put("s", KeyEvent.VK_S);
      map.put("t", KeyEvent.VK_T);
      map.put("u", KeyEvent.VK_U);
      map.put("v", KeyEvent.VK_V);
      map.put("w", KeyEvent.VK_W);
      map.put("x", KeyEvent.VK_X);
      map.put("y", KeyEvent.VK_Y);
      map.put("z", KeyEvent.VK_Z);
      map.put("open_bracket", 219);
      map.put("back_slash", 220);
      map.put("close_bracket", 221);
      map.put("numpad0", KeyEvent.VK_NUMPAD0);
      map.put("numpad1", KeyEvent.VK_NUMPAD1);
      map.put("numpad2", KeyEvent.VK_NUMPAD2);
      map.put("numpad3", KeyEvent.VK_NUMPAD3);
      map.put("numpad4", KeyEvent.VK_NUMPAD4);
      map.put("numpad5", KeyEvent.VK_NUMPAD5);
      map.put("numpad6", KeyEvent.VK_NUMPAD6);
      map.put("numpad7", KeyEvent.VK_NUMPAD7);
      map.put("numpad8", KeyEvent.VK_NUMPAD8);
      map.put("numpad9", KeyEvent.VK_NUMPAD9);
      map.put("multiply", KeyEvent.VK_MULTIPLY);
      map.put("add", KeyEvent.VK_ADD);
      map.put("separator", KeyEvent.VK_SEPARATOR);
      map.put("subtract", KeyEvent.VK_SUBTRACT);
      map.put("decimal", KeyEvent.VK_DECIMAL);
      map.put("divide", KeyEvent.VK_DIVIDE);
      map.put("delete", 46);
      map.put("num_lock", KeyEvent.VK_NUM_LOCK);
      map.put("scroll_lock", KeyEvent.VK_SCROLL_LOCK);
      map.put("f1", KeyEvent.VK_F1);
      map.put("f2", KeyEvent.VK_F2);
      map.put("f3", KeyEvent.VK_F3);
      map.put("f4", KeyEvent.VK_F4);
      map.put("f5", KeyEvent.VK_F5);
      map.put("f6", KeyEvent.VK_F6);
      map.put("f7", KeyEvent.VK_F7);
      map.put("f8", KeyEvent.VK_F8);
      map.put("f9", KeyEvent.VK_F9);
      map.put("f10", KeyEvent.VK_F10);
      map.put("f11", KeyEvent.VK_F11);
      map.put("f12", KeyEvent.VK_F12);
      map.put("f13", KeyEvent.VK_F13);
      map.put("f14", KeyEvent.VK_F14);
      map.put("f15", KeyEvent.VK_F15);
      map.put("f16", KeyEvent.VK_F16);
      map.put("f17", KeyEvent.VK_F17);
      map.put("f18", KeyEvent.VK_F18);
      map.put("f19", KeyEvent.VK_F19);
      map.put("f20", KeyEvent.VK_F20);
      map.put("f21", KeyEvent.VK_F21);
      map.put("f22", KeyEvent.VK_F22);
      map.put("f23", KeyEvent.VK_F23);
      map.put("f24", KeyEvent.VK_F24);
      map.put("printscreen", 44);
      map.put("insert", 45);
      map.put("help", 47);
      map.put("meta", KeyEvent.VK_META);
      map.put("back_quote", KeyEvent.VK_BACK_QUOTE);
      map.put("quote", KeyEvent.VK_QUOTE);
      map.put("kp_up", KeyEvent.VK_KP_UP);
      map.put("kp_down", KeyEvent.VK_KP_DOWN);
      map.put("kp_left", KeyEvent.VK_KP_LEFT);
      map.put("kp_right", KeyEvent.VK_KP_RIGHT);
      map.put("dead_grave", KeyEvent.VK_DEAD_GRAVE);
      map.put("dead_acute", KeyEvent.VK_DEAD_ACUTE);
      map.put("dead_circumflex", KeyEvent.VK_DEAD_CIRCUMFLEX);
      map.put("dead_tilde", KeyEvent.VK_DEAD_TILDE);
      map.put("dead_macron", KeyEvent.VK_DEAD_MACRON);
      map.put("dead_breve", KeyEvent.VK_DEAD_BREVE);
      map.put("dead_abovedot", KeyEvent.VK_DEAD_ABOVEDOT);
      map.put("dead_diaeresis", KeyEvent.VK_DEAD_DIAERESIS);
      map.put("dead_abovering", KeyEvent.VK_DEAD_ABOVERING);
      map.put("dead_doubleacute", KeyEvent.VK_DEAD_DOUBLEACUTE);
      map.put("dead_caron", KeyEvent.VK_DEAD_CARON);
      map.put("dead_cedilla", KeyEvent.VK_DEAD_CEDILLA);
      map.put("dead_ogonek", KeyEvent.VK_DEAD_OGONEK);
      map.put("dead_iota", KeyEvent.VK_DEAD_IOTA);
      map.put("dead_voiced_sound", KeyEvent.VK_DEAD_VOICED_SOUND);
      map.put("dead_semivoiced_sound", KeyEvent.VK_DEAD_SEMIVOICED_SOUND);
      map.put("ampersand", KeyEvent.VK_AMPERSAND);
      map.put("asterisk", KeyEvent.VK_ASTERISK);
      map.put("quotedbl", KeyEvent.VK_QUOTEDBL);
      map.put("less", KeyEvent.VK_LESS);
      map.put("greater", KeyEvent.VK_GREATER);
      map.put("braceleft", KeyEvent.VK_BRACELEFT);
      map.put("braceright", KeyEvent.VK_BRACERIGHT);
      map.put("at", KeyEvent.VK_AT);
      map.put("colon", KeyEvent.VK_COLON);
      map.put("circumflex", KeyEvent.VK_CIRCUMFLEX);
      map.put("dollar", KeyEvent.VK_DOLLAR);
      map.put("euro_sign", KeyEvent.VK_EURO_SIGN);
      map.put("exclamation_mark", KeyEvent.VK_EXCLAMATION_MARK);
      map.put("inverted_exclamation_mark", KeyEvent.VK_INVERTED_EXCLAMATION_MARK);
      map.put("left_parenthesis", KeyEvent.VK_LEFT_PARENTHESIS);
      map.put("number_sign", KeyEvent.VK_NUMBER_SIGN);
      map.put("plus", KeyEvent.VK_PLUS);
      map.put("right_parenthesis", KeyEvent.VK_RIGHT_PARENTHESIS);
      map.put("underscore", KeyEvent.VK_UNDERSCORE);
      map.put("context_menu", KeyEvent.VK_CONTEXT_MENU);
      map.put("final", KeyEvent.VK_FINAL);
      map.put("convert", KeyEvent.VK_CONVERT);
      map.put("nonconvert", KeyEvent.VK_NONCONVERT);
      map.put("accept", KeyEvent.VK_ACCEPT);
      map.put("modechange", KeyEvent.VK_MODECHANGE);
      map.put("kana", KeyEvent.VK_KANA);
      map.put("kanji", KeyEvent.VK_KANJI);
      map.put("alphanumeric", KeyEvent.VK_ALPHANUMERIC);
      map.put("katakana", KeyEvent.VK_KATAKANA);
      map.put("hiragana", KeyEvent.VK_HIRAGANA);
      map.put("full_width", KeyEvent.VK_FULL_WIDTH);
      map.put("half_width", KeyEvent.VK_HALF_WIDTH);
      map.put("roman_characters", KeyEvent.VK_ROMAN_CHARACTERS);
      map.put("all_candidates", KeyEvent.VK_ALL_CANDIDATES);
      map.put("previous_candidate", KeyEvent.VK_PREVIOUS_CANDIDATE);
      map.put("code_input", KeyEvent.VK_CODE_INPUT);
      map.put("japanese_katakana", KeyEvent.VK_JAPANESE_KATAKANA);
      map.put("japanese_hiragana", KeyEvent.VK_JAPANESE_HIRAGANA);
      map.put("japanese_roman", KeyEvent.VK_JAPANESE_ROMAN);
      map.put("kana_lock", KeyEvent.VK_KANA_LOCK);
      map.put("input_method_on_off", KeyEvent.VK_INPUT_METHOD_ON_OFF);
      map.put("cut", KeyEvent.VK_CUT);
      map.put("copy", KeyEvent.VK_COPY);
      map.put("paste", KeyEvent.VK_PASTE);
      map.put("undo", KeyEvent.VK_UNDO);
      map.put("again", KeyEvent.VK_AGAIN);
      map.put("find", KeyEvent.VK_FIND);
      map.put("props", KeyEvent.VK_PROPS);
      map.put("stop", KeyEvent.VK_STOP);
      map.put("compose", KeyEvent.VK_COMPOSE);
      map.put("alt_graph", KeyEvent.VK_ALT_GRAPH);
      map.put("begin", KeyEvent.VK_BEGIN);

      return map;
   }

   private synchronized native void initializeLibrary() throws UnsatisfiedLinkError;

   private synchronized native void regHotKey(int identifier, int modifier, int keycode) throws UnsatisfiedLinkError;

   private synchronized native void terminate() throws UnsatisfiedLinkError;

   private synchronized native void unregHotKey(int identifier) throws UnsatisfiedLinkError;

   /**
    * Checks if there's an instance with hidden window title = appName running
    * Can be used to detect that another instance of your app is already running
    * (so exit..)
    * <p>
    * @param appName = the title of the hidden window to search for
    */
   private synchronized native boolean isRunning(String appName);
}