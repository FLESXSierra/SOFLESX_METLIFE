package lesx.ui.mainpage.main;

import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.LesxResource;
import lesx.property.properties.LesxResourceBusiness;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTreeTableViewPane;
import lesx.ui.soflesx.LesxMain;

public class LesxMainPaneController extends LesxController {

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
  private TreeTableColumn<LesxResourceBusiness, String> comision;
  private TreeTableColumn<LesxResourceBusiness, String> month;
  //Data
  private Map<Long, LesxResource> dataResource;
  private LesxResourcesDataModel dataModelResource = new LesxResourcesDataModel();
  private LesxBusinessResourceDataModel dataModel = new LesxBusinessResourceDataModel();
  private IntegerProperty year = new SimpleIntegerProperty();

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
    dataResource = dataModelResource.getMap();
    dataModel.setResourceMap(dataResource);
    dataModel.setBusinessMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessMap());
    dataModel.buildResourceBusinessPairs();
    configurateColumns();
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
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getProduct()
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
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getProduct()
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
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getProduct()
                    .toString()
                : "");
      }
    });
    primaAP = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    primaAP.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getProduct() != null
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getProduct()
                    .toString()
                : "");
      }
    });
    prima.getColumns()
        .addAll(primaVida, primaAP);
    nbs = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NBS"));
    nbs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getNbs() != null
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getNbs()
                    .toString()
                : "");
      }
    });
    comision = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_COMISION"));
    comision.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxResourceBusiness, String> param) {
        return new SimpleStringProperty(parameterNotNull(param, false) && param.getValue()
            .getValue()
            .getBusiness()
            .getPrima() != null
                ? param.getValue()
                    .getValue()
                    .getBusiness()
                    .getPrima()
                    .toString()
                : ""); // TODO Calculate Comision
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

    month.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.1));
    solicitud.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.1));
    nameColumn.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.14));
    cc.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.1));
    tipoAP.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.07));
    tipoVida.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.07));
    tipo.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.14));
    primaAP.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.07));
    primaVida.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.07));
    prima.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.14));
    nbs.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.14));
    comision.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.14));

    table.setShowRoot(false);
    updateTreeTableData();
    table.getColumns()
        .setAll(month, solicitud, nameColumn, cc, tipo, prima, nbs, comision);
  }

  protected boolean parameterNotNull(CellDataFeatures<LesxResourceBusiness, String> param, boolean resource) {
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
    final TreeItem<LesxResourceBusiness> root = new TreeItem<>();
    for (ELesxMonth month : ELesxMonth.values()) {
      root.getChildren()
          .add(dataModel.getTreeItem(month, year.getValue()));
    }
    table.setRoot(root);
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  @Override
  protected boolean showAlert() {
    return false;
  }

}
