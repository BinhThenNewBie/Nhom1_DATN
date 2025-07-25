/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QR;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author ADMIN
 */
public class QRCodeGenerator {
    public static void showQRCode(String noiDung) {
        try {
            int width = 300;
            int height = 300;
            BitMatrix matrix = new MultiFormatWriter().encode(noiDung, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label, "Quét mã QR để thanh toán", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
