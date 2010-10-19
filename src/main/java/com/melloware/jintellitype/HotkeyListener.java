/**
 * JIntellitype
 * -----------------
 * Copyright 2005-2008 Emil A. Lefkof III, Melloware Inc.
 *
 * I always give it my best shot to make a program useful and solid, but
 * remeber that there is absolutely no warranty for using this program as
 * stated in the following terms:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.melloware.jintellitype;


/**
 * Listener interface for Windows Hotkey events registered using the
 * Windows API call RegisterHotKey to globally listen for a key combination
 * regardless if your application has focus or not.
 * <p>
 * Copyright (c) 1999-2008
 * Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.3.1
 * 
 * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputfunctions/registerhotkey.asp
 */
public interface HotkeyListener
{
	/**
	 * Event fired when a WM_HOTKEY message is received that was initiated
	 * by this application.
	 * <p>
	 * @param identifier the unique Identifer the Hotkey was assigned
	 */
	void onHotKey( int identifier );
}