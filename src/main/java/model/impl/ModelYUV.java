package model.impl;

import javafx.scene.image.Image;
import model.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.BinaryOperator;

public class ModelYUV extends Model {

    public ModelYUV(int border1, int border2, int border3) {
        super(new Image("model-yuv.png"), border1, border2, border3);
    }

    @Override
    public String getFirstCoordinateName() {
        return "Y";
    }

    @Override
    public String getSecondCoordinateName() {
        return "U";
    }

    @Override
    public String getThirdCoordinateName() {
        return "V";
    }

    @Override
    public Mat getFirstProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2YUV);
        return fillSliceWith((i, j) -> slice.put(i, j,
                getFirstCoordinate(),
                MAX_VAL - (double) i / slice.rows() * MAX_VAL,
                (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getSecondProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2YUV);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - (double) i / slice.rows() * MAX_VAL,
                getSecondCoordinate(),
                (double) j / slice.cols() * MAX_VAL), slice);
    }

    @Override
    public Mat getThirdProjection() {
        Mat slice = new Mat(size, CvType.CV_8UC3);
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_BGR2YUV);
        return fillSliceWith((i, j) -> slice.put(i, j,
                MAX_VAL - (double) i / slice.rows() * MAX_VAL,
                (double) j / slice.cols() * MAX_VAL,
                getThirdCoordinate()), slice);
    }

    private Mat fillSliceWith(BinaryOperator<Integer> operator, Mat slice) {
        for (int i = 0; i < slice.cols(); i++) {
            for (int j = 0; j < slice.rows(); j++) {
                operator.apply(i, j);
            }
        }
        Imgproc.cvtColor(slice, slice, Imgproc.COLOR_YUV2BGR);
        return slice;
    }


    @Override
    public String getInfo() {
        return "Цветовая модель YUV - это система представления цвета, состоящая из трех компонентов:\n" +
                "Y - яркость (Luma);\n" +
                "U и V - цветоразностные компоненты (Chroma).\n" +
                "YUV используется во всех системах передачи и трансляции видео, позволяя снизить объем передаваемых\n" +
                "данных, т.к. цветоразностные компоненты можно кодировать с меньшей точность.";
    }
}
