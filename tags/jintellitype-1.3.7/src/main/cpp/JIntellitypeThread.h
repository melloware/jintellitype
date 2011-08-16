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
#ifndef __JIntellitypeThread_h__
#define __JIntellitypeThread_h__


class JIntellitypeThread
{
public:

	JIntellitypeThread();

	void MakeSureThreadIsUp( JNIEnv *env );

	JNIEnv *m_env;
	static DWORD WINAPI ThreadProc( LPVOID lpParameter );

	operator DWORD ();

private:

	DWORD m_thread;
	JavaVM *m_vm;
	int m_handlerCount;

	
};

extern JIntellitypeThread g_JIntellitypeThread;


#define WM_JINTELLITYPE (WM_USER+1)


#endif
