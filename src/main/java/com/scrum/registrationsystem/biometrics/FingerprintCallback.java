
package com.scrum.registrationsystem.biometrics;

import com.scrum.registrationsystem.entities.User;


public interface FingerprintCallback {
    void onFingerprintCaptured(byte[] imgBuf);
    void onFingerprintSuccess(String message);
    void onFingerprintError(String errorMessage);
    User onUserIdentify(User user);
}
