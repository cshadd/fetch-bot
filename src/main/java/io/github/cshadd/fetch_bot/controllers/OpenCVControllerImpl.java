package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.Component;

//Main
@Component("OpenCV")
public class OpenCVControllerImpl
extends AbstractOpenCVController {
    // Public Constructors
    public OpenCVControllerImpl() {
        super();
    }

    // Public Methods (Overrided)
    @Override
    public void startCamera() {
        cameraThread.start();
    }
    @Override
    public void stopCamera()
    throws InterruptedException {
        if (cameraThread != null) {
            cameraRunnable.terminate();
            cameraThread.join();
        }
    }
}