package lesx.ui.components.dialogs;

import java.text.NumberFormat;
import java.util.Comparator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxReportMonthBusiness;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxToolBar;

public class LesxYearDialogController extends LesxController {

  @FXML
  BorderPane container;
  @FXML
  Label header;
  @FXML
  Label totalNBSTitle;
  @FXML
  Label totalNBS;
  @FXML
  Label totalSellTitle;
  @FXML
  Label vidaTitle;
  @FXML
  Label apTitle;
  @FXML
  Label quantityVida;
  @FXML
  Label quantityAP;
  @FXML
  TableView<LesxReportMonthBusiness> detailsTable;

  //Columns
  private TableColumn<LesxReportMonthBusiness, String> month;
  private TableColumn<LesxReportMonthBusiness, String> ap;
  private TableColumn<LesxReportMonthBusiness, String> vida;
  private TableColumn<LesxReportMonthBusiness, String> nbs;

  private LesxToolBar toolbar;
  private LesxBusinessResourceDataModel dataModel;
  private ObservableList<LesxReportMonthBusiness> report = FXCollections.observableArrayList();
  Comparator<LesxReportMonthBusiness> comparator = Comparator.comparingInt(LesxReportMonthBusiness::getMonth);

  public void init(LesxBusinessResourceDataModel dataModel) {
    this.dataModel = dataModel;
    initializeData();
  }

  @FXML
  public void initialize() {
    toolbar = new LesxToolBar(ELesxUseCase.UC_YEAR_ONLY);
    container.setTop(toolbar);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_YEAR_DIALOG"));
    header.setText(LesxMessage.getMessage("TEXT-HEADER_YEAR_DIALOG"));
    vidaTitle.setText(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    apTitle.setText(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    toolbar.yearProperty()
        .addListener(obs -> reInitialize());
    initializeComponentes();
  }

  private void reInitialize() {
    initializeComponentes();
    initializeData();
  }

  private void initializeComponentes() {
    totalNBSTitle.setText(LesxMessage.getMessage("TEXT-COLUMN_TITLE_YEAR_NBS", toolbar.yearProperty()
        .get()));
    totalSellTitle.setText(LesxMessage.getMessage("TEXT-COLUMN_TITLE_YEAR_SELL", toolbar.yearProperty()
        .get()));
    initializeTable();
  }

  @SuppressWarnings("unchecked")
  private void initializeTable() {
    month = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_MONTH"));
    month.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        return new SimpleStringProperty(ELesxMonth.valueOf(data.getValue()
            .getMonth() - 1)
            .toString());
      }
    });
    ap = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_AP"));
    ap.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        return new SimpleStringProperty(String.valueOf(data.getValue()
            .getAp()));
      }
    });
    vida = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LIFE"));
    vida.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        return new SimpleStringProperty(String.valueOf(data.getValue()
            .getVida()));
      }
    });
    nbs = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NBS"));
    nbs.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return new SimpleStringProperty(formatter.format(data.getValue()
            .getNBS()));
      }
    });
    detailsTable.getColumns()
        .setAll(month, vida, ap, nbs);
    detailsTable.setItems(report);
  }

  private void initializeData() {
    // Total NBS
    double total = dataModel.getNBSTotalFromYear(toolbar.yearProperty()
        .get());
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    totalNBS.setText(formatter.format(total));
    // Count Vida
    long totalVida = dataModel.countTotalVidaFromYear(toolbar.yearProperty()
        .get());
    quantityVida.setText(String.valueOf(totalVida));
    // Count AP
    long totalAP = dataModel.countTotalAPFromYear(toolbar.yearProperty()
        .get());
    quantityAP.setText(String.valueOf(totalAP));
    report.setAll(dataModel.getMonthToMonthNBSReport(toolbar.yearProperty()
        .get()));
    FXCollections.sort(report, comparator);
  }

}
