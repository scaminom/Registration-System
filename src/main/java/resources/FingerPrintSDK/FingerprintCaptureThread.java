package resources.FingerPrintSDK;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FingerprintCaptureThread implements Runnable {
   private FingerprintSensor fingerprintSensor = null;
   private FingerprintCaptureListener fingerprintCaptureListener = null;
   private boolean isCancel = false;
   private CountDownLatch countdownLatch = new CountDownLatch(1);

   public FingerprintCaptureThread(FingerprintSensor fingerprintSensor, int index) {
      this.fingerprintSensor = fingerprintSensor;
      this.fingerprintCaptureListener = (FingerprintCaptureListener)fingerprintSensor.getFingerprintCaptureListenerList().get("key.working.listener." + index);
   }

   @Override
   public void run() {
      while(!this.isCancel) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }

         byte[] fpImage = new byte[this.fingerprintSensor.getImageWidth() * this.fingerprintSensor.getImageHeight()];
         byte[] fpTemplate = new byte[2048];
         int ret = 0;
         int[] tempLen = new int[]{2048};
         ret = this.fingerprintSensor.capture(fpImage, fpTemplate, tempLen);
         if (ret < 0) {
            this.fingerprintCaptureListener.captureError(ret);
         } else {
            this.fingerprintSensor.setLastTempLen(tempLen[0]);
            this.fingerprintCaptureListener.captureOK(fpImage);
            this.fingerprintCaptureListener.extractOK(fpTemplate);
         }
      }

      this.countdownLatch.countDown();
   }

   public void cancel() {
      this.isCancel = true;

      try {
         this.countdownLatch.await(2L, TimeUnit.SECONDS);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }

   public boolean isCancel() {
      return this.isCancel();
   }
}