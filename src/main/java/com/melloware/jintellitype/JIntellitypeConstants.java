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
 * Constants from the Windows API used in JIntellitype.
 * <p>
 * Message information can be found on MSDN here:
 * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputmessages/wm_appcommand.asp
 * <p>
 * Copyright (c) 1999-2008
 * Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.3.1
 */
public interface JIntellitypeConstants {
   
   public static final String ERROR_MESSAGE = "JIntellitype DLL Error";
	
	// Modifier keys, can be added together
	
	/**
	 * ALT key for registering Hotkeys.
	 */
	public static final int MOD_ALT = 1;
	
	/**
	 * CONTROL key for registering Hotkeys.
	 */
	public static final int MOD_CONTROL = 2;
	
	/**
	 * SHIFT key for registering Hotkeys.
	 */
	public static final int MOD_SHIFT = 4;
	
	/**
	 * WINDOWS key for registering Hotkeys.
	 */
	public static final int MOD_WIN = 8;
	
	
	// Intellitype Virtual Key Constants from MSDN  
	
	/**
	 * Browser Navigate backward
	 */
	public static final int  APPCOMMAND_BROWSER_BACKWARD       = 1;  
	
	/**
	 * Browser Navigate forward
	 */
	public static final int  APPCOMMAND_BROWSER_FORWARD        = 2; 
	
	/**
	 * Browser Refresh page
	 */
	public static final int  APPCOMMAND_BROWSER_REFRESH        = 3;  
	
	/**
	 * Browser Stop download
	 */
	public static final int  APPCOMMAND_BROWSER_STOP           = 4;  
	
	/**
	 * Browser Open search
	 */
	public static final int  APPCOMMAND_BROWSER_SEARCH         = 5;  
	
	/**
	 * Browser Open favorites
	 */
	public static final int  APPCOMMAND_BROWSER_FAVOURITES     = 6;  
	
	/**
	 * Browser Navigate home
	 */
	public static final int  APPCOMMAND_BROWSER_HOME           = 7;  
	
	/**
	 * Mute the volume
	 */
	public static final int  APPCOMMAND_VOLUME_MUTE            = 8;  
	
	/**
	 * Lower the volume
	 */
	public static final int  APPCOMMAND_VOLUME_DOWN            = 9;  
	
	/**
	 * Raise the volume
	 */
	public static final int  APPCOMMAND_VOLUME_UP              = 10;  
	
	/**
	 * Media application go to next track.
	 */
	public static final int  APPCOMMAND_MEDIA_NEXTTRACK        = 11;  
	
	/**
	 * Media application Go to previous track.
	 */
	public static final int  APPCOMMAND_MEDIA_PREVIOUSTRACK    = 12;  
	
	/**
	 * Media application Stop playback.
	 */
	public static final int  APPCOMMAND_MEDIA_STOP             = 13;  
	
	/**
	 * Media application Play or pause playback.
	 */
	public static final int  APPCOMMAND_MEDIA_PLAY_PAUSE       = 14;  
	
	/**
	 * Open mail application
	 */
	public static final int  APPCOMMAND_LAUNCH_MAIL            = 15; 
	
	/**
	 * Go to Media Select mode.
	 */
	public static final int  APPCOMMAND_LAUNCH_MEDIA_SELECT    = 16;  
	
	/**
	 * Start App1.
	 */
	public static final int  APPCOMMAND_LAUNCH_APP1            = 17;  
	
	/**
	 * Start App2.
	 */
	public static final int  APPCOMMAND_LAUNCH_APP2            = 18; 
	
	public static final int  APPCOMMAND_BASS_DOWN              = 19;
	public static final int  APPCOMMAND_BASS_BOOST             = 20;
	public static final int  APPCOMMAND_BASS_UP                = 21;
	public static final int  APPCOMMAND_TREBLE_DOWN            = 22;
	public static final int  APPCOMMAND_TREBLE_UP              = 23;
	public static final int  APPCOMMAND_MICROPHONE_VOLUME_MUTE = 24;
	public static final int  APPCOMMAND_MICROPHONE_VOLUME_DOWN = 25;
	public static final int  APPCOMMAND_MICROPHONE_VOLUME_UP   = 26;
	public static final int  APPCOMMAND_HELP                   = 27;
	public static final int  APPCOMMAND_FIND                   = 28;
	public static final int  APPCOMMAND_NEW                    = 29;
	public static final int  APPCOMMAND_OPEN                   = 30;
	public static final int  APPCOMMAND_CLOSE                  = 31;
	public static final int  APPCOMMAND_SAVE                   = 32;
	public static final int  APPCOMMAND_PRINT                  = 33;
	public static final int  APPCOMMAND_UNDO                   = 34;
	public static final int  APPCOMMAND_REDO                   = 35;
	public static final int  APPCOMMAND_COPY                   = 36;
	public static final int  APPCOMMAND_CUT                    = 37;
	public static final int  APPCOMMAND_PASTE                  = 38;
	public static final int  APPCOMMAND_REPLY_TO_MAIL          = 39;
	public static final int  APPCOMMAND_FORWARD_MAIL           = 40;
	public static final int  APPCOMMAND_SEND_MAIL              = 41;
	public static final int  APPCOMMAND_SPELL_CHECK            = 42;
	public static final int  APPCOMMAND_DICTATE_OR_COMMAND_CONTROL_TOGGLE = 43;
	public static final int  APPCOMMAND_MIC_ON_OFF_TOGGLE      = 44;
	public static final int  APPCOMMAND_CORRECTION_LIST        = 45;

}
