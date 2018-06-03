
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
import org.junit.Test;

import io.github.cshadd.fetch_bot.controllers.OpenCVController;
import io.github.cshadd.fetch_bot.controllers.OpenCVControllerException;
import io.github.cshadd.fetch_bot.controllers.OpenCVControllerImpl;
import io.github.cshadd.fetch_bot.io.CommunicationException;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunication;
import io.github.cshadd.fetch_bot.io.WebInterfaceCommunicationImpl;

// Main

/**
 * The Class AppTest. This is for testing.
 *
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public class AppTest {
    // Public Static Methods

    /**
     * Test.
     * @throws OpenCVControllerException
     * @throws CommunicationException
     * @throws InterruptedException
     */
    @Test
    @SuppressWarnings("static-method")
    public void test() throws OpenCVControllerException, CommunicationException, InterruptedException {
        /*WebInterfaceCommunication a = new WebInterfaceCommunicationImpl();
        OpenCVController b = new OpenCVControllerImpl();
        b.startCamera();
        io.github.cshadd.fetch_bot.Core.delayThread(2000);
        a.reset();
        System.out.println("Tested.");
        while (true) {
            a.setSourceValue("mode", "Idle");
            a.pushRobot();
            a.pushSource();
            io.github.cshadd.fetch_bot.Core.delayThread(1000);
            a.pullRobot();
        }*/
    }
}
