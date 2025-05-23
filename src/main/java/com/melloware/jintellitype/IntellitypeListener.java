/**
 * JIntellitype
 * -----------------
 * Copyright 2005-2019 Emil A. Lefkof III, Melloware Inc.
 * <p>
 * I always give it my best shot to make a program useful and solid, but
 * remember that there is absolutely no warranty for using this program as
 * stated in the following terms:
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.melloware.jintellitype;


/**
 * Listener interface for Windows Intellitype events. Intellitype are Windows
 * App Commands that are specialand were introduced with Microsoft Keyboards 
 * that had special keys for Play, Pause, Stop, Next etc. for controlling
 * Media applications like Windows Media Player, Itunes, and Winamp.
 * <p>
 * If you have ever wanted your Swing/SWT application to respond to these global
 * events you now can with JIntellitype.  Just implement this interface, and
 * you can now take action when those special Media keys are pressed.
 * <p>
 * Copyright (c) 1999-2025 Melloware, Inc. <a href="http://www.melloware.com">Melloware</a>
 * @version 1.4.0
 * 
 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputmessages/wm_appcommand.asp">...</a>
 */
public interface IntellitypeListener
{
	/**
	 * Event fired when a WM_APPCOMMAND message is received that was initiated
	 * by this application.
	 * <p>
	 * @param command the WM_APPCOMMAND that was pressed
	 */
	void onIntellitype( int command );
}