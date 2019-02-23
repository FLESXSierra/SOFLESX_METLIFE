package lesx.ui.soflesx;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lesx.db.LesxDBProperties;
import lesx.gui.message.LesxMessage;
import lesx.logger.LesxLogger;
import lesx.scene.controller.LesxSceneController;
import lesx.utils.LesxString;

public class LesxMain extends Application {

  private static Logger LOGGER;
  private static LesxMain instance;
  private static LesxDBProperties dbProperty;
  private static StringProperty title = new SimpleStringProperty("title");
  private static LesxSceneController sceneController;
  private Stage mainStage;

  public static void main(String[] args) {
    //Setting Logger
    LogManager.getLogManager()
        .reset();
    LOGGER = LogManager.getLogManager()
        .getLogger("");
    LOGGER.addHandler(new LesxLogger());
    LOGGER.log(Level.INFO, "Initializing");
    try {
      final FileHandler fileHandling = new FileHandler(LesxString.LOG_PATH);
      fileHandling.setFormatter(new SimpleFormatter());
      LOGGER.addHandler(fileHandling);
    }
    catch (SecurityException | IOException e) {
      e.printStackTrace();
    }
    //Create DataBase
    dbProperty = new LesxDBProperties();
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    try {
      mainStage = stage;
      Scene scene = new Scene(new Pane());
      //Add Scene
      sceneController = new LesxSceneController(scene);
      //Activate main scene
      activateScene(LesxString.MAIN_SCENE_PATH);
      stage.setTitle(title.get());
      stage.setMinHeight(250);
      stage.setMinWidth(200);
      stage.setScene(scene);
      stage.show();
      //Listener into title
      title.addListener(obs -> stage.setTitle(((StringProperty) obs).get()));
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_CLASS"), e);
    }
  }

  public LesxMain() {
    instance = this;
  }

  public static LesxMain getInstance() {
    return instance;
  }

  public Stage getStage() {
    return mainStage;
  }

  public static void setTitle(String newTitle) {
    title.set(newTitle);
  }

  public LesxDBProperties getDbProperty() {
    return dbProperty;
  }

  public void activateScene(String path) {
    try {
      sceneController.activate(path);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"), e);
    }
  }

}
