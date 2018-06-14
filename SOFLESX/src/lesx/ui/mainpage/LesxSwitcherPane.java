package lesx.ui.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lesx.gui.message.LesxMessage;
import lesx.scene.controller.LesxController;
import lesx.ui.soflesx.LesxMain;

public class LesxSwitcherPane {

  private final static Logger LOGGER = Logger.getLogger(LesxMessage.class.getName());
  public final static String CLIENTES = "/lesx/ui/mainpage/resources/LesxResourcesPaneFXML.fxml";
  public final static String AÑO = "";
  public final static String CAPP = "";
  public final static String COMISION_MES = "";
  public final static String COMISION_AÑO = "";

  private static LesxMainPageController controller;

  public static void setMainController(LesxMainPageController controller) {
    LesxSwitcherPane.controller = controller;
  }

  /**
   * Loads a new Pane for LesxMainPageController.
   */
  public static void loadPane(String path) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      Node root = fxmlLoader.load(LesxMain.getInstance()
          .getClass()
          .getResource(path)
          .openStream());
      LesxController controllerPane = (LesxController) fxmlLoader.getController();
      controller.showProgressProperty()
          .bind(controllerPane.showProgressProperty());
      controller.setMainPane(root);
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SWITCHING_PANE"), e);
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SWITCHING_PANE"), ex);
      ex.printStackTrace();
    }
  }

}
