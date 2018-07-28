package lesx.ui.soflesx;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import lesx.gui.message.LesxMessage;
import lesx.scene.controller.LesxController;
import lesx.ui.security.LesxSecurityTask;
import lesx.utils.LesxString;
import lesx.xml.thread.LesxXMLUtils;

public class LesxMainController extends LesxController {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLUtils.class.getName());

  @FXML
  Label header;
  @FXML
  Label loadText;
  @FXML
  ProgressIndicator progress;

  private BooleanProperty allLoaded = new SimpleBooleanProperty(this, "allLoaded");

  public LesxMainController() {
    //Nothing here
  }

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_READING_XML"));
    header.setText(LesxMessage.getMessage("TEXT-HEADER_READING_XML"));
    loadText.setText(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
    allLoaded.addListener((obs, oldV, newV) -> {
      if (newV) {
        LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-XML_LOADED_CORRECTLY"));
        LesxMain.getInstance()
            .activateScene(LesxString.MAINPAGE_PATH);
      }
    });
  }

  @Override
  protected void init() {
    File test = new File(LesxString.XML_RESOURCE_PATH);
    LesxSecurityTask task = new LesxSecurityTask(!test.exists());
    task.setOnSucceeded(obs -> {
      if (task.getValue()) {
        initializeFlesx();
      }
      else {
        initializeBait();
      }
    });
    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  private void initializeBait() {
    LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-XML_LOADED_CORRECTLY"));
    LesxMain.getInstance()
        .activateScene(LesxString.MAINPAGE_BAIT_PATH);
  }

  private void initializeFlesx() {
    try {
      LesxXMLUtils.importAllXMLFileToLesxProperty(() -> allLoaded.set(true));
    }
    catch (RuntimeException ex) {
      loadText.setText(LesxMessage.getMessage("TEXT-LOADING_ERROR_TITLE"));
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER_DATA"), ex);
      ex.printStackTrace();
    }
    catch (Exception e) {
      loadText.setText(LesxMessage.getMessage("TEXT-LOADING_ERROR_TITLE"));
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER_DATA"), e);
      e.printStackTrace();
    }
  }

}
