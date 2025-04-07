package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ModelHSV extends Model {
    private final double[] hsvPixel;

    public ModelHSV() {
        super(new Image("model-hsv.png"), Model.BORDER_360, Model.BORDER_100, Model.BORDER_100);
        hsvPixel = new double[3];
    }

    @Override
    public String getFirstCoordinateName() {
        return "H";
    }

    @Override
    public String getSecondCoordinateName() {
        return "S";
    }

    @Override
    public String getThirdCoordinateName() {
        return "V";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HSV);
        int center = slice.cols() / 2;
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                if (x <= center) {
                    hsvPixel[0] = getFirstCoordinate() / 2;
                    hsvPixel[1] = MAX_VAL - (double) (x * 2) / slice.cols() * MAX_VAL;
                } else {
                    hsvPixel[0] = getFirstCoordinate() / 2 + HALF_HALF_FI;
                    hsvPixel[1] = (double) ((x - center) * 2) / slice.cols() * MAX_VAL;
                }
                hsvPixel[2] = (double) (slice.rows() - y) / slice.rows() * MAX_VAL;
                slice.put(y, x, hsvPixel);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HSV2BGR);
        return slice;
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HSV);
        int cols = (int) (slice.cols() * getSecondCoordinate() / Model.BORDER_100);
        slice.setTo(new Scalar(0, 0, 0));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < cols; x++) {
                slice.put(y, slice.cols() / 2 - cols / 2 + x,
                        (double) x / cols * HALF_FI,
                        getSecondCoordinate() / Model.BORDER_100 * Model.BORDER_255,
                        MAX_VAL - (double) y / slice.rows() * MAX_VAL);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HSV2BGR);
        return slice;
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HSV);
        double R = Math.floor((double) slice.cols() / 2);
        slice.setTo(new Scalar(0, 0, 0));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                double X = x - R;
                double Y = y - R;
                double r = Math.sqrt(X * X + Y * Y);
                long angle = Math.round(Math.atan2(Y, X) * HALF_FI / Math.PI) / 2 + HALF_HALF_FI;
                if (r <= R) {
                    hsvPixel[0] = angle;
                    hsvPixel[1] = r / R * MAX_VAL;
                    hsvPixel[2] = getThirdCoordinate() / Model.BORDER_100 * Model.BORDER_255;
                    slice.put(x, y, hsvPixel);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HSV2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель HSB - это перцепционная модель описания цвета, основанная на трех компонентах:\n" +
                "Hue - это оттенок цвета (0 - 360°);\n" +
                "Saturation - это степень насыщенности цвета (0 - 100%);\n" +
                "Brightness - это интенсивность света (0 - 100%).\n" +
                "Часто используется в графических редакторах и системах управления цветами";
    }
}
