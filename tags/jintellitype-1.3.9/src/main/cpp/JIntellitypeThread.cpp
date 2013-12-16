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
#include "JIntellitypeThread.h"
#include "JIntellitypeHandler.h"

JIntellitypeThread g_JIntellitypeThread;


JIntellitypeThread::JIntellitypeThread()
{
	m_env = NULL;
	m_thread = 0;
	m_vm = NULL;
	m_handlerCount = 0;
}


void JIntellitypeThread::MakeSureThreadIsUp( JNIEnv *env )
{
	if( !m_thread )
	{
		// Get VM
		env->GetJavaVM( &m_vm );

		// Start "native" thread
		CreateThread
		(
			NULL,
			0,
			ThreadProc,
			this,
			0,
			&m_thread
		);
	}
}


JIntellitypeThread::operator DWORD ()
{
	return m_thread;
}


DWORD WINAPI JIntellitypeThread::ThreadProc( LPVOID lpParameter )
{
	JIntellitypeThread *l_this = (JIntellitypeThread *) lpParameter;

	// Attach the thread to the VM 
	l_this->m_vm->AttachCurrentThread( (void**) &l_this->m_env, NULL );

	MSG msg;
	while( GetMessage( &msg, NULL, 0, 0 ) )
	{
		if( msg.message == WM_JINTELLITYPE )
		{
			// Extract handler
			JIntellitypeHandler *l_handler;

			switch( msg.wParam )
			{
            case JIntellitypeHandler::INITIALIZE_CODE:
				l_handler = (JIntellitypeHandler*) msg.lParam;
				l_this->m_handlerCount++;
				l_handler->doInitialize();
				break;        
			case JIntellitypeHandler::REGISTER_HOTKEY_CODE:
				l_handler = ((JIntellitypeHandlerCallback*) msg.lParam)->handler;
				l_handler->doRegHotKey(msg.lParam);
				break;

			case JIntellitypeHandler::UNREGISTER_HOTKEY_CODE:
				l_handler = ((JIntellitypeHandlerCallback*) msg.lParam)->handler;
				l_handler->doUnregisterHotKey(msg.lParam);
				break;
			case JIntellitypeHandler::INTELLITYPE_CODE:
				l_handler = ((JIntellitypeHandlerCallback*) msg.lParam)->handler;
				l_handler->doIntellitype(msg.lParam);
				break;

			case JIntellitypeHandler::TERMINATE_CODE:
				l_handler = (JIntellitypeHandler*) msg.lParam;

				// Destroy it!
				delete l_handler;

				// No more handlers?
				if( !--l_this->m_handlerCount )
				{
					l_this->m_thread = 0;

					// Detach thread from VM
					l_this->m_vm->DetachCurrentThread();

					// Time to die
					ExitThread( 0 );
				}
				break;
			}
		}
		else
		{
			TranslateMessage( &msg );
			DispatchMessage( &msg );
		}
	}

	// Detach thread from VM 
	l_this->m_vm->DetachCurrentThread();

	return 0;
}
