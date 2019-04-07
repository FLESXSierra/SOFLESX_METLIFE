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
  public static final String MAIN = "/lesx/ui/mainpage/main/LesxMainPaneFXML.fxml";
  public final static String CLIENTES = "/lesx/ui/mainpage/resources/LesxResourcesPaneFXML.fxml";
  public final static String CAPP = "/lesx/ui/mainpage/capp/LesxCAPPPaneFXML.fxml";
  public final static String COMISION_MES = "/lesx/ui/mainpage/comisionmes/LesxComisionMesPaneFXML.fxml";
  public final static String COMISION_AÑO = "";

  private static LesxMainPageController2 controller;
  private static String actualPage = "";
  private static LesxController mainPaneController;

  public static void setMainController(LesxMainPageController2 controller) {
    LesxSwitcherPane.controller = controller;
  }

  /**
   * Loads a new Pane for LesxMainPageController.
   */
  public static void loadPane(String path) {
    if (!actualPage.equals(path)) {
      actualPage = path;
      try {
        if (mainPaneController != null) {
          controller.pendingChangesProperty()
              .unbindBidirectional(mainPaneController.pendingChangesProperty());
          mainPaneController.clearComponent();
        }
        FXMLLoader fxmlLoader = new FXMLLoader();
        Node root = fxmlLoader.load(LesxMain.getInstance()
            .getClass()
            .getResource(path)
            .openStream());
        mainPaneController = (LesxController) fxmlLoader.getController();
        mainPaneController.pendingChangesProperty()
            .set(controller.pendingChangesProperty()
                .get());
        controller.pendingChangesProperty()
            .bindBidirectional(mainPaneController.pendingChangesProperty());
        controller.showProgressProperty()
            .bind(mainPaneController.showProgressProperty());
        LesxMain.setTitle(mainPaneController.getTitle());
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

}
