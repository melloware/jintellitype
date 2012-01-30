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
 * Exception class for all JIntellitype Exceptions.
 * <p>
 * Copyright (c) 1999-2008
 * Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 1.3.1
 */
public class JIntellitypeException extends RuntimeException {

	
	public JIntellitypeException() {
		super();
	}

	
	public JIntellitypeException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	
	public JIntellitypeException(String aMessage) {
		super(aMessage);
	}


	public JIntellitypeException(Throwable aCause) {
		super(aCause);
	}

}
