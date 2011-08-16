/*
	JIntellitype (http://www.melloware.com/)
	Java JNI API for Windows Intellitype commands and global keystrokes.

	Copyright (C) 1999, 2008 Emil A. Lefkof III, info@melloware.com

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0
 
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.


	Compiled with Mingw port of GCC,
	Bloodshed Dev-C++ IDE (http://www.bloodshed.net/devcpp.html)
*/
#include "stdafx.h"
#include "JIntellitypeHandler.h"
#include "JIntellitypeThread.h"
#include <stdlib.h>


UINT WM_SHELLHOOK = 0;

/*
 * Extract the unique handlerID from the java object
 */
JIntellitypeHandler *JIntellitypeHandler::extract( JNIEnv *env, jobject object )
{                
	// Get field ID
	jfieldID l_handlerId = env->GetFieldID( env->GetObjectClass( object ), "handler", "I" );

	// Get field
	JIntellitypeHandler *l_handler = (JIntellitypeHandler *) env->GetIntField( object, l_handlerId );

	return l_handler;
}

/*
 * Constructor
 */
JIntellitypeHandler::JIntellitypeHandler( JNIEnv *env, jobject object )
{                              
	m_window = NULL;

	// Reference object
	m_object = env->NewGlobalRef(object );

	// Get method IDs
	m_fireHotKey = env->GetMethodID( env->GetObjectClass( m_object ) , "onHotKey", "(I)V" );
	m_fireIntellitype = env->GetMethodID(  env->GetObjectClass( m_object ) , "onIntellitype", "(I)V" );

	// Get field ID
	jfieldID l_handlerId = env->GetFieldID(  env->GetObjectClass( m_object ) , "handler", "I" );

	// Set field
	env->SetIntField( m_object, l_handlerId, (jint) this );
}

/*
 * Destructor
 */
JIntellitypeHandler::~JIntellitypeHandler()
{
	// Get field ID
	jfieldID l_handlerId = g_JIntellitypeThread.m_env->GetFieldID( g_JIntellitypeThread.m_env->GetObjectClass( m_object ), "handler", "I" );

	// Set field
	g_JIntellitypeThread.m_env->SetIntField( m_object, l_handlerId, 0 );

	// Release our reference
	g_JIntellitypeThread.m_env->DeleteGlobalRef( m_object );
	
	// unregister the shell hook
	DeregisterShellHookWindow( m_window );

	// Destroy window
	DestroyWindow( m_window );
}


/*
 * Perform initialization of the object and thread.
 */
void JIntellitypeHandler::initialize( JNIEnv *env, HINSTANCE instance )
{
    m_instance = instance;
	g_JIntellitypeThread.MakeSureThreadIsUp( env );
	while( !PostThreadMessage( g_JIntellitypeThread, WM_JINTELLITYPE, INITIALIZE_CODE, (LPARAM) this ) )
		Sleep( 0 );
}

/*
 * Callback method that creates the hidden window on initialization to receive
 * any WM_HOTKEY messages and process them.
 */
void JIntellitypeHandler::doInitialize()
{
	// Register window class
	WNDCLASSEX l_Class;
	l_Class.cbSize = sizeof( l_Class );
	l_Class.style = CS_HREDRAW | CS_VREDRAW;
	l_Class.lpszClassName = TEXT( "JIntellitypeHandlerClass" );
	l_Class.lpfnWndProc = WndProc;
	l_Class.hbrBackground = (HBRUSH)GetStockObject(BLACK_BRUSH);
	l_Class.hCursor = NULL;
	l_Class.hIcon = NULL;
	l_Class.hIconSm = NULL;
	l_Class.lpszMenuName = NULL;
	l_Class.cbClsExtra = 0;
	l_Class.cbWndExtra = 0;
	l_Class.hInstance = m_instance;

	if( !RegisterClassEx( &l_Class ) )
		return;

	// Create window
	m_window = CreateWindow
	(
		TEXT( "JIntellitypeHandlerClass" ),
		TEXT( "JIntellitypeHandler" ),
		WS_OVERLAPPEDWINDOW,
		0, 0, 0, 0,
		NULL,
		NULL,
		m_instance,
		NULL
	);

	if( !m_window )
		return;

	 //Set pointer to this object inside the Window's USERDATA section
	SetWindowLong( m_window, GWL_USERDATA, (LONG) this );
	
	// hide the window
	ShowWindow(m_window, SW_HIDE);
	UpdateWindow(m_window);
	
	//register this window as a shell hook to intercept WM_APPCOMMAND messages
	WM_SHELLHOOK = RegisterWindowMessage(TEXT("SHELLHOOK"));
	BOOL (__stdcall *RegisterShellHookWindow)(HWND) = NULL;
	RegisterShellHookWindow = (BOOL (__stdcall *)(HWND))GetProcAddress(GetModuleHandle("USER32.DLL"), "RegisterShellHookWindow");
	
	//make sure it worked
	if (!RegisterShellHookWindow(m_window)) {
        // throw exception
		jclass JIntellitypeException = g_JIntellitypeThread.m_env->FindClass("com/melloware/jintellitype/JIntellitypeException");
        g_JIntellitypeThread.m_env->ThrowNew(JIntellitypeException,"Could not register window as a shell hook window.");                                     
    }
}

/*
 * Registers a hotkey. 
 * identifier - unique identifier assigned to this key comination
 * modifier - ALT, SHIFT, CTRL, WIN or combination of
 * keycode- Ascii keycode, 65 for A, 66 for B etc
 */
void JIntellitypeHandler::regHotKey( jint identifier, jint modifier, jint keycode )
{
	JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) malloc(sizeof(JIntellitypeHandlerCallback));
	callback->identifier = identifier;
	callback->modifier = modifier;
	callback->keycode = keycode;
	callback->handler = this;
	PostThreadMessage( g_JIntellitypeThread, WM_JINTELLITYPE, REGISTER_HOTKEY_CODE, (LPARAM) callback );
}

/*
 * Actually registers the hotkey using the Win32API RegisterHotKey call.
 */
void JIntellitypeHandler::doRegHotKey(LPARAM callback_)
{
	JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) callback_;
	RegisterHotKey(m_window, callback->identifier, callback->modifier, callback->keycode);
	free(callback);
}

/*
 * Unregisters a previously assigned hotkey. 
 * identifier - unique identifier assigned to this key comination
 */
void JIntellitypeHandler::unregHotKey( jint identifier )
{
    JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) malloc(sizeof(JIntellitypeHandlerCallback));
	callback->identifier = identifier;
	callback->handler = this;
	PostThreadMessage( g_JIntellitypeThread, WM_JINTELLITYPE, UNREGISTER_HOTKEY_CODE, (LPARAM) callback );
}

/*
 * Actually unregisters the hotkey using the Win32API UnregisterHotKey call.
 */
void JIntellitypeHandler::doUnregisterHotKey(LPARAM callback_)
{
	JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) callback_;
    UnregisterHotKey(m_window, callback->identifier);
	free(callback);
}

/*
 * When an intellitype command is recieved by the JFrame this method is called 
 * to perform a callback to the Intellitype java listeners.
 * commandId - the unique command Id from the WM_APPCOMMAND listings
 */
void JIntellitypeHandler::intellitype( jint commandId )
{
    JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) malloc(sizeof(JIntellitypeHandlerCallback));
	callback->command = commandId;
	callback->handler = this;
	PostThreadMessage( g_JIntellitypeThread, WM_JINTELLITYPE, INTELLITYPE_CODE, (LPARAM) callback );
}

/*
 * Call the correct JVM with the intellitype command for the listeners listening.
 */
void JIntellitypeHandler::doIntellitype(LPARAM callback_)
{
	JIntellitypeHandlerCallback *callback = (JIntellitypeHandlerCallback*) callback_;
    g_JIntellitypeThread.m_env->CallVoidMethod(m_object, m_fireIntellitype, callback->command);
	free(callback);
}

/*
 * Cleans up resources allocated by JIntellitype.
 */
void JIntellitypeHandler::terminate()
{
	PostThreadMessage( g_JIntellitypeThread, WM_JINTELLITYPE, TERMINATE_CODE, (LPARAM) this );
}

/*
 * Callback method to send hotkey to the Java HotKeyListeners registered.
 */
void JIntellitypeHandler::fireHotKey(jint hotkeyId)
{
	g_JIntellitypeThread.m_env->CallVoidMethod(m_object, m_fireHotKey, hotkeyId); 	
}


/*
 * WndProc method registered to the hidden window to listen for WM_HOTKEY 
 * messages and send them back to the Java listeners.
 */
LRESULT CALLBACK JIntellitypeHandler::WndProc( HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam )
{
      
    // check for Intellitype messages and if found send them to Intellitype listeners
    if (uMessage == WM_SHELLHOOK) {
        if (wParam == HSHELL_APPCOMMAND) {
           jint cmd  = GET_APPCOMMAND_LPARAM(lParam);        
           JIntellitypeHandler *l_this = (JIntellitypeHandler *) GetWindowLong( hWnd, GWL_USERDATA ); 
           l_this->intellitype(cmd);          
        }
        return TRUE;        
    } 
    
    // check for registered hotkey messages and send them to HotKeyListeners
    switch( uMessage ) {
        case WM_HOTKEY: {
            JIntellitypeHandler *l_this = (JIntellitypeHandler *) GetWindowLong( hWnd, GWL_USERDATA ); 
        	l_this->fireHotKey(wParam);
            return TRUE;
    		break;      
        }
    default:
        return DefWindowProc( hWnd, uMessage, wParam, lParam );    
	}

}



