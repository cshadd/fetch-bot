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
package io.github.cshadd.fetch_bot.io.json;

import io.github.cshadd.fetch_bot.io.Communication;

// Main

/**
 * The Interface JSONCommunication. Defines what a JSON Communication does.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 2.0.0-alpha
 */
public abstract interface JSONCommunication extends Communication {
    // Public Abstract Methods
    
    /**
     * Clear.
     *
     * @throws JSONCommunicationException
     *             if the clear request failed
     */
    public abstract void clear() throws JSONCommunicationException;
    
    /**
     * Gets the robot value.
     *
     * @param key
     *            the key
     * @return the robot value
     * @throws JSONCommunicationException
     *             if the value could not be read
     */
    public abstract String getRobotValue(String key)
                    throws JSONCommunicationException;
    
    /**
     * Gets the source value.
     *
     * @param key
     *            the key
     * @return the source value
     * @throws JSONCommunicationException
     *             if the value could not be read
     */
    public abstract String getSourceValue(String key)
                    throws JSONCommunicationException;
    
    /**
     * Pull robot.
     *
     * @throws JSONCommunicationException
     *             if the pull request failed
     */
    public abstract void pullRobot() throws JSONCommunicationException;
    
    /**
     * Pull source.
     *
     * @throws JSONCommunicationException
     *             if the pull request failed
     */
    public abstract void pullSource() throws JSONCommunicationException;
    
    /**
     * Push robot.
     *
     * @throws JSONCommunicationException
     *             if the push request failed
     */
    public abstract void pushRobot() throws JSONCommunicationException;
    
    /**
     * Push source.
     *
     * @throws JSONCommunicationException
     *             if the push request failed
     */
    public abstract void pushSource() throws JSONCommunicationException;
    
    /**
     * Reset.
     *
     * @throws JSONCommunicationException
     *             if the reset request failed
     */
    public abstract void reset() throws JSONCommunicationException;
    
    /**
     * Sets the robot value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @throws JSONCommunicationException
     *             if the value could not be set
     */
    public abstract void setRobotValue(String key, String value)
                    throws JSONCommunicationException;
    
    /**
     * Sets the source value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @throws JSONCommunicationException
     *             if the value could not be set
     */
    public abstract void setSourceValue(String key, String value)
                    throws JSONCommunicationException;
}