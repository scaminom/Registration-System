package resources.FingerPrintSDK;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FingerprintSensor {
   private long handle = 0L;
   private int width = 0;
   private int height = 0;
   private int lastTempLen = 0;
   private boolean bopened = false;
   private boolean bstarted = false;
   private int index = -1;
   private String devSn = "";
   private int FakeFunOn = 1;
   protected String deviceTag = String.valueOf((new Random()).nextInt());
   public static final String KEY_CAPTURE_LISTENER_PREFIX = "key.working.listener.";
   private Map<String, FingerprintCaptureListener> fingerVeinCaptureListenerList = new HashMap();

   public FingerprintSensor() {
      ZKFPService.Initialize();
   }

   public void setLastTempLen(int len) {
      this.lastTempLen = len;
   }

   public int getLastTempLen() {
      return this.lastTempLen;
   }

   public int getImageWidth() {
      return this.width;
   }

   public int getImageHeight() {
      return this.height;
   }

   public String getDevSn() {
      return this.devSn;
   }

   public Map<String, FingerprintCaptureListener> getFingerprintCaptureListenerList() {
      return this.fingerVeinCaptureListenerList;
   }

   public void setFingerprintCaptureListener(FingerprintCaptureListener listener) {
      this.fingerVeinCaptureListenerList.put("key.working.listener." + this.index, listener);
   }

   public String getDeviceTag() {
      return this.deviceTag;
   }

   public boolean startCapture() {
      if (this.index >= 0 && !this.bstarted) {
         if (this.fingerVeinCaptureListenerList.size() > 0 && this.index < this.fingerVeinCaptureListenerList.size()) {
            FingerprintCaptureThreadPool.start(this, this.index);
            System.out.println("Start fingerprint capture thread " + this.index + " OK");
            this.bstarted = true;
            return true;
         } else {
            System.out.println("Start fingerprint capture thread failed!");
            return false;
         }
      } else {
         return false;
      }
   }

   public void destroy() {
      FingerprintCaptureThreadPool.destroy();
      if (this.bopened) {
         this.closeDevice();
      }

      ZKFPService.Finalize();
   }

   public void stopCapture() {
      if (this.bstarted) {
         if (this.fingerVeinCaptureListenerList.size() > 0 && this.index < this.fingerVeinCaptureListenerList.size()) {
            FingerprintCaptureThreadPool.cancel(this, this.index);
         } else {
            System.out.println("Stop fingerprint capture thread failed!");
         }

         this.bstarted = false;
      }
   }

   public int getDeviceCount() {
      return ZKFPService.GetDeviceCount();
   }

   public int openDevice(int index) {
      this.handle = ZKFPService.OpenDevice(index);
      if (this.handle == 0L) {
         return FingerprintSensorErrorCode.ERROR_OPEN_FAIL;
      } else {
         int[] retWidth = new int[1];
         int[] retHeight = new int[1];
         ZKFPService.GetCapParams(this.handle, retWidth, retHeight);
         this.width = retWidth[0];
         this.height = retHeight[0];
         byte[] value = new byte[64];
         int[] retLen = new int[]{64};
         ZKFPService.GetParameter(this.handle, 1103, value, retLen);
         this.devSn = new String(value);
         this.index = index;
         return FingerprintSensorErrorCode.ERROR_SUCCESS;
      }
   }

   public int closeDevice() {
      this.stopCapture();
      if (0L != this.handle) {
         ZKFPService.CloseDevice(this.handle);
         this.handle = 0L;
      }

      this.bopened = false;
      this.index = -1;
      return FingerprintSensorErrorCode.ERROR_SUCCESS;
   }

   public int capture(byte[] image, byte[] template, int[] templen) {
      return 0L == this.handle ? FingerprintSensorErrorCode.ERROR_NOT_OPENED : ZKFPService.AcquireTemplate(this.handle, image, template, templen);
   }

   public void setFakeFunOn(int FakeFunOn) {
      if (0L != this.handle) {
         this.FakeFunOn = FakeFunOn;
         byte[] value = new byte[]{(byte)(FakeFunOn & 255), (byte)((FakeFunOn & '\uff00') >> 8), (byte)((FakeFunOn & 16711680) >> 16), (byte)((FakeFunOn & -16777216) >> 24)};
         ZKFPService.SetParameter(this.handle, 2002, value, 4);
      }

   }

   public int getFakeFunOn() {
      return this.FakeFunOn;
   }

   public int getFakeStatus() {
      int status = -1;
      byte[] value = new byte[4];
      int[] retlen = new int[]{4};
      if (ZKFPService.GetParameter(this.handle, 2004, value, retlen) == 0) {
         status = value[0] & 255;
         status += value[1] << 8 & '\uff00';
         status += value[2] << 16 & 16711680;
         status += value[3] << 24 & -16777216;
      }

      return status;
   }

   public int GenRegFPTemplate(byte[] temp1, byte[] temp2, byte[] temp3, byte[] regTemp, int[] regTempLen) {
      return ZKFPService.GenRegFPTemplate(temp1, temp2, temp3, regTemp, regTempLen);
   }

   public int DBAdd(int fid, byte[] regTemplate) {
      return ZKFPService.DBAdd(fid, regTemplate);
   }

   public int DBDel(int fid) {
      return ZKFPService.DBDel(fid);
   }

   public int DBCount() {
      return ZKFPService.DBCount();
   }

   public int VerifyFPByID(int fid, byte[] template) {
      return ZKFPService.VerifyFPByID(fid, template);
   }

   public int MatchFP(byte[] temp1, byte[] temp2) {
      return ZKFPService.MatchFP(temp1, temp2);
   }

   public int IdentifyFP(byte[] template, int[] fid, int[] socre) {
      return ZKFPService.IdentifyFP(template, fid, socre);
   }

   public int ExtractFromImage(String filePath, int DPI, byte[] template, int[] size) {
      return ZKFPService.ExtractFromImage(filePath, DPI, template, size);
   }

   public int GetParameter(int code, byte[] value, int[] len) {
      return 0L == this.handle ? FingerprintSensorErrorCode.ERROR_NOT_OPENED : ZKFPService.GetParameter(this.handle, code, value, len);
   }

   public int SetParameter(int code, byte[] value, int len) {
      if (0L == this.handle) {
         return FingerprintSensorErrorCode.ERROR_NOT_OPENED;
      } else {
         int ret = ZKFPService.SetParameter(this.handle, code, value, len);
         if (ret == 0 && 2002 == code) {
            this.FakeFunOn = value[0] & 255;
            this.FakeFunOn += value[1] << 8 & '\uff00';
            this.FakeFunOn += value[2] << 16 & 16711680;
            this.FakeFunOn += value[3] << 24 & -16777216;
         }

         if (ret == 0 && 3 == code) {
            int[] retWidth = new int[1];
            int[] retHeight = new int[1];
            ZKFPService.GetCapParams(this.handle, retWidth, retHeight);
            this.width = retWidth[0];
            this.height = retHeight[0];
         }

         return ret;
      }
   }

   public static String BlobToBase64(byte[] buf, int cbBuf) {
      return ZKFPService.BlobToBase64(buf, cbBuf);
   }

   public static int Base64ToBlob(String strBase64, byte[] buf, int cbBuf) {
      return ZKFPService.Base64ToBlob(strBase64, buf, cbBuf);
   }
}

