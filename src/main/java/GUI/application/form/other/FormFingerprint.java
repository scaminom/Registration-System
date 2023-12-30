/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.application.form.other;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

/**
 *
 * @author patri
 */
public class FormFingerprint extends javax.swing.JPanel {

    UserDao ud = null;

    //the width of fingerprint image
    int fpWidth = 0;
    //the height of fingerprint image
    int fpHeight = 0;
    //for verify test
    private byte[] lastRegTemp = new byte[2048];
    //the length of lastRegTemp
    private int cbRegTemp = 0;
    //pre-register template
    private byte[][] regtemparray = new byte[3][2048];
    //Register
    private boolean bRegister = false;
    //Identify
    private boolean bIdentify = true;
    //finger id
    private int iFid = 1;

    private int nFakeFunOn = 1;
    //must be 3
    static final int enroll_cnt = 3;
    //the index of pre-register function
    private int enroll_idx = 0;

    private byte[] imgbuf = null;
    private byte[] template = new byte[2048];
    private int[] templateLen = new int[1];

    private boolean mbStop = true;
    private long mhDevice = 0;
    private long mhDB = 0;
    private WorkThread workThread = null;

    public FormFingerprint() {
        initComponents();
        ud = new UserDao();
        chargeDataCombobox();
    }

    private void chargeDataCombobox() {
        DefaultComboBoxModel<User> model = new DefaultComboBoxModel<>();
        try {
            var users = ud.getUsers();
            for (User user : users) {
                model.addElement(user);
            }
        } catch (Exception e) {
        }
        jComboBox1.setModel(model);

    }

    private class WorkThread extends Thread {

        @Override
        public void run() {
            super.run();
            int ret = 0;
            while (!mbStop) {
                System.out.println(mbStop);
                templateLen[0] = 2048;
                if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen))) {
                    if (nFakeFunOn == 1) {
                        byte[] paramValue = new byte[4];
                        int[] size = new int[1];
                        size[0] = 4;
                        int nFakeStatus = 0;
                        //GetFakeStatus
                        ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
                        nFakeStatus = byteArrayToInt(paramValue);
                        System.out.println("ret = " + ret + ",nFakeStatus=" + nFakeStatus);
                        if (0 == ret && (byte) (nFakeStatus & 31) != 31) {
                            jtxtVer.setText("Is a fake-finer?");
                            return;
                        }
                    }
                    OnCatpureOK(imgbuf);
                    OnExtractOK(template, templateLen[0]);
                    
                    System.out.println(template);
                    System.out.println(templateLen);
                    
                    String strBase64 = FingerprintSensorEx.BlobToBase64(template, templateLen[0]);
                    System.out.println("strBase64=" + strBase64);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        private void runOnUiThread(Runnable runnable) {
            // TODO Auto-generated method stub

        }
    }

    private void FreeSensor() {
        mbStop = true;
        try {		//wait for thread stopping
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
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
        int bfType = 0x424d; // 浣嶅浘鏂囦欢绫诲瀷锛�0鈥�1瀛楄妭锛�
        int bfSize = 54 + 1024 + w * nHeight;// bmp鏂囦欢鐨勫ぇ灏忥紙2鈥�5瀛楄妭锛�
        int bfReserved1 = 0;// 浣嶅浘鏂囦欢淇濈暀瀛楋紝蹇呴』涓�0锛�6-7瀛楄妭锛�
        int bfReserved2 = 0;// 浣嶅浘鏂囦欢淇濈暀瀛楋紝蹇呴』涓�0锛�8-9瀛楄妭锛�
        int bfOffBits = 54 + 1024;// 鏂囦欢澶村紑濮嬪埌浣嶅浘瀹為檯鏁版嵁涔嬮棿鐨勫瓧鑺傜殑鍋忕Щ閲忥紙10-13瀛楄妭锛�

        dos.writeShort(bfType); // 杈撳叆浣嶅浘鏂囦欢绫诲瀷'BM'
        dos.write(changeByte(bfSize), 0, 4); // 杈撳叆浣嶅浘鏂囦欢澶у皬
        dos.write(changeByte(bfReserved1), 0, 2);// 杈撳叆浣嶅浘鏂囦欢淇濈暀瀛�
        dos.write(changeByte(bfReserved2), 0, 2);// 杈撳叆浣嶅浘鏂囦欢淇濈暀瀛�
        dos.write(changeByte(bfOffBits), 0, 4);// 杈撳叆浣嶅浘鏂囦欢鍋忕Щ閲�

        int biSize = 40;// 淇℃伅澶存墍闇�鐨勫瓧鑺傛暟锛�14-17瀛楄妭锛�
        int biWidth = nWidth;// 浣嶅浘鐨勫锛�18-21瀛楄妭锛�
        int biHeight = nHeight;// 浣嶅浘鐨勯珮锛�22-25瀛楄妭锛�
        int biPlanes = 1; // 鐩爣璁惧鐨勭骇鍒紝蹇呴』鏄�1锛�26-27瀛楄妭锛�
        int biBitcount = 8;// 姣忎釜鍍忕礌鎵�闇�鐨勪綅鏁帮紙28-29瀛楄妭锛夛紝蹇呴』鏄�1浣嶏紙鍙岃壊锛夈��4浣嶏紙16鑹诧級銆�8浣嶏紙256鑹诧級鎴栬��24浣嶏紙鐪熷僵鑹诧級涔嬩竴銆�
        int biCompression = 0;// 浣嶅浘鍘嬬缉绫诲瀷锛屽繀椤绘槸0锛堜笉鍘嬬缉锛夛紙30-33瀛楄妭锛夈��1锛圔I_RLEB鍘嬬缉绫诲瀷锛夋垨2锛圔I_RLE4鍘嬬缉绫诲瀷锛変箣涓�銆�
        int biSizeImage = w * nHeight;// 瀹為檯浣嶅浘鍥惧儚鐨勫ぇ灏忥紝鍗虫暣涓疄闄呯粯鍒剁殑鍥惧儚澶у皬锛�34-37瀛楄妭锛�
        int biXPelsPerMeter = 0;// 浣嶅浘姘村钩鍒嗚鲸鐜囷紝姣忕背鍍忕礌鏁帮紙38-41瀛楄妭锛夎繖涓暟鏄郴缁熼粯璁ゅ��
        int biYPelsPerMeter = 0;// 浣嶅浘鍨傜洿鍒嗚鲸鐜囷紝姣忕背鍍忕礌鏁帮紙42-45瀛楄妭锛夎繖涓暟鏄郴缁熼粯璁ゅ��
        int biClrUsed = 0;// 浣嶅浘瀹為檯浣跨敤鐨勯鑹茶〃涓殑棰滆壊鏁帮紙46-49瀛楄妭锛夛紝濡傛灉涓�0鐨勮瘽锛岃鏄庡叏閮ㄤ娇鐢ㄤ簡
        int biClrImportant = 0;// 浣嶅浘鏄剧ず杩囩▼涓噸瑕佺殑棰滆壊鏁�(50-53瀛楄妭)锛屽鏋滀负0鐨勮瘽锛岃鏄庡叏閮ㄩ噸瑕�

        dos.write(changeByte(biSize), 0, 4);// 杈撳叆淇℃伅澶存暟鎹殑鎬诲瓧鑺傛暟
        dos.write(changeByte(biWidth), 0, 4);// 杈撳叆浣嶅浘鐨勫
        dos.write(changeByte(biHeight), 0, 4);// 杈撳叆浣嶅浘鐨勯珮
        dos.write(changeByte(biPlanes), 0, 2);// 杈撳叆浣嶅浘鐨勭洰鏍囪澶囩骇鍒�
        dos.write(changeByte(biBitcount), 0, 2);// 杈撳叆姣忎釜鍍忕礌鍗犳嵁鐨勫瓧鑺傛暟
        dos.write(changeByte(biCompression), 0, 4);// 杈撳叆浣嶅浘鐨勫帇缂╃被鍨�
        dos.write(changeByte(biSizeImage), 0, 4);// 杈撳叆浣嶅浘鐨勫疄闄呭ぇ灏�
        dos.write(changeByte(biXPelsPerMeter), 0, 4);// 杈撳叆浣嶅浘鐨勬按骞冲垎杈ㄧ巼
        dos.write(changeByte(biYPelsPerMeter), 0, 4);// 杈撳叆浣嶅浘鐨勫瀭鐩村垎杈ㄧ巼
        dos.write(changeByte(biClrUsed), 0, 4);// 杈撳叆浣嶅浘浣跨敤鐨勬�婚鑹叉暟
        dos.write(changeByte(biClrImportant), 0, 4);// 杈撳叆浣嶅浘浣跨敤杩囩▼涓噸瑕佺殑棰滆壊鏁�

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
        // "&" 涓庯紙AND锛夛紝瀵逛袱涓暣鍨嬫搷浣滄暟涓搴斾綅鎵ц甯冨皵浠ｆ暟锛屼袱涓綅閮戒负1鏃惰緭鍑�1锛屽惁鍒�0銆�  
        abyte[0] = (byte) (0xff & number);
        // ">>"鍙崇Щ浣嶏紝鑻ヤ负姝ｆ暟鍒欓珮浣嶈ˉ0锛岃嫢涓鸿礋鏁板垯楂樹綅琛�1  
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        // "|="鎸変綅鎴栬祴鍊笺��  
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    private void OnCatpureOK(byte[] imgBuf) {
        try {
            writeBitmap(imgBuf, fpWidth, fpHeight, "fingerprint.bmp");
            jbtnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void OnExtractOK(byte[] template, int len) {
        if (bRegister) {
            int[] fid = new int[1];
            int[] score = new int[1];
            int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
            if (ret == 0) {
                jtxtVer.setText("the finger already enroll by " + fid[0] + ",cancel enroll");
                bRegister = false;
                enroll_idx = 0;
                return;
            }
            if (enroll_idx > 0 && FingerprintSensorEx.DBMatch(mhDB, regtemparray[enroll_idx - 1], template) <= 0) {
                jtxtVer.setText("please press the same finger 3 times for the enrollment");
                return;
            }
            System.arraycopy(template, 0, regtemparray[enroll_idx], 0, 2048);
            enroll_idx++;
            if (enroll_idx == 3) {
                int[] _retLen = new int[1];
                _retLen[0] = 2048;
                byte[] regTemp = new byte[_retLen[0]];

                if (0 == (ret = FingerprintSensorEx.DBMerge(mhDB, regtemparray[0], regtemparray[1], regtemparray[2], regTemp, _retLen))
                        && 0 == (ret = FingerprintSensorEx.DBAdd(mhDB, iFid, regTemp))) {
                    iFid++;
                    cbRegTemp = _retLen[0];
                    System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);
                    String strBase64 = FingerprintSensorEx.BlobToBase64(regTemp, cbRegTemp);
                    //Base64 Template
                    jtxtVer.setText("enroll succ");
                } else {
                    jtxtVer.setText("enroll fail, error code=" + ret);
                }
                bRegister = false;
            } else {
                jtxtVer.setText("You need to press the " + (3 - enroll_idx) + " times fingerprint");
            }
        } else {
            if (bIdentify) {
                int[] fid = new int[1];
                int[] score = new int[1];
                int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
                if (ret == 0) {
                    jtxtVer.setText("Identify succ, fid=" + fid[0] + ",score=" + score[0]);
                } else {
                    jtxtVer.setText("Identify fail, errcode=" + ret);
                }

            } else {
                if (cbRegTemp <= 0) {
                    jtxtVer.setText("Please register first!");
                } else {
                    int ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, template);
                    if (ret > 0) {
                        jtxtVer.setText("Verify succ, score=" + ret);
                    } else {
                        jtxtVer.setText("Verify fail, ret=" + ret);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnImg = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtVer = new javax.swing.JTextArea();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Huella");
        jLabel1.setToolTipText("");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Seleccione el empleado:");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Iniciar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jtxtVer.setColumns(20);
        jtxtVer.setRows(5);
        jScrollPane1.setViewportView(jtxtVer);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 17, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(105, 105, 105))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 393, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jComboBox1.getAccessibleContext().setAccessibleParent(jComboBox1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jbtnImg, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        var user = (User) jComboBox1.getSelectedItem();

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO Auto-generated method stub
        if (0 != mhDevice) {
            //already inited
            jtxtVer.setText("Please close device first!");
            return;
        }
        int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
        //Initialize
        cbRegTemp = 0;
        bRegister = false;
        bIdentify = false;
        iFid = 1;
        enroll_idx = 0;
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init()) {
            jtxtVer.setText("Init failed!");
            return;
        }
        ret = FingerprintSensorEx.GetDeviceCount();
        if (ret < 0) {
            jtxtVer.setText("No devices connected!");
            FreeSensor();
            return;
        }
        if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0))) {
            jtxtVer.setText("Open device fail, ret = " + ret + "!");
            FreeSensor();
            return;
        }
        if (0 == (mhDB = FingerprintSensorEx.DBInit())) {
            jtxtVer.setText("Init DB fail, ret = " + ret + "!");
            FreeSensor();
            return;
        }

        //set fakefun off
        //FingerprintSensorEx.SetParameter(mhDevice, 2002, changeByte(nFakeFunOn), 4);
        byte[] paramValue = new byte[4];
        int[] size = new int[1];
        //GetFakeOn
        //size[0] = 4;
        //FingerprintSensorEx.GetParameters(mhDevice, 2002, paramValue, size);
        //nFakeFunOn = byteArrayToInt(paramValue);

        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
        fpWidth = byteArrayToInt(paramValue);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
        fpHeight = byteArrayToInt(paramValue);
        //width = fingerprintSensor.getImageWidth();
        //height = fingerprintSensor.getImageHeight();
        imgbuf = new byte[fpWidth * fpHeight];
        jbtnImg.resize(fpWidth, fpHeight);
        mbStop = false;
        workThread = new WorkThread();
        workThread.start();// 绾跨▼鍚姩
        jtxtVer.setText("Open succ!");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<User> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnImg;
    private javax.swing.JTextArea jtxtVer;
    // End of variables declaration//GEN-END:variables
}
