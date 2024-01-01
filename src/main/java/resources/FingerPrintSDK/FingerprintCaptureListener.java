/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources.FingerPrintSDK;

public interface FingerprintCaptureListener {
   void captureOK(byte[] var1);

   void captureError(int var1);

   void extractOK(byte[] var1);
}
