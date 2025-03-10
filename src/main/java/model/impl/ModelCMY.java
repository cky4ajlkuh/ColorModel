package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.BinaryOperator;

public class ModelCMY extends Model {

    public ModelCMY(int border1, int border2, int border3) {
        super(new Image("model-cmy.png"), border1, border2, border3);
    }

    @Override
    public String getFirstCoordinateName() {
        return "C";
    }

    @Override
    public String getSecondCoordinateName() {
        return "M";
    }

    @Override
    public String getThirdCoordinateName() {
        return "Y";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - getFirstCoordinate(),
                (double) i / slice.cols() * MAX_VAL,
                MAX_VAL - (double) j / slice.rows() * MAX_VAL), slice);
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                (double) i / slice.rows() * MAX_VAL,
                MAX_VAL - getSecondCoordinate(),
                MAX_VAL - (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                (double) i / slice.cols() * MAX_VAL,
                MAX_VAL - (double) j / slice.rows() * MAX_VAL,
                MAX_VAL - getThirdCoordinate()), slice);
    }

    private Mat fillSliceWith(BinaryOperator<Integer> operator, Mat slice) {
        for (int i = 0; i < slice.cols(); i++) {
            for (int j = 0; j < slice.rows(); j++) {
                operator.apply(i, j);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_RGB2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель CMY - субтрактивную цветовую модель, основанную на цветовой модели CMYK, используемую\n" +
                "в цветной печати, а также для описания самого процесса печати. Аббревиатура CMYK относится к четырем\n" +
                "используемым чернильным пластинам:\n" +
                "C - голубой (0 - 255);\n" +
                "M - пурпурный (0 - 255);\n" +
                "Y - желтый (0 - 255);\n" +
                "K - черный (0 - 100%).";
    }
}
