package lesx.ui.mainpage.main;

import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxResource;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTreeTableViewPane;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxPair;

public class LesxMainPaneController extends LesxController {

  @FXML
  Label yearLabel;
  @FXML
  ComboBox<Integer> yearCombo;
  @FXML
  LesxTreeTableViewPane<LesxPair<LesxResource, LesxBusiness>> mainPane;

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);
  //Components
  private TreeTableView<LesxPair<LesxResource, LesxBusiness>> table;
  //Columns
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> solicitud;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> nameColumn;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> cc;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> tipo;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> tipoVida;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> tipoAP;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> prima;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> primaVida;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> primaAP;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> nbs;
  private TreeTableColumn<LesxPair<LesxResource, LesxBusiness>, String> comision;
  //Data
  private Map<Long, LesxResource> dataResource;
  private LesxResourcesDataModel dataModelResource = new LesxResourcesDataModel();
  private LesxBusinessResourceDataModel dataModel = new LesxBusinessResourceDataModel();
  private ObservableList<LesxPair<LesxResource, LesxBusiness>> currentList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    showProgress.set(true);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE_MAIN"));
    yearLabel.setText(LesxMessage.getMessage("TEXT-YEAR"));
    table = mainPane.getTable();
    //Load Data Base
    dataModelResource.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getResourceMap());
    dataResource = dataModelResource.getMap();
    dataModel.setResourceMap(dataResource);
    dataModel.setBusinessMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessMap()); // TODO Not loading
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
    solicitud.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getFirst()
            .getSolicitud()
            .toString());
      }
    });
    cc = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getFirst()
            .getCc()
            .toString());
      }
    });
    nameColumn = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getFirst()
            .getName());
      }
    });
    tipo = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_TYPE_PRODUCT"));
    tipoVida = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    tipoVida.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getProduct()
            .toString());
      }
    });
    tipoAP = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    tipoAP.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getProduct()
            .toString());
      }
    });
    tipo.getColumns()
        .addAll(tipoVida, tipoAP);
    prima = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_TYPE_PRIMA"));
    primaVida = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    primaVida.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getProduct()
            .toString());
      }
    });
    primaAP = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    primaAP.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getProduct()
            .toString());
      }
    });
    prima.getColumns()
        .addAll(primaVida, primaAP);
    nbs = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NBS"));
    nbs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getNbs()
            .toString());
      }
    });
    comision = new TreeTableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_COMISION"));
    comision.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(javafx.scene.control.TreeTableColumn.CellDataFeatures<LesxPair<LesxResource, LesxBusiness>, String> param) {
        return new SimpleStringProperty(param.getValue()
            .getValue()
            .getSecond()
            .getPrima()
            .toString()); // TODO Calculate Comision
      }
    });

    solicitud.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.1));
    nameColumn.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    cc.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    tipo.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    prima.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    nbs.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    comision.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));

    table.setShowRoot(false);
    updateTreeTableData();
    table.getColumns()
        .setAll(solicitud, nameColumn, cc, tipo, prima, nbs, comision);
  }

  private void updateTreeTableData() {
    final TreeItem<LesxPair<LesxResource, LesxBusiness>> root = new TreeItem<>();
    for (ELesxMonth month : ELesxMonth.values()) {
      root.getChildren()
          .addAll(dataModel.getTreeItem(month, yearCombo.getValue()));
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
