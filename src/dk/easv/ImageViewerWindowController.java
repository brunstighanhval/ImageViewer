package dk.easv;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController implements Initializable
{
    private final List<Image> images = new ArrayList<>();
    public Button btnStartSlideshow;
    public Slider sldSlideShow;
    public Label stiLabel;
    private int currentImageIndex = 0;
    private Task<Void> task;
    private Thread th;
    private boolean b;

    private List<File> files;
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
         files = fileChooser.showOpenMultipleDialog(new Stage());

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
    public void handleStartsSlideShow(ActionEvent actionEvent) {
        if(th != null && b) {
            th.interrupt();
            b = false;
        } else {
            task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    while (true) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                if (count == images.size()) {
                                    count = 0;
                                }



                                imageView.setImage(images.get(count));
                                stiLabel.setText(files.get(count).getName());

                                count++;

                            }
                        });
                        Thread.sleep((long) (sldSlideShow.getValue() * 1000));
                    }
                }
            };
            th = new Thread(task);
            th.start();
            b = true;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sldSlideShow.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // You can add code here to update the delay time based on the slider value
            }
        });
    }
}
