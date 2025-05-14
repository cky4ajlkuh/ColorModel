package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.BinaryOperator;

public class ModelCMY extends Model {

    public ModelCMY() {
        super(new Image("model-cmy.png"), Model.BORDER_100, Model.BORDER_100, Model.BORDER_100);
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
                (BORDER_100 - getFirstCoordinate()) / BORDER_100 * MAX_VAL,
                (double) i / slice.cols() * MAX_VAL,
                MAX_VAL - (double) j / slice.rows() * MAX_VAL), slice);
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                (double) i / slice.rows() * MAX_VAL,
                (BORDER_100 - getSecondCoordinate()) / BORDER_100 * MAX_VAL,
                MAX_VAL - (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                (double) i / slice.cols() * MAX_VAL,
                MAX_VAL - (double) j / slice.rows() * MAX_VAL,
                (BORDER_100 - getThirdCoordinate()) / BORDER_100 * MAX_VAL), slice);
    }

    private Mat fillSliceWith(BinaryOperator<Integer> operator, Mat slice) {
        for (int i = 0; i < slice.cols() - 1; i++) {
            for (int j = 1; j < slice.rows(); j++) {
                operator.apply(i, j);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_RGB2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель CMY - субтрактивную цветовую модель, основанную на цветовой модели CMYK,\n" +
                "используемую в цветной печати, а также для описания самого процесса печати.\n" +
                "Аббревиатура CMYK относится к четырем используемым чернильным пластинам:\n" +
                "C - голубой (0 - 100%);\n" +
                "M - пурпурный (0 - 100%);\n" +
                "Y - желтый (0 - 100%);\n" +
                "K - черный (0 - 100%).";
    }
}
