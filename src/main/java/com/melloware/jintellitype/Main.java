/*
 * JIntellitype
 * -----------------
 * Copyright 2005-2019 Emil A. Lefkof III, Melloware Inc.
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

import java.util.Properties;

/**
 * Simple executable class that is used as the Main-Class in the JIntellitype 
 * jar. Outputs version information and other information about the environment 
 * on which the jar is being executed.
 * <p>
 * Copyright (c) 1999-2019 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <mellowaredev@gmail.com>
 * @version 1.4.0
 */
public final class Main {

    /**
     * Private constructor to make sure this class is never instantiated.
     *
     */
    private Main() {
        // private constructor to make singleton.
    }

    /** Main method that does what the class level javadoc states. */
    public static void main(String[] argv) {
        System.out.println("JIntellitype version \"" + getProjectVersion() + "\"");
        System.out.println(" ");

        System.out.println("Running on java version \"" + System.getProperty("java.version") + "\""
                           + " (build " + System.getProperty("java.runtime.version") + ")"
                           + " from " + System.getProperty("java.vendor"));

        System.out.println("Operating environment \"" + System.getProperty("os.name") + "\""
                           + " version " + System.getProperty("os.version") + " on " + System.getProperty("os.arch"));

        System.out.println("For more information on JIntellitype please visit http://www.melloware.com");
    }

    /**
     * Attempts to read the version number out of the pom.properties.  If not found
     * then RUNNING.IN.IDE.FULL is returned as the version.
     * <p>
     * @return the full version number of this application
     */
    static String getProjectVersion() {
        String version;

        try {
            final Properties pomProperties = new Properties();
            pomProperties.load(Main.class.getResourceAsStream("/META-INF/maven/com.melloware/jintellitype/pom.properties"));
            version = pomProperties.getProperty("version");
        } catch (Exception e) {
            version = "RUNNING.IN.IDE.FULL";
        }
        return version;
    }
}