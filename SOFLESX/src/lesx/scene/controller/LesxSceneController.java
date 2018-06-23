package lesx.scene.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.ui.components.dialogs.LesxEditResourceDialog;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxString;

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

  public static void showResourceEditDialog(LesxController controller, boolean isCreate, LesxResourcesDataModel dataModel, Runnable runnable) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
          .getResource(LesxString.EDIT_COMPONENT_DIALOG_PATH));
      Stage stage = new Stage();
      LesxEditResourceDialog controllerResource = new LesxEditResourceDialog();
      fxmlLoader.setController(controllerResource);
      Pane root = fxmlLoader.load();
      controllerResource.init(dataModel, isCreate);
      controllerResource.setWindow(stage);
      stage.setTitle(controllerResource.getTitle());
      stage.setScene(new Scene(root));
      stage.initOwner(LesxMain.getInstance()
          .getStage());
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setMinHeight(520);
      stage.setMinWidth(465);
      stage.sizeToScene();
      stage.show();
      controllerResource.afterSaveProperty()
          .addListener(obs -> runnable.run());
      controllerResource.closeProperty()
          .addListener((obs, oldV, newV) -> {
            if (newV) {
              runnable.run();
              stage.close();
            }
          });
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
