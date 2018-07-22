package lesx.ui.components.dialogs;

import java.text.NumberFormat;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxToolBar;
import lesx.utils.LesxPropertyUtils;

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

  private LesxToolBar toolbar;
  private LesxBusinessResourceDataModel dataModel;

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
  }

  private void initializeData() {
    // Total NBS
    double total = dataModel.getResourceBusinessList()
        .stream()
        .filter(rbItem -> {
          int year = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return year == toolbar.yearProperty()
              .get();
        })
        .mapToDouble(rbItem -> rbItem.getBusiness()
            .getNbs())
        .sum();
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    totalNBS.setText(formatter.format(total));
    // Count Vida
    long totalVida = dataModel.getResourceBusinessList()
        .stream()
        .filter(rbItem -> {
          int year = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return year == toolbar.yearProperty()
              .get();
        })
        .filter(rbItem -> rbItem.getBusiness()
            .getProduct()
            .getTypeVida() != null)
        .count();
    quantityVida.setText(String.valueOf(totalVida));
    // Count AP
    long totalAP = dataModel.getResourceBusinessList()
        .stream()
        .filter(rbItem -> {
          int year = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return year == toolbar.yearProperty()
              .get();
        })
        .filter(rbItem -> rbItem.getBusiness()
            .getProduct()
            .getTypeAP() != null)
        .count();
    quantityAP.setText(String.valueOf(totalAP));
  }

}
