package lesx.ui.mainpage.main;

import static lesx.property.properties.ELesxUseCase.EDIT;

import java.text.NumberFormat;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.icon.utils.LesxIcon;
import lesx.property.properties.ELesxListenerType;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResourceBusiness;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.LesxTreeTableViewPane;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxAlertBuilder;
import lesx.utils.LesxButtonType;

public class LesxMainPaneController extends LesxController {

  private final static Logger LOGGER = Logger.getLogger(LesxMainPaneController.class.getName());

  @FXML
  LesxTreeTableViewPane<LesxResourceBusiness> mainPane;

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);
  //Components
  private TreeTableView<LesxResourceBusiness> table;
  //Columns
  private TreeTableColumn<LesxResourceBusiness, String> solicitud;
  private TreeTableColumn<LesxResourceBusiness, String> nameColumn;
  private TreeTableColumn<LesxResourceBusiness, String> cc;
  private TreeTableColumn<LesxResourceBusiness, String> tipo;
  private TreeTableColumn<LesxResourceBusiness, String> tipoVida;
  private TreeTableColumn<LesxResourceBusiness, String> tipoAP;
  private TreeTableColumn<LesxResourceBusiness, String> prima;
  private TreeTableColumn<LesxResourceBusiness, String> primaVida;
  private TreeTableColumn<LesxResourceBusiness, String> primaAP;
  private TreeTableColumn<LesxResourceBusiness, String> nbs;
  private TreeTableColumn<LesxResourceBusiness, String> month;
  //Data
  private LesxResourcesDataModel dataModelResource = new LesxResourcesDataModel();
  private LesxBusinessResourceDataModel dataModel = new LesxBusinessResourceDataModel();
  private IntegerProperty year = new SimpleIntegerProperty();
  //flag
  private boolean ignoreListener = false;
  //Runnables
  private Runnable onDelete;
  private Consumer<ELesxUseCase> onAdd;

  @FXML
  public void initialize() {
    showProgress.set(true);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE_MAIN"));
    table = mainPane.getTable();
    year.bind(mainPane.yearProperty());
    //Load Data Base
    dataModelResource.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getResourceMap());
    dataModel.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap());
    configurateColumns();
    installListeners();
    showProgress.set(false);
  }

  /**
   * Configures the Resources' Table
   */
  @SuppressWarnings("unchecked")
  private void configurateColumns() {
    solicitud = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_SOLICITATION"));
    solicitud.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, true) && param.getValue()
            .getValue()
            .getResource()
            .getSolicitud() != null
                ? param.getValue()
                    .getValue()
                    .getResource()
                    .getSolicitud()
                    .toString()
                : "");
      }
    });
    cc = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, true) && param.getValue()
            .getValue()
            .getResource()
            .getCc() != null
                ? param.getValue()
                    .getValue()
                    .getResource()
                    .getCc()
                    .toString()
                : "");
      }
    });
    nameColumn = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, true) && param.getValue()
            .getValue()
            .getResource()
            .getName() != null
                ? param.getValue()
                    .getValue()
                    .getResource()
                    .getName()
                : "");
      }
    });
    tipo = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_TYPE_PRODUCT"));
    tipoVida = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    tipoVida.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
            && param.getValue()
                .getValue()
                .getBusiness()
                .getProduct()
                .getTypeVida() != null
                    ? param.getValue()
                        .getValue()
                        .getBusiness()
                        .getProduct()
                        .getTypeVida()
                        .toString()
                    : "");
      }
    });
    tipoAP = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    tipoAP.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {

      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
            && param.getValue()
                .getValue()
                .getBusiness()
                .getProduct()
                .getTypeAP() != null
                    ? param.getValue()
                        .getValue()
                        .getBusiness()
                        .getProduct()
                        .getTypeAP()
                        .toString()
                    : "");
      }
    });
    tipo.getColumns()
        .addAll(tipoVida, tipoAP);
    prima = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_TYPE_PRIMA"));
    primaVida = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    primaVida.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
            && param.getValue()
                .getValue()
                .getBusiness()
                .getProduct()
                .getPrimaVida() != null
                    ? formatter.format(param.getValue()
                        .getValue()
                        .getBusiness()
                        .getProduct()
                        .getPrimaVida())
                    : "");
      }
    });
    primaAP = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    primaAP.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
            && param.getValue()
                .getValue()
                .getBusiness()
                .getProduct()
                .getPrimaAP() != null
                    ? formatter.format(param.getValue()
                        .getValue()
                        .getBusiness()
                        .getProduct()
                        .getPrimaAP())
                    : "");
      }
    });
    prima.getColumns()
        .addAll(primaVida, primaAP);
    nbs = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NBS"));
    nbs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getNbs() != null
                ? formatter.format(param.getValue()
                    .getValue()
                    .getBusiness()
                    .getNbs())
                : "");
      }
    });

    month = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_MONTH"));
    month.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(param != null && param.getValue() != null && param.getValue()
            .getValue() != null
            && param.getValue()
                .getValue()
                .getMonth() != null
                    ? param.getValue()
                        .getValue()
                        .getMonth()
                    : "");
      }
    });

    table.setShowRoot(false);
    table.getColumns()
        .setAll(month, solicitud, nameColumn, cc, tipo, prima, nbs);
    updateTreeTableData();
  }

  private boolean parameterNotNull(CellDataFeatures<LesxResourceBusiness, String> param, boolean resource) {
    final boolean result = (param != null && param.getValue() != null && param.getValue()
        .getValue() != null);
    if (resource) {
      return (result && param.getValue()
          .getValue()
          .getResource() != null);
    }
    return (result && param.getValue()
        .getValue()
        .getBusiness() != null);
  }

  private void updateTreeTableData() {
    showProgress.set(true);
    ignoreListener = true;
    final TreeItem<LesxResourceBusiness> root = new TreeItem<>();
    for (ELesxMonth month : ELesxMonth.values()) {
      root.getChildren()
          .add(dataModel.getTreeItem(month, year.getValue()));
    }
    table.setRoot(root);
    ignoreListener = false;
    showProgress.set(false);
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  private void installListeners() {
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> {
          if (!ignoreListener) {
            if (mainPane.isSelectedResourceBusinessItem()) {
              dataModel.setComponentSelected(table.getSelectionModel()
                  .selectedItemProperty()
                  .getValue()
                  .getValue());
            }
            else {
              dataModel.setComponentSelected(null);
            }
          }
        });
    year.addListener(obs -> updateTreeTableData());
    updateCache = () -> updateeDataFromCache();
    LesxMain.getInstance()
        .getDbProperty()
        .setListenerRB(ELesxListenerType.UPDATE, updateCache);
    createRunnables();
  }

  private void updateeDataFromCache() {
    showProgress.set(true);
    final LesxResourceBusiness temp = dataModel.getComponentSelected() != null ? dataModel.getComponentSelected()
        .clone() : null;
    Task<Void> load = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        //Load Data Base
        dataModelResource.setMap(LesxMain.getInstance()
            .getDbProperty()
            .getResourceMap());
        dataModel.setMap(LesxMain.getInstance()
            .getDbProperty()
            .getBusinessResourceMap());
        dataModel.setComponentSelected(temp);
        return null;
      }

      @Override
      protected void succeeded() {
        updateTreeTableData();
        mainPane.selectItem(temp);
        showProgress.set(false);
      }

      @Override
      protected void failed() {
        LOGGER.log(Level.SEVERE, getException().getLocalizedMessage(), getException());
        getException().printStackTrace();
        super.failed();
      }
    };
    Thread test = new Thread(load);
    test.start();
  }

  private void createRunnables() {
    onDelete = () -> {
      dataModel.deleteSelectedBusiness();
      pendingChangesProperty().set(true);
      updateTreeTableData();
    };
    mainPane.setOnDelete(onDelete);
    onAdd = (useCase) -> addNewBusiness(useCase, false);
    mainPane.setOnAddNewItem(onAdd);
  }

  private void addNewBusiness(ELesxUseCase useCase, boolean isCreated) {
    if (useCase != EDIT) {
      if (mainPane.isSelectedResourceBusinessItem() || isCreated) {
        final Runnable onSave = () -> {
          pendingChangesProperty().set(true);
          dataModelResource.setComponentSelected(null);
        };
        LesxSceneController.showBusinessEditDialog(this, useCase, dataModel, onSave, onSave);
      }
      else {
        ButtonType result = null;
        if (dataModelResource.getComponentSelected() == null) {
          result = LesxAlertBuilder.create()
              .setType(AlertType.CONFIRMATION)
              .setTitle(LesxMessage.getMessage("TEXT-ALERT_TITLE_NEW_SALE"))
              .setHeaderText(LesxMessage.getMessage("TEXT-ALERT_HEADER_NEW_SALE"))
              .setOwner(LesxMain.getInstance()
                  .getStage())
              .setGraphic(LesxIcon.getImage(LesxIcon.MONEY))
              .setButtons(LesxButtonType.NEW_RESOURCE, LesxButtonType.NO_NEW_RESOURCE, LesxButtonType.CANCEL)
              .showAndWait()
              .orElse(null);
        }
        else {
          result = LesxButtonType.NO_NEW_RESOURCE;
        }
        if (LesxButtonType.NEW_RESOURCE.equals(result)) {
          LesxSceneController.showResourceEditDialog(this, ELesxUseCase.ADD_ONLY, dataModelResource, () -> {
            if (dataModelResource.getComponentSelected() != null) {
              dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
              addNewBusiness(useCase, true);
              pendingChangesProperty().set(true);
            }
          });
        }
        else if (LesxButtonType.NO_NEW_RESOURCE.equals(result)) {
          LesxSceneController.showSelectResourceDialog(this, dataModelResource, () -> {
            if (dataModelResource.getComponentSelected() != null) {
              dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
              addNewBusiness(useCase, true);
            }
          });
        }
      }
    }
    else {
      LesxSceneController.showBusinessEditDialog(this, useCase, dataModel, () -> pendingChangesProperty().set(true));
    }
  }

  @Override
  public void clearComponent() {
    LesxMain.getInstance()
        .getDbProperty()
        .removeListenerRB(ELesxListenerType.UPDATE, updateCache);
  }

}
