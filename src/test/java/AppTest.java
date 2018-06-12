
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

import io.github.cshadd.fetch_bot.controllers.HudController;
import io.github.cshadd.fetch_bot.controllers.HudControllerException;
import io.github.cshadd.fetch_bot.controllers.HudControllerImpl;
import io.github.cshadd.fetch_bot.controllers.OpenCVController;
import io.github.cshadd.fetch_bot.controllers.OpenCVControllerException;
import io.github.cshadd.fetch_bot.controllers.OpenCVControllerImpl;

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
     */
    @Test
    @SuppressWarnings("static-method")
    public void test() throws OpenCVControllerException, HudControllerException {
        /*HudController b = new HudControllerImpl();
        OpenCVController a = new OpenCVControllerImpl(b);
        
        b.openHud();
        a.startCamera();
        
        b.updateStatus(a.status());
        
        io.github.cshadd.fetch_bot.Core.delayThread(15000);
        b.closeHud();
        a.stopCamera();
        
        System.out.println("Tested.");*/
    }
}
