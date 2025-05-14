package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ModelLab extends Model {

    private final double[] labPixels;

    public ModelLab() {
        super(new Image("model-lab.png"),
                Model.BORDER_100, Model.BORDER_127, Model.BORDER_127,
                Model.BORDER_0, Model.BORDER_128, Model.BORDER_128);
        labPixels = new double[3];
    }

    @Override
    public String getFirstCoordinateName() {
        return "L";
    }

    @Override
    public String getSecondCoordinateName() {
        return "a";
    }

    @Override
    public String getThirdCoordinateName() {
        return "b";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2Lab);
        double R = Math.floor((double) slice.cols() / 2);
        slice.setTo(new Scalar(0, HALF_VAL, HALF_VAL));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                double X = x - R;
                double Y = y - R;
                double r = Math.sqrt(X * X + Y * Y);
                double coordinate = getFirstCoordinate() / Model.BORDER_100 * Model.BORDER_255;
                if (coordinate <= HALF_VAL && r <= coordinate / MAX_VAL * 2 * R ||
                        coordinate > HALF_VAL && r <= 2 * R - coordinate / MAX_VAL * 2 * R) {
                    labPixels[0] = coordinate;
                    labPixels[1] = (double) x / slice.cols() * MAX_VAL;
                    labPixels[2] = (double) y / slice.rows() * MAX_VAL;
                    slice.put(x, y, labPixels);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_Lab2BGR);
        return slice;
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2Lab);
        double R = Math.floor((double) slice.cols() / 2);
        slice.setTo(new Scalar(0, HALF_VAL, HALF_VAL));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                double X = x - R;
                double Y = y - R;
                double r = Math.sqrt(X * X + Y * Y);
                double coordinate = getSecondCoordinate() + (-1) * Model.BORDER_128;
                if (coordinate <= HALF_VAL && r <= coordinate / MAX_VAL * 2 * R ||
                        coordinate > HALF_VAL && r <= 2 * R - coordinate / MAX_VAL * 2 * R) {
                    labPixels[0] = MAX_VAL - (double) x / slice.cols() * MAX_VAL;
                    labPixels[1] = coordinate;
                    labPixels[2] = (double) y / slice.rows() * MAX_VAL;
                    slice.put(x, y, labPixels);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_Lab2BGR);
        return slice;
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2Lab);
        double R = Math.floor((double) slice.cols() / 2);
        slice.setTo(new Scalar(0, HALF_VAL, HALF_VAL));
        for (int y = 0; y < slice.rows(); y++) {
            for (int x = 0; x < slice.cols(); x++) {
                double X = x - R;
                double Y = y - R;
                double r = Math.sqrt(X * X + Y * Y);
                double coordinate = getThirdCoordinate() + (-1) * Model.BORDER_128;
                if (coordinate <= HALF_VAL && r <= coordinate / MAX_VAL * 2 * R ||
                        coordinate > HALF_VAL && r <= 2 * R - coordinate / MAX_VAL * 2 * R) {
                    labPixels[0] = MAX_VAL - (double) x / slice.cols() * MAX_VAL;
                    labPixels[1] = (double) y / slice.rows() * MAX_VAL;
                    labPixels[2] = coordinate;
                    slice.put(x, y, labPixels);
                }
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_Lab2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель Lab - это математическая модель, которая пытается более точно отражать человеческое\n" +
                "восприятие цвета по сравнению с другими цветовыми пространствами.\n" +
                "Состоит из следующих компонент:\n" +
                "L - светлота (0 - 100%);\n" +
                "a - красно-зеленый тон (-128 - +127);\n" +
                "b - желто-синий тон (-128 - +127).";
    }
}
