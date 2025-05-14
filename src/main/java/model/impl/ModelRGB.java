package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.BinaryOperator;

public class ModelRGB extends Model {

    public ModelRGB() {
        super(new Image("model-rgb.png"), Model.BORDER_255, Model.BORDER_255, Model.BORDER_255);
    }

    @Override
    public String getFirstCoordinateName() {
        return "R";
    }

    @Override
    public String getSecondCoordinateName() {
        return "G";
    }

    @Override
    public String getThirdCoordinateName() {
        return "B";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                getFirstCoordinate(),
                MAX_VAL - (double) i / slice.cols() * MAX_VAL,
                (double) j / slice.rows() * MAX_VAL), slice);
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - (double) i / slice.rows() * MAX_VAL,
                getSecondCoordinate(),
                (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2RGB);
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
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_RGB2BGR);
        return slice;
    }

    @Override
    public String getInfo() {
        return "Цветовая модель RGB - аддитивная цветовая модель, описывающая способ кодирования цвета для\n" +
                "цветовоспроизведения с помощью трёх цветов, которые принято называть основными.\n" +
                "Выбор основных цветов обусловлен особенностями физиологии восприятия цвета сетчаткой\n" +
                "человеческого глаза. Состоит из следующих цветов:\n" +
                "R - Красный (0 - 255);\n" +
                "G - Зеленый (0 - 255);\n" +
                "B - Синий (0 - 255).\n";
    }
}
