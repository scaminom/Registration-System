/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scrum.registrationsystem.biometrics;

import com.scrum.registrationsystem.entities.User;

/**
 *
 * @author patri
 */
public interface FingerprintCallback {
    void onFingerprintCaptured(byte[] imgBuf);
    void onFingerprintSuccess(String message);
    void onFingerprintError(String errorMessage);
    User onUserIdentify(User user);
}
