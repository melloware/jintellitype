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
#include "com_melloware_jintellitype_JIntellitype.h"
#include "JIntellitypeHandler.h"

HINSTANCE g_instance = NULL;


BOOL WINAPI DllMain
(
	HINSTANCE hinstDLL,  // handle to DLL module
	DWORD fdwReason,     // reason for calling function
	LPVOID lpvReserved   // reserved
)
{
    switch( fdwReason )
	{
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:

		case DLL_PROCESS_ATTACH:
			g_instance = hinstDLL;
			
			break;
    }
    return TRUE;
}


extern "C"
/*
 * Class:     com_melloware_jintellitype_JIntellitype
 * Method:    initializeLibrary
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_melloware_jintellitype_JIntellitype_initializeLibrary
  (JNIEnv *env, jobject object)
{
    // Get handler
    JIntellitypeHandler *l_handler = JIntellitypeHandler::extract( env, object ); 
    
    // Create our handler
    l_handler = new JIntellitypeHandler( env, object );
    
    // Enable it
    if( l_handler )
       l_handler->initialize(env, g_instance);
}

extern "C"
/*
 * Class:     com_melloware_jintellitype_JIntellitype
 * Method:    regHotKey
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_melloware_jintellitype_JIntellitype_regHotKey
  (JNIEnv *env, jobject object, jint identifier, jint modifier, jint keycode)
{
    // Get handler
    JIntellitypeHandler *l_handler = JIntellitypeHandler::extract( env, object ); 
  
  	if( l_handler )
	{
		l_handler->regHotKey(identifier, modifier, keycode);
	}
	else
	{
        // throw exception
		jclass JIntellitypeException = env->FindClass("com/melloware/jintellitype/JIntellitypeException");
        env->ThrowNew(JIntellitypeException,"JIntellitype was not initialized properly.");
	} 
}

extern "C" 
/*
 * Class:     com_melloware_jintellitype_JIntellitype
 * Method:    unregHotKey
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_melloware_jintellitype_JIntellitype_unregHotKey
  (JNIEnv *env, jobject object, jint identifier) 
{
  // Get handler
  JIntellitypeHandler *l_handler = JIntellitypeHandler::extract( env, object ); 
  
  	if( l_handler )
	{
		l_handler->unregHotKey(identifier);
	}
	else
	{
        // throw exception
		jclass JIntellitypeException = env->FindClass("com/melloware/jintellitype/JIntellitypeException");
        env->ThrowNew(JIntellitypeException,"JIntellitype was not initialized properly.");
	} 
   
}

extern "C" 
/*
 * Class:     com_melloware_jintellitype_JIntellitype
 * Method:    terminate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_melloware_jintellitype_JIntellitype_terminate
  (JNIEnv *env, jobject object)
{
  	// Get handler
	JIntellitypeHandler *l_handler = JIntellitypeHandler::extract( env, object );

	// Clean up all resources allocated by this API
	if( l_handler )
		l_handler->terminate(); 
     
}

extern "C" 
/*
 * Class:     com_melloware_jintellitype_JIntellitype
 * Method:    isRunning
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_melloware_jintellitype_JIntellitype_isRunning
  (JNIEnv *env, jclass, jstring wndName)
{
    // App name for the hidden window's registered class
    CHAR szAppName[] = "SunAwtFrame";
    const char *cWndName = env->GetStringUTFChars(wndName, 0);
	// Find out if there's a hidden window with the given title
	HWND mHwnd = FindWindow(szAppName, cWndName);
	env->ReleaseStringUTFChars(wndName, cWndName);
	// If there is, another instance of our app is already running
	return mHwnd != NULL;    
}

