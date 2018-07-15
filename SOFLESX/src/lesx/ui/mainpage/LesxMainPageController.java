package lesx.ui.mainpage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.icon.utils.LesxIcon;
import lesx.property.properties.ELesxListenerType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResourceBusiness;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.LesxBirthdayButton;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxAlertBuilder;
import lesx.utils.LesxButtonType;
import lesx.utils.LesxString;

public class LesxMainPageController extends LesxController {
  @FXML
  MenuItem importXML;
  @FXML
  MenuItem exportXML;
  @FXML
  MenuItem saveFlesx;
  @FXML
  MenuItem main;
  @FXML
  MenuItem newSell;
  @FXML
  MenuItem resourcesItem;
  @FXML
  MenuItem year;
  @FXML
  MenuItem capp;
  @FXML
  MenuItem cMonth;
  @FXML
  MenuItem cYear2;
  @FXML
  Menu fileMenu;
  @FXML
  Menu bussiness;
  @FXML
  Menu comisions;
  @FXML
  MenuBar menu;
  @FXML
  LesxBirthdayButton bButton;
  @FXML
  AnchorPane mainPane;
  @FXML
  HBox progressBox;
  @FXML
  Label loading;

  private final static Logger LOGGER = Logger.getLogger(LesxMainPageController.class.getName());

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);

  public LesxMainPageController() {
    //Nothing
  }

  @FXML
  public void initialize() {
    //Sets all texts
    LesxSwitcherPane.setMainController(this);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE"));
    loading.setText(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
    importXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_IMPORT_SAVE"));
    exportXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_EXPORT_SAVE"));
    saveFlesx.setText(LesxMessage.getMessage("TEXT-MENUITEM_FILE_SAVE"));
    fileMenu.setText(LesxMessage.getMessage("TEXT-MENUBAR_FILE"));
    resourcesItem.setText(LesxMessage.getMessage("TEXT-MENUITEM_EDIT_RESOURCES"));
    main.setText(LesxMessage.getMessage("TEXT-MENUITEM_MAIN"));
    newSell.setText(LesxMessage.getMessage("TEXT-MENUITEM_NEW_SELL"));
    year.setText(LesxMessage.getMessage("TEXT-MENUITEM_YEAR"));
    bussiness.setText(LesxMessage.getMessage("TEXT-MENUBAR_BUSSINESS"));
    capp.setText(LesxMessage.getMessage("TEXT-MENUITEM_CAPP"));
    cMonth.setText(LesxMessage.getMessage("TEXT-MENUITEM_MONTH"));
    cYear2.setText(LesxMessage.getMessage("TEXT-MENUITEM_YEAR_2"));
    comisions.setText(LesxMessage.getMessage("TEXT-MENUBAR_COMISIONS"));
  }

  @Override
  public void init() {
    LesxSwitcherPane.loadPane(LesxSwitcherPane.MAIN);
    setUpMenuButtons();
    progressBox.visibleProperty()
        .bind(showProgress);
  }

  /**
   * Adds Listeners and and set to read only if needed.
   */
  private void setUpMenuButtons() {
    capp.setDisable(true);
    cMonth.setDisable(true);
    cYear2.setDisable(true);
    importXML.setDisable(true);
    exportXML.setOnAction(obs -> exportXML());
    main.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.MAIN));
    resourcesItem.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.CLIENTES));
    newSell.setOnAction(obs -> addNewSell());
    LesxMain.getInstance()
        .getDbProperty()
        .setListener(ELesxListenerType.UPDATE, () -> updateBirthDayButtonNames());
    updateBirthDayButtonNames();
  }

  private void updateBirthDayButtonNames() {
    bButton.setNames(LesxMain.getInstance()
        .getDbProperty()
        .getBirthdayNames());
  }

  private void addNewSell() {
    LesxResourcesDataModel dataModelResource = new LesxResourcesDataModel();
    LesxBusinessResourceDataModel dataModel = new LesxBusinessResourceDataModel();
    //Load Data Base
    dataModelResource.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getResourceMap());
    dataModel.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap());
    ButtonType result = LesxAlertBuilder.create()
        .setType(AlertType.CONFIRMATION)
        .setTitle(LesxMessage.getMessage("TEXT-ALERT_TITLE_NEW_SALE"))
        .setHeaderText(LesxMessage.getMessage("TEXT-ALERT_HEADER_NEW_SALE"))
        .setOwner(LesxMain.getInstance()
            .getStage())
        .setGraphic(LesxIcon.getImage(LesxIcon.MONEY))
        .setButtons(LesxButtonType.NEW_RESOURCE, LesxButtonType.NO_NEW_RESOURCE, LesxButtonType.CANCEL)
        .showAndWait()
        .orElse(null);
    if (LesxButtonType.NEW_RESOURCE.equals(result)) {
      LesxSceneController.showResourceEditDialog(this, ELesxUseCase.ADD_ONLY, dataModelResource, () -> {
        if (dataModelResource.getComponentSelected() != null) {
          dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
          LesxSceneController.showBusinessEditDialog(this, ELesxUseCase.ADD, dataModel, () -> {
            //Triggers Listener to update
          });
        }
      });
    }
    else if (LesxButtonType.NO_NEW_RESOURCE.equals(result)) {
      LesxSceneController.showSelectResourceDialog(this, dataModelResource, () -> {
        if (dataModelResource.getComponentSelected() != null) {
          dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
          LesxSceneController.showBusinessEditDialog(this, ELesxUseCase.ADD, dataModel, () -> {
            //Triggers Listener to update
          });
        }
      });
    }
  }

  /**
   * Exports the XML stored in the SOFLESX
   */
  private void exportXML() {
    final FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xml)", "*.xml");
    fileChooser.setTitle("Save file");
    fileChooser.setSelectedExtensionFilter(extFilter);
    fileChooser.setInitialFileName(LesxString.XML_NAME_RESOURCE);
    final File dest = fileChooser.showSaveDialog(LesxMain.getInstance()
        .getStage());
    if (dest != null) {
      try {
        String path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_RESOURCE);
        File destination = new File(path);
        final File costumer = new File(LesxString.XML_RESOURCE_PATH);
        Files.copy(costumer.toPath(), destination.toPath());

      }
      catch (FileAlreadyExistsException e) {
        LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"));
        LesxAlertBuilder.create()
            .setType(AlertType.WARNING)
            .setHeaderText(LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"))
            .show();
      }
      catch (IOException ex) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-EXPORT_XMLS"), ex);
        ex.printStackTrace();
      }
    }
  }

  public void setMainPane(Node node) {
    AnchorPane.setBottomAnchor(node, 0.0);
    AnchorPane.setLeftAnchor(node, 0.0);
    AnchorPane.setRightAnchor(node, 0.0);
    AnchorPane.setTopAnchor(node, 0.0);
    mainPane.getChildren()
        .setAll(node);
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

}
