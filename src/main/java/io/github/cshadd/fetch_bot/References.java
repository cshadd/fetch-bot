/*
 * MIT License
 * 
 * Copyright (c) 2018 Christian Shadd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * https://cshadd.github.io/fetch-bot/
 */
package io.github.cshadd.fetch_bot;

// Main

/**
 * The Class References. You must change the values in here to reflect any
 * custom builds.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha.1
 */
public final class References implements FetchBot {
    // Public Constant Instance/Property Fields
    
    /**
     * The Constant APACHE_HTTP_SERVER_PATH.
     */
    public static final String APACHE_HTTP_SERVER_PATH = "/var/www/html/";
    
    /**
     * The Constant ARDUINO_SERIAL_PORT.
     */
    public static final String ARDUINO_SERIAL_PORT = "/dev/ttyACM0";
    
    /**
     * The Constant CAMERA_PORT.
     */
    public static final int CAMERA_PORT = 0;
    
    /**
     * The Constant CUSTOM_PROGRAM.
     */
    public static final boolean CUSTOM_PROGRAM = false;
    
    /**
     * The Constant HUD_STREAM_PORT.
     */
    public static final int HUD_STREAM_PORT = 8855;
    
    /**
     * The Constant PROGRAM_LIBRARY_PATH.
     */
    public static final String PROGRAM_LIBRARY_PATH = "./libs/fetch-bot/";
    
    /**
     * The Constant WEB_INTERFACE_FOLDER_NAME.
     */
    public static final String WEB_INTERFACE_FOLDER_NAME = "FetchBot";
    
    /**
     * The Constant WEB_INTERFACE_PATH.
     */
    public static final String WEB_INTERFACE_PATH = APACHE_HTTP_SERVER_PATH
                    + WEB_INTERFACE_FOLDER_NAME;
}
