package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ModelHLS extends Model {
    private final double[] hlsPixel;
    private final int[][] mask;

    public ModelHLS() {
        super(new Image("model-hsl.png"),
                Model.BORDER_360, Model.BORDER_100, Model.BORDER_100);
        hlsPixel = new double[3];
        mask = getMask();
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
        return "L";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HLS);
        int up = slice.rows() / 2;
        int part = slice.cols() / 2;
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                if (y > part - x && y <= up && x <= part) {
                    hlsPixel[0] = getFirstCoordinate() / 2;
                    hlsPixel[2] = MAX_VAL - (double) (x * 2) / slice.cols() * MAX_VAL;
                    hlsPixel[1] = (double) (slice.rows() - y) / slice.rows() * MAX_VAL;
                } else if (y + up > x && y <= up && x > part) {
                    hlsPixel[0] = getFirstCoordinate() / 2 + HALF_HALF_FI;
                    hlsPixel[2] = (double) ((x - part) * 2) / slice.cols() * MAX_VAL;
                    hlsPixel[1] = (double) (slice.rows() - y) / slice.rows() * MAX_VAL;
                } else if (x < part * 2 - y + up && y > up && x > part) {
                    hlsPixel[0] = getFirstCoordinate() / 2 + HALF_HALF_FI;
                    hlsPixel[2] = (double) ((x - part) * 2) / slice.cols() * MAX_VAL;
                    hlsPixel[1] = (double) (slice.rows() - y) / slice.rows() * MAX_VAL;
                } else if (y - up < x && y > up && x <= part) {
                    hlsPixel[0] = getFirstCoordinate() / 2;
                    hlsPixel[2] = MAX_VAL - (double) (x * 2) / slice.cols() * MAX_VAL;
                    hlsPixel[1] = (double) (slice.rows() - y) / slice.rows() * MAX_VAL;
                } else {
                    hlsPixel[0] = 0;
                    hlsPixel[2] = 0;
                    hlsPixel[1] = 0;
                }
                slice.put(y, x, hlsPixel);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HLS2BGR);
        return slice;
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HLS);
        int cols = (int) (slice.cols() * getSecondCoordinate() / Model.BORDER_100);
        int border = (int) (getSecondCoordinate() / Model.BORDER_100 * slice.rows() / 2);
        int end = slice.rows() - border;
        if (slice.rows() / 2 - border == 0) {
            end = border + 1;
        }
        slice.setTo(new Scalar(0, 0, 0));
        for (int y = border; y < end; y++) {
            for (int x = 0; x < cols; x++) {
                if (mask[y][slice.cols() / 2 - cols / 2 + x] == 1) {
                    slice.put(y, slice.cols() / 2 - cols / 2 + x,
                            (double) x / cols * HALF_FI,
                            MAX_VAL - (double) y / slice.rows() * MAX_VAL,
                            getSecondCoordinate() / Model.BORDER_100 * Model.BORDER_255);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HLS2BGR);
        return slice;
    }

    private int[][] getMask() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HLS);
        int up = slice.rows() / 2 + 1;
        int part = slice.cols() / 2 + 1;
        int[][] mask = new int[slice.rows()][slice.cols()];
        for (int y = 0; y < mask.length; y++) {
            for (int x = 0; x < mask[0].length; x++) {
                if (y > part - x && y <= up && x <= part) {
                    mask[y][x] = 1;
                } else if (y + up > x && y <= up && x > part) {
                    mask[y][x] = 1;
                } else if (x < part * 2 - y + up && y > up && x > part) {
                    mask[y][x] = 1;
                } else if (y - up <= x && y > up && x <= part) {
                    mask[y][x] = 1;
                } else {
                    mask[y][x] = 0;
                }
            }
        }
        return mask;
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2HLS);
        double R = Math.floor((double) slice.cols() / 2);
        slice.setTo(new Scalar(0, 0, 0));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                double X = x - R;
                double Y = y - R;
                double r = Math.sqrt(X * X + Y * Y);
                long angle = Math.round(Math.atan2(Y, X) * HALF_FI / Math.PI) / 2 + HALF_HALF_FI;
                double coordinate = getThirdCoordinate() / Model.BORDER_100 * Model.BORDER_255;
                if (coordinate <= HALF_VAL && r <= coordinate / MAX_VAL * 2 * R ||
                        coordinate > HALF_VAL && r <= 2 * R - coordinate / MAX_VAL * 2 * R) {
                    hlsPixel[0] = angle;
                    hlsPixel[1] = coordinate;
                    hlsPixel[2] = r / R * MAX_VAL;
                    slice.put(x, y, hlsPixel);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_HLS2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель HSL представляет собой альтернативу RGB для описания цвета. Часто используется в\n" +
                "дизайне и графических редакторах, так как позволяет легко менять оттенок цвета, сохраняя его\n" +
                "яркость и насыщенность. Состоит из следующих цветов:\n" +
                "Hue - Определяет основной цвет (0 - 360°);\n" +
                "Saturation - Указывает на степень насыщенности цвета (0 - 100%);\n" +
                "Lightness - Описывает светлость или темноту цвета (0 - 100%).";
    }
}
