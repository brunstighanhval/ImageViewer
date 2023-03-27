package dk.easv;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ImageViewerWindowController implements Initializable
{
    private final List<Image> images = new ArrayList<>();
    public Button btnStartSlideshow;
    public Slider sldSlideShow;
    private int currentImageIndex = 0;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;
    private int count;

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
        }
    }
/*
    public void slideshow () {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                imageView.setImage(images.get(count));
                count++;
                if (count == images.size())
                    count = 0;
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

 */

    //then the working logic in my eventhandler
    Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            for (int i = 0; i < images.size(); i++) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImage(images.get(count));
                        count++;
                        if (count >= images.size()) {
                            count = 0;
                        }
                    }
                });

                Thread.sleep(5000);
            }
            return null;
        }
    };

    public void handleStartsSlideShow(ActionEvent actionEvent) {
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sldSlideShow.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
    }
}
