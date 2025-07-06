package ar.edu.uade.desa1.service;

import io.nayuki.qrcodegen.QrCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private static int IMAGE_SIZE = 300;

    @Override
    public String generateQRCodeAsBase64(Long routeId) {
        try {
            QrCode qrCode = QrCode.encodeText(String.valueOf(routeId), QrCode.Ecc.MEDIUM);
            BufferedImage image = toImage(qrCode, IMAGE_SIZE);
            return imageToBase64(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    private BufferedImage toImage(QrCode qrCode, int size) {
        int scale = size / qrCode.size;
        if (scale <= 0) scale = 1;

        int imgSize = qrCode.size * scale;
        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < qrCode.size; y++) {
            for (int x = 0; x < qrCode.size; x++) {
                int rgb = qrCode.getModule(x, y) ? 0x000000 : 0xFFFFFF;

                for (int sy = 0; sy < scale; sy++) {
                    for (int sx = 0; sx < scale; sx++) {
                        image.setRGB(x * scale + sx, y * scale + sy, rgb);
                    }
                }
            }
        }

        return image;
    }

    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
