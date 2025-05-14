package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.BinaryOperator;

public class ModelXYZ extends Model {

    public ModelXYZ() {
        super(new Image("model-xyz.png"), Model.BORDER_255, Model.BORDER_255, Model.BORDER_255);
    }

    @Override
    public String getFirstCoordinateName() {
        return "X";
    }

    @Override
    public String getSecondCoordinateName() {
        return "Y";
    }

    @Override
    public String getThirdCoordinateName() {
        return "Z";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2XYZ);
        return fillSliceWith((i, j) -> slice.put(i, j,
                getFirstCoordinate(),
                MAX_VAL - (double) i / slice.cols() * MAX_VAL,
                (double) j / slice.rows() * MAX_VAL), slice);
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2XYZ);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - (double) i / slice.rows() * MAX_VAL,
                getSecondCoordinate(),
                (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2XYZ);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - (double) i / slice.cols() * MAX_VAL,
                (double) j / slice.rows() * MAX_VAL,
                getThirdCoordinate()), slice);
    }

    private Mat fillSliceWith(BinaryOperator<Integer> operator, Mat slice) {
        for (int i = 0; i < slice.cols() - 1; i++) {
            for (int j = 1; j < slice.rows(); j++) {
                operator.apply(i, j);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_XYZ2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель XYZ - это аддитивная цветовая модель, которая описывает цвета\n" +
                "с точки зрения физики света. Она является фундаментальной моделью,\n" +
                "лежащей в основе многих других цветовых пространств:\n" +
                "X - отвечает за длинноволновую часть спектра (красный оттенок, 0 - 255);\n" +
                "Y - отвечает за средневолновую часть спектра (зелёный оттенок, 0 - 255);\n" +
                "Z - отвечает за коротковолновую часть спектра (синий оттенок, 0 - 255).";
    }
}
