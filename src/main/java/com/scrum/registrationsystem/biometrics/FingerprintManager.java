/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scrum.registrationsystem.biometrics;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.FinesCalculator;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FingerprintManager {
    private static FingerprintManager instance;

    int fpWidth = 0;

    int fpHeight = 0;

    private byte[] lastRegTemp = new byte[2048];

    private int cbRegTemp = 0;

    private boolean bRegister = false;

    private boolean bIdentify = true;

    private int iFid = 1;

    private int nFakeFunOn = 1;

    static final int enroll_cnt = 3;

    private byte[] imgbuf = null;
    private byte[] template = new byte[2048];
    private int[] templateLen = new int[1];

    private boolean mbStop = true;
    private long mhDevice = 0;
    private long mhDB = 0;
    private WorkThread workThread = null;
    private List<FingerprintCallback> callbacks = new ArrayList<>();
    private FingerprintCallback callback = null;
    UserDao userDao = new UserDao();
    ArrayList<User> users = new ArrayList<>();
    private final FinesCalculator finesCalculator = new FinesCalculator();

    public FingerprintManager() {
        callback = MyFingerprintCallback.getInstance(null);
        open();
        saveUserFootprints();
    }

    public void open() {
        if (0 != mhDevice) {

            notifyFingerprintError("Primero cierre el dispositivo!");
            return;
        }
        int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;

        cbRegTemp = 0;
        bRegister = false;
        bIdentify = false;
        iFid = 1;
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init()) {

            notifyFingerprintError("Inicialización fallida!");
            return;
        }
        ret = FingerprintSensorEx.GetDeviceCount();
        if (ret < 0) {

            notifyFingerprintError("No se detectó el dispositivo!");
            return;
        }
        if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0))) {

            notifyFingerprintError("Error al abrir el dispositivo!");
            return;
        }
        if (0 == (mhDB = FingerprintSensorEx.DBInit())) {

            notifyFingerprintError("Error al inicializar la base de datos!");
            return;
        }

        byte[] paramValue = new byte[4];
        int[] size = new int[1];

        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
        fpWidth = byteArrayToInt(paramValue);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
        fpHeight = byteArrayToInt(paramValue);
        imgbuf = new byte[fpWidth * fpHeight];
        mbStop = false;
        workThread = new WorkThread();
        workThread.start();
        notifyFingerprintSuccess("Suc. abierta!");
    }

    public void enroll() {
        if (0 == mhDevice) {
            notifyFingerprintError("Por favor abra el dispositivo primero!");
            return;
        }
        if (bIdentify) {
            bIdentify = false;
        }
        if (!bRegister) {
            bRegister = true;
        }

    }

    public void verify() {
        if (0 == mhDevice) {
            notifyFingerprintError("Por favor abra el dispositivo primero!");
            return;
        }
        if (bRegister) {
            bRegister = false;
        }
        if (bIdentify) {
            bIdentify = false;
        }
    }

    public void identify() {
        if (0 == mhDevice) {
            notifyFingerprintError("Por favor abra el dispositivo primero!");
            return;
        }
        if (bRegister) {
            bRegister = false;
        }
        if (!bIdentify) {
            bIdentify = true;
        }
    }

    public void FreeSensor() {
        mbStop = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (0 != mhDB) {
            FingerprintSensorEx.DBFree(mhDB);
            mhDB = 0;
        }
        if (0 != mhDevice) {
            FingerprintSensorEx.CloseDevice(mhDevice);
            mhDevice = 0;
        }
        FingerprintSensorEx.Terminate();
    }

    public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,
            String path) throws IOException {
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        int w = (((nWidth + 3) / 4) * 4);
        int bfType = 0x424d;
        int bfSize = 54 + 1024 + w * nHeight;
        int bfReserved1 = 0;
        int bfReserved2 = 0;
        int bfOffBits = 54 + 1024;

        dos.writeShort(bfType);
        dos.write(changeByte(bfSize), 0, 4);
        dos.write(changeByte(bfReserved1), 0, 2);
        dos.write(changeByte(bfReserved2), 0, 2);
        dos.write(changeByte(bfOffBits), 0, 4);

        int biSize = 40;
        int biWidth = nWidth;
        int biHeight = nHeight;
        int biPlanes = 1;
        int biBitcount = 8;
        int biCompression = 0;
        int biSizeImage = w * nHeight;
        int biXPelsPerMeter = 0;
        int biYPelsPerMeter = 0;
        int biClrUsed = 0;
        int biClrImportant = 0;

        dos.write(changeByte(biSize), 0, 4);
        dos.write(changeByte(biWidth), 0, 4);
        dos.write(changeByte(biHeight), 0, 4);
        dos.write(changeByte(biPlanes), 0, 2);
        dos.write(changeByte(biBitcount), 0, 2);
        dos.write(changeByte(biCompression), 0, 4);
        dos.write(changeByte(biSizeImage), 0, 4);
        dos.write(changeByte(biXPelsPerMeter), 0, 4);
        dos.write(changeByte(biYPelsPerMeter), 0, 4);
        dos.write(changeByte(biClrUsed), 0, 4);
        dos.write(changeByte(biClrImportant), 0, 4);

        for (int i = 0; i < 256; i++) {
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(0);
        }

        byte[] filter = null;
        if (w > nWidth) {
            filter = new byte[w - nWidth];
        }

        for (int i = 0; i < nHeight; i++) {
            dos.write(imageBuf, (nHeight - 1 - i) * nWidth, nWidth);
            if (w > nWidth) {
                dos.write(filter, 0, w - nWidth);
            }
        }
        dos.flush();
        dos.close();
        fos.close();
    }

    public static byte[] changeByte(int data) {
        return intToByteArray(data);
    }

    public static byte[] intToByteArray(final int number) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) (0xff & number);
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    private class WorkThread extends Thread {

        @Override
        public void run() {
            super.run();
            int ret = 0;
            while (!mbStop) {
                templateLen[0] = 2048;
                finesCalculator.calculateAndStoreFines();
                if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen))) {
                    if (nFakeFunOn == 1) {
                        byte[] paramValue = new byte[4];
                        int[] size = new int[1];
                        size[0] = 4;
                        int nFakeStatus = 0;

                        ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
                        nFakeStatus = byteArrayToInt(paramValue);
                        System.out.println("ret = " + ret + ",nFakeStatus=" + nFakeStatus);
                        if (0 == ret && (byte) (nFakeStatus & 31) != 31) {
                            notifyFingerprintError("Huella no valida?");
                            return;
                        }
                    }
                    OnCatpureOK(imgbuf);
                    OnExtractOK(template, templateLen[0]);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void runOnUiThread(Runnable runnable) {
            // TODO Auto-generated method stub

        }
    }

    public void OnCatpureOK(byte[] imgBuf) {
        try {
            writeBitmap(imgBuf, fpWidth, fpHeight, "fingerprint.bmp");
            // btnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
            notifyFingerprintCaptured(imgBuf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnExtractOK(byte[] template, int len) {
        if (bRegister) {
            int[] fid = new int[1];
            int[] score = new int[1];
            int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
            if (ret == 0) {
                notifyFingerprintError("Ya huella ya esta registrada!");
                return;
            }
        } else if (bIdentify) {
            identifyUser();
        }
    }

    public User findUserById(Long userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public void identifyUser() {
        int[] fid = new int[1];
        int[] score = new int[1];
        int ret = FingerprintSensorEx.DBIdentify(mhDB, this.template, fid, score);
        if (ret == 0) {
            User user = findUserById((long) fid[0]);
            if (user != null) {
                notifyUser(user);
            } else {
                notifyFingerprintError("No se encontro el usuario");
            }
        } else {
            notifyFingerprintError("Fallo la idenficación, no se encontro el usuario");
        }
    }

    public void saveUserFootprints() {
        users = (ArrayList<User>) userDao.findAll();
        FingerprintSensorEx.DBClear(mhDB);
        for (User user : users) {
            byte[] fingerprintTemplate = user.getFingerprintPattern();
            if (fingerprintTemplate != null) {
                int ret = FingerprintSensorEx.DBAdd(mhDB, (int) ((long) user.getId()), fingerprintTemplate);
                System.out.println(ret);
                if (ret == 0) {
                    System.out.println("Huella del usuario " + user.getId() + " guardada con éxito.");
                } else {
                    System.out.println("Error al guardar la huella del usuario " + user.getId() + ": " + ret);
                }
            } else {
                System.out.println("El usuario " + user.getId() + " no tiene una huella registrada.");
            }
        }
    }

    public static FingerprintManager getInstance() {
        if (instance == null) {
            instance = new FingerprintManager();
        }
        return instance;
    }

    public void addFingerprintCallback(FingerprintCallback callback) {
        System.out.println("Agregando callback");
        this.callback = callback;
    }

    public void removeFingerprintCallback(FingerprintCallback callback) {
        callbacks.remove(callback);
    }

    private void notifyFingerprintCaptured(byte[] imgBuf) {
        this.callback.onFingerprintCaptured(imgBuf);
    }

    private void notifyFingerprintSuccess(String message) {
        this.callback.onFingerprintSuccess(message);
    }

    private void notifyFingerprintError(String message) {
        this.callback.onFingerprintError(message);
    }

    public void notifyUser(User user) {
        this.callback.onUserIdentify(user);
    }

    public int getFpWidth() {
        return fpWidth;
    }

    public void setFpWidth(int fpWidth) {
        this.fpWidth = fpWidth;
    }

    public int getFpHeight() {
        return fpHeight;
    }

    public void setFpHeight(int fpHeight) {
        this.fpHeight = fpHeight;
    }

    public byte[] getLastRegTemp() {
        return lastRegTemp;
    }

    public void setLastRegTemp(byte[] lastRegTemp) {
        this.lastRegTemp = lastRegTemp;
    }

    public int getCbRegTemp() {
        return cbRegTemp;
    }

    public void setCbRegTemp(int cbRegTemp) {
        this.cbRegTemp = cbRegTemp;
    }

    public boolean isbRegister() {
        return bRegister;
    }

    public void setbRegister(boolean bRegister) {
        this.bRegister = bRegister;
    }

    public boolean isbIdentify() {
        return bIdentify;
    }

    public void setbIdentify(boolean bIdentify) {
        this.bIdentify = bIdentify;
    }

    public int getiFid() {
        return iFid;
    }

    public void setiFid(int iFid) {
        this.iFid = iFid;
    }

    public int getnFakeFunOn() {
        return nFakeFunOn;
    }

    public void setnFakeFunOn(int nFakeFunOn) {
        this.nFakeFunOn = nFakeFunOn;
    }

    public static int getEnrollCnt() {
        return enroll_cnt;
    }

    public byte[] getImgbuf() {
        return imgbuf;
    }

    public void setImgbuf(byte[] imgbuf) {
        this.imgbuf = imgbuf;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    public int[] getTemplateLen() {
        return templateLen;
    }

    public void setTemplateLen(int[] templateLen) {
        this.templateLen = templateLen;
    }

    public boolean isMbStop() {
        return mbStop;
    }

    public void setMbStop(boolean mbStop) {
        this.mbStop = mbStop;
    }

    public long getMhDevice() {
        return mhDevice;
    }

    public void setMhDevice(long mhDevice) {
        this.mhDevice = mhDevice;
    }

    public long getMhDB() {
        return mhDB;
    }

    public void setMhDB(long mhDB) {
        this.mhDB = mhDB;
    }

    public WorkThread getWorkThread() {
        return workThread;
    }

    public void setWorkThread(WorkThread workThread) {
        this.workThread = workThread;
    }

}
