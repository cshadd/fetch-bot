package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.Component;

//Main
@Component("OpenCV")
public class OpenCVControllerImpl
extends AbstractOpenCVController {
    // Public Constructors
    public OpenCVControllerImpl()
    throws OpenCVControllerException {
        super();
    }

    // Public Methods (Overrided)
    @Override
    public void assignTrackClass(String trackClass) {
        this.trackClass = trackClass;
    }
    @Override
    public boolean isTrackClassFound() {
        return trackClassFound;
    }
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