package controller;

import converter.Converter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import model.impl.*;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

import static org.opencv.imgproc.Imgproc.resize;

public class Controller implements Initializable {
    @FXML
    public HBox paneModel;
    @FXML
    public Label valuesLabel;
    @FXML
    public MenuItem menuItemRGB;
    @FXML
    public MenuItem menuItemHSV;
    @FXML
    public MenuItem menuItemHSL;
    @FXML
    public ImageView modelImageView;
    @FXML
    public Label firstCoordinate;
    @FXML
    public Label secondCoordinate;
    @FXML
    public Label thirdCoordinate;
    @FXML
    public Slider sliderFirstCoordinate;
    @FXML
    public Slider sliderSecondCoordinate;
    @FXML
    public Slider sliderThirdCoordinate;
    @FXML
    public ImageView sliceProjection;
    @FXML
    public MenuItem menuItemSaveAsFile;
    @FXML
    public VBox box;
    @FXML
    public Line separator;
    @FXML
    public MenuItem menuItemCMY;
    @FXML
    public Button loadImageButton;
    @FXML
    public VBox paneConverter;
    @FXML
    public ImageView convertingImageView;
    @FXML
    public MenuItem menuItemConverter;
    @FXML
    public Button clearTextFieldButton;
    @FXML
    public ImageView mouseView;
    @FXML
    public Label labelCMYK;
    @FXML
    public TextField textFieldCMYK;
    @FXML
    public Label labelHSV;
    @FXML
    public TextField textFieldHSV;
    @FXML
    public Label labelHSL;
    @FXML
    public TextField textFieldHSL;
    @FXML
    public Label labelLAB;
    @FXML
    public TextField textFieldLAB;
    @FXML
    public Label labelRGB;
    @FXML
    public TextField textFieldRGB;
    @FXML
    public Label labelYUV;
    @FXML
    public TextField textFieldYUV;
    @FXML
    public Label labelXYZ;
    @FXML
    public TextField textFieldXYZ;
    @FXML
    public Label labelCoordinatesXY;
    @FXML
    public MenuItem menuItemXYZ;
    @FXML
    public MenuItem menuItemLAB;
    @FXML
    public MenuItem menuItemYUV;
    @FXML
    public MenuBar menuBar;
    @FXML
    public Pane pane;
    @FXML
    public HBox HBoxImageView;
    @FXML
    public Label firstValueLabel;
    @FXML
    public Label secondValueLabel;
    @FXML
    public Label thirdValueLabel;
    @FXML
    public Label coordinateY;
    @FXML
    public Label coordinateX;
    @FXML
    public Label infoAboutModel;
    @FXML
    public ImageView imageViewDot;
    @FXML
    public ImageView imageViewDotConverter;

    private Model model;

    private static final int DEFAULT_IMAGE_SIZE = 300;
    private static final int DEFAULT_MOUSE_IMAGE_SIZE = 200;

    public Controller() {
        OpenCV.loadLocally();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int widthAndHeight = 500;
        Model.size = new Size(widthAndHeight, widthAndHeight);
        box.heightProperty().addListener((observable, oldValue, newValue) -> separator.setEndY(newValue.doubleValue()));
        pane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != 0 && oldValue.doubleValue() != 0) {
                if (convertingImageView.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        sliceProjection.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        modelImageView.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        mouseView.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_MOUSE_IMAGE_SIZE) {
                    convertingImageView.setFitHeight(newValue.doubleValue() / 3 * 2);
                    modelImageView.setFitHeight(modelImageView.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue());
                    sliceProjection.setFitHeight(sliceProjection.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue());
                    mouseView.setFitHeight(mouseView.getFitHeight() * newValue.doubleValue() / oldValue.doubleValue());
                } else {
                    convertingImageView.setFitHeight(DEFAULT_IMAGE_SIZE);
                    modelImageView.setFitHeight(DEFAULT_IMAGE_SIZE);
                    sliceProjection.setFitHeight(DEFAULT_IMAGE_SIZE);
                    mouseView.setFitHeight(DEFAULT_MOUSE_IMAGE_SIZE);
                }
            }
        });
        pane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != 0 && oldValue.doubleValue() != 0) {
                if (convertingImageView.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        sliceProjection.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        modelImageView.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_IMAGE_SIZE ||
                        mouseView.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue() >= DEFAULT_MOUSE_IMAGE_SIZE) {
                    convertingImageView.setFitWidth(newValue.doubleValue() / 3 * 2);
                    modelImageView.setFitWidth(modelImageView.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    sliceProjection.setFitWidth(sliceProjection.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    mouseView.setFitWidth(mouseView.getFitWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldRGB.setPrefWidth(textFieldRGB.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldCMYK.setPrefWidth(textFieldCMYK.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldHSV.setPrefWidth(textFieldHSV.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldHSL.setPrefWidth(textFieldHSL.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldLAB.setPrefWidth(textFieldLAB.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldXYZ.setPrefWidth(textFieldXYZ.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                    textFieldYUV.setPrefWidth(textFieldYUV.getWidth() * newValue.doubleValue() / oldValue.doubleValue());
                } else {
                    convertingImageView.setFitWidth(DEFAULT_IMAGE_SIZE);
                    modelImageView.setFitWidth(DEFAULT_IMAGE_SIZE);
                    sliceProjection.setFitWidth(DEFAULT_IMAGE_SIZE);
                    mouseView.setFitWidth(DEFAULT_MOUSE_IMAGE_SIZE);
                }
            }
        });
        paneConverter.prefWidthProperty().bind(pane.widthProperty());
        paneConverter.prefHeightProperty().bind(pane.heightProperty());
        paneModel.prefHeightProperty().bind(pane.heightProperty());
        paneModel.prefWidthProperty().bind(pane.widthProperty());
        reset();
        menuItemSaveAsFile.setDisable(true);
        setDisableSliders(true);
    }

    @FXML
    public void onActionMenuItemRGB() {
        model = new ModelRGB();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemCMY() {
        model = new ModelCMY();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemHSV() {
        model = new ModelHSV();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemHSL() {
        model = new ModelHLS();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemXYZ() {
        model = new ModelXYZ();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemLAB() {
        model = new ModelLab();
        initializeModel(model);
    }

    @FXML
    public void onActionMenuItemYUV() {
        model = new ModelYUV();
        initializeModel(model);
    }

    /**
     * Метод, реализующий первоначальную инициализацию выбранной цветовой модели
     *
     * @param model цветовая модель
     */
    private void initializeModel(Model model) {
        String defaultFirstCoordinateName = "1";
        String defaultSecondCoordinateName = "2";
        initializeSliders(model);
        dropLabelValues();
        setCoordinatesNames(defaultFirstCoordinateName, defaultSecondCoordinateName);
        modelImageView.setImage(model.getModel());
        if (paneConverter.isVisible()) {
            paneConverter.setVisible(false);
        }
        if (menuItemSaveAsFile.isDisable()) {
            menuItemSaveAsFile.setDisable(false);
        }
        infoAboutModel.setText(model.getInfo());
        sliceProjection.setImage(null);
        paneModel.setVisible(true);
        imageViewDot.setImage(new Image("default-pixel.png"));
    }

    private void initializeSliders(Model model) {
        firstCoordinate.setText(model.getFirstCoordinateName());
        secondCoordinate.setText(model.getSecondCoordinateName());
        thirdCoordinate.setText(model.getThirdCoordinateName());
        sliderFirstCoordinate.setMax(model.getBorderMax1());
        sliderSecondCoordinate.setMax(model.getBorderMax2());
        sliderThirdCoordinate.setMax(model.getBorderMax3());
        sliderFirstCoordinate.setMin(model.getBorderMin1());
        sliderSecondCoordinate.setMin(model.getBorderMin2());
        sliderThirdCoordinate.setMin(model.getBorderMin3());
        sliderFirstCoordinate.setValue(Model.BORDER_0);
        sliderSecondCoordinate.setValue(Model.BORDER_0);
        sliderThirdCoordinate.setValue(Model.BORDER_0);
        setVisibleSliders();
        setDisableSliders(false);
    }

    @FXML
    public void onActionMenuItemConverter() {
        initializeConverter();
    }

    private void initializeConverter() {
        if (paneModel.isVisible()) {
            paneModel.setVisible(false);
        }
        if (!menuItemSaveAsFile.isDisable()) {
            menuItemSaveAsFile.setDisable(true);
        }
        imageViewDotConverter.setImage(new Image("default-pixel.png"));
        mouseView.setImage(Converter.generatePixelByColor(mouseView.getFitWidth(), mouseView.getFitHeight(),
                new double[]{0, 0, 0}));
        paneConverter.setVisible(true);
        model = null;
        textFieldRGB.setText("");
        textFieldCMYK.setText("");
        textFieldHSV.setText("");
        textFieldHSL.setText("");
        textFieldLAB.setText("");
        textFieldXYZ.setText("");
        textFieldYUV.setText("");
    }

    private void reset() {
        sliderFirstCoordinate.setValue(0);
        sliderSecondCoordinate.setValue(0);
        sliderThirdCoordinate.setValue(0);
        firstCoordinate.setText("1");
        secondCoordinate.setText("2");
        thirdCoordinate.setText("3");
        valuesLabel.setText("");
        menuItemSaveAsFile.setDisable(true);
        dropLabelValues();
    }

    private void setValuesLabel(String name, double value) {
        valuesLabel.setText(String.format("%s = %.0f", name, value));
    }

    @FXML
    public void onMouseReleasedSliderFirstCoordinate() {
        sliderSecondCoordinate.setValue(Model.BORDER_0);
        secondValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderSecondCoordinate.getMax()));
        sliderThirdCoordinate.setValue(Model.BORDER_0);
        thirdValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderThirdCoordinate.getMax()));
        model.setFirstCoordinate(Math.floor(sliderFirstCoordinate.getValue()));
        sliceProjection.setImage(Converter.mat2Img(model.getFirstProjection()));
        firstValueLabel.setText(String.format("%.0f/%.0f", sliderFirstCoordinate.getValue(), sliderFirstCoordinate.getMax()));
        setCoordinatesNames(model.getSecondCoordinateName(), model.getThirdCoordinateName());
        setValuesLabel(model.getFirstCoordinateName(), sliderFirstCoordinate.getValue());
    }

    @FXML
    public void onMouseReleasedSliderSecondCoordinate() {
        sliderFirstCoordinate.setValue(Model.BORDER_0);
        firstValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderFirstCoordinate.getMax()));
        sliderThirdCoordinate.setValue(Model.BORDER_0);
        thirdValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderThirdCoordinate.getMax()));
        model.setSecondCoordinate(Math.floor(sliderSecondCoordinate.getValue()));
        sliceProjection.setImage(Converter.mat2Img(model.getSecondProjection()));
        secondValueLabel.setText(String.format("%.0f/%.0f", sliderSecondCoordinate.getValue(), sliderSecondCoordinate.getMax()));
        setCoordinatesNames(model.getFirstCoordinateName(), model.getThirdCoordinateName());
        setValuesLabel(model.getSecondCoordinateName(), sliderSecondCoordinate.getValue());
    }

    @FXML
    public void onMouseReleasedSliderThirdCoordinate() {
        sliderFirstCoordinate.setValue(Model.BORDER_0);
        firstValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderFirstCoordinate.getMax()));
        sliderSecondCoordinate.setValue(Model.BORDER_0);
        secondValueLabel.setText(String.format("%.0f/%.0f", 0.f, sliderSecondCoordinate.getMax()));
        model.setThirdCoordinate(Math.floor(sliderThirdCoordinate.getValue()));
        sliceProjection.setImage(Converter.mat2Img(model.getThirdProjection()));
        thirdValueLabel.setText(String.format("%.0f/%.0f", sliderThirdCoordinate.getValue(), sliderThirdCoordinate.getMax()));
        setCoordinatesNames(model.getFirstCoordinateName(), model.getSecondCoordinateName());
        setValuesLabel(model.getThirdCoordinateName(), sliderThirdCoordinate.getValue());
    }

    @FXML
    public void onActionMenuItemSaveAsFile() {
        if (sliceProjection.getImage() != null && paneModel.isVisible()) {
            Mat resizeMat = new Mat();
            Size size = new Size(1000, 1000);
            resize(Converter.img2Mat(sliceProjection.getImage()), resizeMat, size, 0, 0, Imgproc.INTER_CUBIC);
            saveAsFile(sliceProjection.getImage());
        } else {
            createModalDialog("Сечение цветовой модели не выбрано", 250, 110);
        }
    }

    /**
     * Сохранение сечения цветовой модели на компьютер
     *
     * @param image сохраняемое изображение
     */
    private void saveAsFile(Image image) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить сечение");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileOutputStream stream = new FileOutputStream(file)) {
                MatOfByte bytes = new MatOfByte();
                Imgcodecs.imencode(".png", Converter.img2Mat(image), bytes);
                if (bytes.toArray().length != 0) {
                    stream.write(bytes.toArray());
                    stream.flush();
                    createModalDialog("Изображение успешно сохранено!", 250, 110);
                }
            } catch (IOException e) {
                createModalDialog(e.getMessage(), 500, 200);
            }
        }
    }

    private void setDisableSliders(boolean isDisable) {
        sliderFirstCoordinate.setDisable(isDisable);
        sliderSecondCoordinate.setDisable(isDisable);
        sliderThirdCoordinate.setDisable(isDisable);
    }

    private void setVisibleSliders() {
        sliderFirstCoordinate.setVisible(true);
        sliderSecondCoordinate.setVisible(true);
        sliderThirdCoordinate.setVisible(true);
    }

    @FXML
    public void onActionButtonLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка изображения");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ваше изображение",
                "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            int MAX_FILE_SIZE = 20000000;
            if (file.length() >= MAX_FILE_SIZE) {
                createModalDialog("Размер файла не должен превышать 20 мегабайт", 300, 100);
            } else {
                convertingImageView.setImage(new Image("file:///" + file.getPath()));
            }
        }
    }

    @FXML
    public void onActionClearTextFieldButton() {
        textFieldRGB.clear();
        textFieldCMYK.clear();
        textFieldHSL.clear();
        textFieldHSV.clear();
        textFieldYUV.clear();
        textFieldLAB.clear();
        textFieldXYZ.clear();
    }

    @FXML
    public void onMouseClickedGetPixels() {
        convertingImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                int x = (int) (event.getX() / convertingImageView.getBoundsInParent().getWidth()
                        * convertingImageView.getImage().getWidth());
                int y = (int) (event.getY() / convertingImageView.getBoundsInParent().getHeight() * convertingImageView.getImage().getHeight());
                double[] pixelsRGB = Converter.getRGBValueOfPixel(convertingImageView.getImage(), x, y);
                double[] normPixelsRGB = Converter.getRGBNormalValueOfPixel(convertingImageView.getImage(), x, y);
                double[] pixelsCMYK = Converter.getCMYK(normPixelsRGB);
                double[] pixelsHSL = Converter.getHSL(normPixelsRGB);
                double[] pixelsHSV = Converter.getHSV(normPixelsRGB);
                double[] pixelsYUV = Converter.getYUV(normPixelsRGB);
                double[] pixelsXYZ = Converter.getXYZ(normPixelsRGB);
                double[] pixelsLab = Converter.getLab(pixelsXYZ);
                textFieldRGB.setText(String.format("%.1f; %.1f; %.1f", pixelsRGB[0], pixelsRGB[1], pixelsRGB[2]));
                textFieldCMYK.setText(String.format("%.1f; %.1f; %.1f; %.1f", pixelsCMYK[0], pixelsCMYK[1], pixelsCMYK[2], pixelsCMYK[3]));
                textFieldHSL.setText(String.format("%.1f; %.1f; %.1f", pixelsHSL[0], pixelsHSL[1], pixelsHSL[2]));
                textFieldHSV.setText(String.format("%.1f; %.1f; %.1f", pixelsHSV[0], pixelsHSV[1], pixelsHSV[2]));
                textFieldYUV.setText(String.format("%.2f; %.2f; %.2f", pixelsYUV[0], pixelsYUV[1], pixelsYUV[2]));
                textFieldLAB.setText(String.format("%.1f; %.1f; %.1f", pixelsLab[0], pixelsLab[1], pixelsLab[2]));
                textFieldXYZ.setText(String.format("%.1f; %.1f; %.1f", pixelsXYZ[0], pixelsXYZ[1], pixelsXYZ[2]));
                imageViewDotConverter.setImage(Converter.generatePixelByColor(imageViewDotConverter.getFitWidth(), imageViewDotConverter.getFitHeight(), pixelsRGB));
            }
        });
    }

    @FXML
    public void onMouseMovedConvertingImageView() {
        convertingImageView.setOnMouseMoved(event -> {
            onMouseMovedConvertingImageViewMouseCoordinates(event);
            onMouseMovedConvertingImageViewZoom(event);
        });
    }

    public void onMouseMovedConvertingImageViewMouseCoordinates(MouseEvent event) {
        if (convertingImageView.getImage() != null) {
            labelCoordinatesXY.setText(String.format("X: %d; Y: %d",
                    (int) (event.getX() / convertingImageView.getBoundsInParent().getWidth()
                            * convertingImageView.getImage().getWidth()),
                    (int) (event.getY() / convertingImageView.getBoundsInParent().getHeight()
                            * convertingImageView.getImage().getHeight())));
        }
    }

    public void onMouseMovedConvertingImageViewZoom(MouseEvent event) {
        if (convertingImageView.getImage() != null) {
            int width = 300;
            int height = 300;
            int part = 150;
            Mat mat = new Mat(height, width, CvType.CV_8UC4);
            mat.put(0, 0, getBuffer(event, width, height, part));
            Imgproc.line(mat, new Point(part - 5, part), new Point(part + 5, part), new Scalar(0, 0, 255));
            Imgproc.line(mat, new Point(part, part - 5), new Point(part, part + 5), new Scalar(0, 0, 255));
            mouseView.setImage(Converter.mat2Img(mat));
        }
    }

    private byte[] getBuffer(MouseEvent event, int width, int height, int part) {
        PixelReader reader = convertingImageView.getImage().getPixelReader();
        byte[] buffer = new byte[width * height * 4];
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        int[] startPixels = getStartPixels(
                (int) (event.getX() / convertingImageView.getBoundsInParent().getWidth() * convertingImageView.getImage().getWidth()),
                (int) (event.getY() / convertingImageView.getBoundsInParent().getHeight() * convertingImageView.getImage().getHeight()), part,
                (int) convertingImageView.getImage().getWidth(),
                (int) convertingImageView.getImage().getHeight(),
                width, height);
        reader.getPixels(startPixels[0], startPixels[1], width, height, format, buffer, 0, width * 4);
        return buffer;
    }

    private void dropLabelValues() {
        firstValueLabel.setText("");
        secondValueLabel.setText("");
        thirdValueLabel.setText("");
    }

    /**
     * Поиск начальных пикселей, откуда будет получено изображение для лупы
     *
     * @param x                         координата x нажатия мыши
     * @param y                         координата y нажатия мыши
     * @param part                      отступ от края изображения
     * @param convertingImageViewWidth  ширина изображения
     * @param convertingImageViewHeight высота изображения
     * @param width                     ширина изображения лупы
     * @param height                    высота изображения лупы
     * @return смещенные координаты x и y для изображения лупы
     */
    private int[] getStartPixels(int x, int y, int part,
                                 int convertingImageViewWidth,
                                 int convertingImageViewHeight,
                                 int width, int height) {
        int xPixel = 0;
        int yPixel = 0;
        if (x >= part && y <= part && convertingImageViewWidth - x >= part) {
            xPixel = x - part;
        }
        if (convertingImageViewWidth - x < part && y < part) {
            xPixel = convertingImageViewWidth - width;
        }
        if (convertingImageViewWidth - x < part && y > part && convertingImageViewHeight - y >= part) {
            xPixel = convertingImageViewWidth - width;
            yPixel = y - part;
        }
        if (convertingImageViewWidth - x < part && convertingImageViewHeight - y < part) {
            xPixel = convertingImageViewWidth - width;
            yPixel = convertingImageViewHeight - height;
        }
        if (convertingImageViewHeight - y <= part && x >= part && x <= convertingImageViewWidth - part) {
            xPixel = x - part;
            yPixel = convertingImageViewHeight - height;
        }
        if (convertingImageViewHeight - y < part && x < part) {
            xPixel = 0;
            yPixel = convertingImageViewHeight - height;
        }
        if (x <= part && y >= part && convertingImageViewHeight - y > part) {
            xPixel = 0;
            yPixel = y - part;
        }
        if (x > part && y > part && convertingImageViewHeight - y > part && convertingImageViewWidth - x > part) {
            xPixel = x - part;
            yPixel = y - part;
        }
        return new int[]{xPixel, yPixel};
    }

    /**
     * Вывод диалогового окна с информационным сообщением
     *
     * @param message информационное сообщение
     * @param width   ширина окна
     * @param height  высота окна
     */
    private void createModalDialog(String message, double width, double height) {
        Stage dialog = new Stage();
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        Label label = new Label(message);
        label.setPadding(new javafx.geometry.Insets(5));
        VBox.setVgrow(label, Priority.ALWAYS);
        HBox.setHgrow(label, Priority.ALWAYS);
        root.getChildren().add(label);
        dialog.setTitle("Информация");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);
        dialog.getIcons().add(new Image("info-icon.png"));
        dialog.setScene(new Scene(root, width, height));
        dialog.showAndWait();
    }

    /**
     * Метод, задающий наименования координатным осям у сечения
     *
     * @param firstCoordinateName  Наименование верхней левой координаты
     * @param secondCoordinateName Наименование правой нижней координаты
     */
    private void setCoordinatesNames(String firstCoordinateName, String secondCoordinateName) {
        if (model instanceof ModelHSV || model instanceof ModelHLS) {
            coordinateX.setText(coordinateY.getText().substring(0, coordinateY.getText().length() - 1) + firstCoordinateName);
            coordinateY.setText(coordinateX.getText().substring(0, coordinateX.getText().length() - 1) + secondCoordinateName);
        } else {
            coordinateY.setText(coordinateY.getText().substring(0, coordinateY.getText().length() - 1) + firstCoordinateName);
            coordinateX.setText(coordinateX.getText().substring(0, coordinateX.getText().length() - 1) + secondCoordinateName);
        }
    }

    @FXML
    public void onMouseMovedSliceProjection() {
        if (sliceProjection != null && sliceProjection.getImage() != null) {
            sliceProjection.setOnMouseClicked(event -> {
                double[] color = Converter.getRGBValueOfPixel(sliceProjection.getImage(), (int) (event.getX() / sliceProjection.getBoundsInParent().getWidth()
                        * sliceProjection.getImage().getWidth()), (int) (event.getY() / sliceProjection.getBoundsInParent().getHeight()
                        * sliceProjection.getImage().getHeight()));
                imageViewDot.setImage(Converter.generatePixelByColor(imageViewDot.getFitWidth(), imageViewDot.getFitHeight(), color));
            });
        }
    }
}
