package converter;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import nu.pattern.OpenCV;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;
import java.nio.ByteBuffer;

public class Converter {
    private final static double MAX = 255.0d;
    private final static double TO_DEGREE = 360.d;
    private final static double TO_PERCENT = 100.d;
    private final static double[][] MATRIX = {
            {0.4124, 0.3576, 0.1805},
            {0.2126, 0.7152, 0.0722},
            {0.0193, 0.1192, 0.9505}
    };

    Converter() {
        OpenCV.loadShared();
    }

    public static Image bytes2Img(byte[] bytes, int width, int height) {
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = (x * 4) + (y * width * 4);
                Color color = Color.rgb(
                        (bytes[index] & 0xff),
                        (bytes[index + 1] & 0xff),
                        (bytes[index + 2] & 0xff)
                );
                pixelWriter.setColor(x, y, color);
            }
        }
        return writableImage;
    }

    public static byte[] img2Bytes(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        return buffer;
    }

    public static Image mat2Img(Mat mat) {
        MatOfByte bytes = new MatOfByte();
        Imgcodecs.imencode(".png", mat, bytes);
        return new Image(new ByteArrayInputStream(bytes.toArray()));
    }

    public static Mat img2Mat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }

    public static double[] getHSL(double[] pixels) {
        double r = pixels[0];
        double g = pixels[1];
        double b = pixels[2];
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double l = (max + min) / 2;
        double h = 0;
        double s = 0;
        if (max == min) {
            return new double[]{h, s, l * TO_PERCENT};
        } else {
            double d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else if (max == b) {
                h = (r - g) / d + 4;
            }
            h /= 6;
            return new double[]{h * TO_DEGREE, s * TO_PERCENT, l * TO_PERCENT};
        }
    }

    public static double[] getHSV(double[] pixels) {
        double r = pixels[0];
        double g = pixels[1];
        double b = pixels[2];
        double min = Math.min(Math.min(r, g), b);
        double max = Math.max(Math.max(r, g), b);
        double h = 0;
        double d = max - min;
        double s = max == 0 ? 0 : d / max;
        if (max == min) {
            h = 0;
        } else if (max == r) {
            h = (g - b) / d + (g < b ? 6 : 0);
        } else if (max == g) {
            h = (b - r) / d + 2;
        } else if (max == b) {
            h = (r - g) / d + 4;
        }
        h /= 6;
        return new double[]{h * TO_DEGREE, s * TO_PERCENT, max * TO_PERCENT};
    }

    public static double[] getYUV(double[] pixels) {
        // BT.601
        double Kr = 0.299;
        double Kb = 0.114;
        double r = pixels[0];
        double g = pixels[1];
        double b = pixels[2];
        double y = Kr * r + (1 - Kr - Kb) * g + Kb * b;
        double u = b - y;
        double v = r - y;
        return new double[]{y, u, v};
    }

    public static double[] getCMYK(double[] pixels) {
        double r = pixels[0];
        double g = pixels[1];
        double b = pixels[2];

        double k = 1 - Math.max(Math.max(r, g), b);
        double c = (1 - r - k) / (1 - k);
        double m = (1 - g - k) / (1 - k);
        double y = (1 - b - k) / (1 - k);
        return new double[]{c * TO_PERCENT, m * TO_PERCENT, y * TO_PERCENT, k * TO_PERCENT};
    }

    public static double[] getXYZ(double[] pixels) {
        double r = pixels[0];
        double g = pixels[1];
        double b = pixels[2];
        r = gamma(r) * 100;
        g = gamma(g) * 100;
        b = gamma(b) * 100;
        double x = MATRIX[0][0] * r + MATRIX[0][1] * g + MATRIX[0][2] * b;
        double y = MATRIX[1][0] * r + MATRIX[1][1] * g + MATRIX[1][2] * b;
        double z = MATRIX[2][0] * r + MATRIX[2][1] * g + MATRIX[2][2] * b;
        return new double[]{x, y, z};
    }

    public static double[] getLab(double[] pixels) {
        // https://www.mathworks.com/help/images/ref/whitepoint.html
        double Xr = 95.047;
        double Yr = 100;
        double Zr = 108.883;
        double x = pixels[0] / Xr;
        double y = pixels[1] / Yr;
        double z = pixels[2] / Zr;
        double l = 116 * f(x) - 16;
        double a = 500 * (f(x) - f(y));
        double b = 200 * (f(y) - f(z));
        return new double[]{l, a, b};
    }

    private static double gamma(double value) {
        if (value > 0.04045) {
            return Math.pow((value + 0.055) / 1.055, 2.4);
        } else {
            return value / 12.92;
        }
    }

    private static double f(double value) {
        if (value > 0.008856) {
            return Math.pow(value, 1.d / 3.d);
        } else {
            return (1.d / 3.d) * Math.pow(29.d / 6.d, 2) * value + 4.d / 29.d;
        }
    }

    public static double[] getRGBValueOfPixel(Image image, int x, int y) {
        Color color = image.getPixelReader().getColor(x, y);
        return new double[]{color.getRed() * MAX, color.getGreen() * MAX, color.getBlue() * MAX};
    }

    public static double[] getRGBNormalValueOfPixel(Image image, int x, int y) {
        Color color = image.getPixelReader().getColor(x, y);
        return new double[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public static Image generatePixelByColor(double width, double height, double[] color) {
        Mat mat = new Mat(new Size(width, height), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mat.put(j, i, color);
            }
        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);
        return mat2Img(mat);
    }
}
