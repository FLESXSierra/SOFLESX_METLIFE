package lesx.scene.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lesx.gui.message.LesxMessage;
import lesx.ui.soflesx.LesxMain;

public class LesxSceneController {

  private final static Logger LOGGER = Logger.getLogger(LesxSceneController.class.getName());

  private Scene main;

  public LesxSceneController(Scene scene) {
    main = scene;
  }

  public void activate(String path) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      Pane root = fxmlLoader.load(getClass().getResource(path)
          .openStream());
      LesxController controller = (LesxController) fxmlLoader.getController();
      LesxMain.setTitle(controller.getTitle());
      controller.setExitOperation(LesxMain.getInstance()
          .getStage());
      main.setRoot(root);
      if (main.getWindow() != null) {
        main.getWindow()
            .sizeToScene();
      }
      LesxMain.getInstance()
          .getStage()
          .centerOnScreen();
      controller.init();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

}
