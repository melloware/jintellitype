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
#ifndef __JIntellitypeHandler_h__
#define __JIntellitypeHandler_h__

#include "JIntellitypeThread.h"

class JIntellitypeHandler
{
	friend DWORD WINAPI JIntellitypeThread::ThreadProc( LPVOID lpParameter );

public:

	static JIntellitypeHandler *extract( JNIEnv *env, jobject object );

	JIntellitypeHandler( JNIEnv *env, jobject object );

	void initialize( JNIEnv *env, HINSTANCE instance );
	void regHotKey( jint identifier, jint modifier, jint keycode  );
	void unregHotKey( jint identifier );
	void intellitype( jint commandId );
	void terminate();
	
private:

	enum
	{
		INITIALIZE_CODE = 1,
        REGISTER_HOTKEY_CODE = 2,
		UNREGISTER_HOTKEY_CODE = 3,
		TERMINATE_CODE = 4,
		INTELLITYPE_CODE = 5
	};

	~JIntellitypeHandler();

	void createHiddenWindow();
	void doInitialize();
	void doRegHotKey(LPARAM callback);
	void doUnregisterHotKey(LPARAM callback);
	void doIntellitype(LPARAM callback);
	void fireHotKey(jint hotkeyId);
	void fireIntellitype(jint commandId);

	HINSTANCE m_instance;
	HWND m_window;
	jobject m_object;
	jmethodID m_fireHotKey;
	jmethodID m_fireIntellitype;

	static LRESULT CALLBACK WndProc( HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam );
	static DWORD WINAPI ThreadProc( LPVOID lpParameter );
};

typedef struct {
	JIntellitypeHandler *handler;
	jint identifier; 
	jint modifier; 
	jint keycode;
	jint command; 
} JIntellitypeHandlerCallback;


#ifndef WM_APPCOMMAND
#define WM_APPCOMMAND					0x319
#define FAPPCOMMAND_MASK				0x8000
#define GET_APPCOMMAND_LPARAM(lParam) ((short)(HIWORD(lParam) & ~FAPPCOMMAND_MASK))
#define HSHELL_APPCOMMAND			    12

#endif


#endif
