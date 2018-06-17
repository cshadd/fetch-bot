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
package io.github.cshadd.fetch_bot.io;

import io.github.cshadd.fetch_bot.FetchBotException;

// Main

/**
 * The Class CommunicationException. A Fetch Bot Exception specific to
 * Communication.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public class CommunicationException extends FetchBotException {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    // Public Constructors
    
    /**
     * Instantiates a new Communication Exception.
     */
    public CommunicationException() {
    }
    
    /**
     * Instantiates a new Communication Exception with message.
     *
     * @param message
     *            the message
     */
    public CommunicationException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new Communication Exception with message and cause.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Instantiates a new Communication Exception with cause.
     *
     * @param cause
     *            the cause
     */
    public CommunicationException(Throwable cause) {
        super(cause);
    }
}