package lesx.ui.mainpage.resources;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResource;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.components.LesxTreeViewPane;

public class LesxResourcesPaneController extends LesxController {

  @FXML
  LesxTreeViewPane<ELesxLocations> treePane;
  @FXML
  LesxTableViewPane<LesxResource> tablePane;

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);
  //Components
  private TreeView<ELesxLocations> tree;
  private TableView<LesxResource> table;
  // Columns
  private TableColumn<LesxResource, String> solicitud;
  private TableColumn<LesxResource, String> locations;
  private TableColumn<LesxResource, String> nameColumn;
  private TableColumn<LesxResource, String> cc;
  private TableColumn<LesxResource, String> fechaNacimiento;
  private TableColumn<LesxResource, String> fechaRegistro;
  // Lists
  private final ObservableList<LesxResource> currentList = FXCollections.observableArrayList();
  //Data Model
  private LesxResourcesDataModel dataModel;

  @FXML
  public void initialize() {
    showProgress.set(true);
    tree = treePane.getTree();
    tablePane.setUseCase(ELesxUseCase.UC_RESOURCES);
    table = tablePane.getTable();
    configurateColumns();
    showProgress.set(false);
  }

  /**
   * Configures the Resources' Table
   */
  @SuppressWarnings("unchecked")
  private void configurateColumns() {
    solicitud = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_SOLICITATION"));
    solicitud.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getName());
      }
    });
    locations = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LOCATION"));
    locations.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(ELesxLocations.getLocation(data.getValue()
            .getLocation())
            .toString());
      }
    });
    cc = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getCc()
            .toString());
      }
    });
    nameColumn = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getName());
      }
    });
    fechaNacimiento = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_BIRTHDAY"));
    fechaNacimiento.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getBirthday());
      }
    });
    fechaRegistro = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_REGISTRATION_DATE"));
    fechaRegistro.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getRegistration_date());
      }
    });
    solicitud.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    locations.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    nameColumn.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.2));
    cc.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.2));
    fechaNacimiento.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    fechaRegistro.prefWidthProperty()
        .bind(table.widthProperty()
            .multiply(0.15));
    table.getColumns()
        .setAll(solicitud, locations, nameColumn, cc, fechaNacimiento, fechaRegistro);
    table.setItems(currentList);
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
