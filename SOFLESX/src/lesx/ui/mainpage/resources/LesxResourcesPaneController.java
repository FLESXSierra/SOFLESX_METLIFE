package lesx.ui.mainpage.resources;

import static lesx.property.properties.ELesxLocations.COLOMBIA;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.LesxResource;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.components.LesxTreeViewPane;
import lesx.ui.soflesx.LesxMain;

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
  //Data
  private Map<Long, LesxResource> data;
  private LesxResourcesDataModel dataModel = new LesxResourcesDataModel();
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges");
  private BooleanProperty selectedItemTable = new SimpleBooleanProperty(this, "selectedItemTable");
  //Runnables
  private Runnable onDelete;
  private Consumer<Boolean> onAdd;

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE_RESOURCES"));
    //Load Data Base
    dataModel.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getResourceMap());
    data = dataModel.getMap();
    showProgress.set(true);
    tree = treePane.getTree();
    table = tablePane.getTable();
    configurateColumns();
    fillDataOnTree();
    filterTable();
    installListeners();
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

  /**
   * Filter the table, right now only from TreeView can be filtered.
   */
  private void filterTable() {
    ELesxLocations location = COLOMBIA;
    if (tree.getSelectionModel()
        .getSelectedItem() != null) {
      location = tree.getSelectionModel()
          .getSelectedItem()
          .getValue();
    }
    if (location != null) {
      currentList.setAll(dataModel.getValuesByLocation(location, treePane.isChildrenAlso()));
    }
    else {
      currentList.setAll(data.values());
    }
    table.refresh();
  }

  /**
   * Adds data on TreeView
   */
  private void fillDataOnTree() {
    tree.setRoot(null);
    TreeItem<ELesxLocations> rootItem = new TreeItem<ELesxLocations>(COLOMBIA);
    Set<ELesxLocations> locations = dataModel.getDataLocations();
    Map<ELesxLocations, TreeItem<ELesxLocations>> mapTree = new HashMap<>();
    Set<ELesxLocations> addedLocation = mapTree.keySet();
    mapTree.put(COLOMBIA, rootItem);
    rootItem.setExpanded(true);
    TreeItem<ELesxLocations> leafItem;
    for (ELesxLocations location : locations) {
      if (location == COLOMBIA) {
        continue;
      }
      if (!addedLocation.contains(location)) {
        leafItem = new TreeItem<ELesxLocations>(location);
        mapTree.put(location, leafItem);
        addMissingParentItem(location, mapTree, leafItem);
      }
    }
    tree.setRoot(rootItem);
  }

  /**
   * Recursive method to fill Tree data with no duplicates parents key
   *
   * @param location ELesxLocations needed to track the parent and map values of it.
   * @param mapTree Map of available children with item values
   * @param leafItem Tree item value to add
   */
  private void addMissingParentItem(ELesxLocations location, Map<ELesxLocations, TreeItem<ELesxLocations>> mapTree, TreeItem<ELesxLocations> leafItem) {
    if (mapTree.containsKey(ELesxLocations.getParentLocation(location))) {
      mapTree.get(ELesxLocations.getParentLocation(location))
          .getChildren()
          .add(leafItem);
      return;
    }
    ELesxLocations parentMissing = ELesxLocations.getParentLocation(location);
    TreeItem<ELesxLocations> parentTree = new TreeItem<ELesxLocations>(parentMissing);
    parentTree.getChildren()
        .add(leafItem);
    mapTree.put(parentMissing, parentTree);
    addMissingParentItem(parentMissing, mapTree, parentTree);
  }

  private void installListeners() {
    treePane.childrenAlsoProperty()
        .addListener(obs -> filterTable());
    tree.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> {
          boolean isSelected = tree.getSelectionModel()
              .getSelectedItem() != null;
          if (isSelected) {
            dataModel.setLocationsSelected(tree.getSelectionModel()
                .getSelectedItem()
                .getValue());
          }
          else {
            dataModel.setLocationsSelected(null);
          }
          filterTable();
        });
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedItemTable());
    createRunnables();
  }

  private void createRunnables() {
    onDelete = () -> {
      dataModel.deleteSelectedCostumer();
      pendingChanges.set(true);
      filterTable();
      fillDataOnTree();
    };
    tablePane.setOnDelete(onDelete);
    onAdd = (isCreate) -> addNewResource(isCreate);
    tablePane.setOnAddNewItem(onAdd);
  }

  private void selectedItemTable() {
    selectedItemTable.set(table.getSelectionModel()
        .getSelectedItem() != null);
    if (table.getSelectionModel() != null) {
      dataModel.setResourceSelected(table.getSelectionModel()
          .getSelectedItem());
    }
    else {
      dataModel.setResourceSelected(null);
    }
  }

  private void addNewResource(boolean isCreate) {
    //    LesxSceneController.showAddResourceDialog(this, isCreate, dataModel, () -> {
    //      pendingChanges.set(true);
    //      filterTable();
    //      fillDataOnTree();
    //    });
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
