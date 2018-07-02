package lesx.ui.property.editor;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxProductType;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.LesxProductType;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.formatter.LesxNumberFormatter;
import lesx.utils.LesxMisc;
import lesx.utils.LesxString;

public class LesxProductEditor extends ComboBox<String> {

  private LesxProductType product;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

  //Displayed Cell
  private LesxListEditor productsVidaCombo = new LesxListEditor();
  private CheckBox vida = new CheckBox();
  private TextField editorVida = new TextField();
  private LesxListEditor productsAPCombo = new LesxListEditor();
  private CheckBox ap = new CheckBox();
  private TextField editorAP = new TextField();
  //flags
  private boolean ignoreListener = false;

  public LesxProductEditor(LesxProperty fxProperty) {
    if (fxProperty.getValue() != null) {
      product = ((LesxProductType) fxProperty.getValue()).clone();
    }
    else {
      product = new LesxProductType();
    }
    initializeCombo();
    productsVidaCombo.validProperty()
        .bindBidirectional(fxProperty.validProperty());
    setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> l) {
        return new ListCell<String>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(null);
            setText(null);
            if (item != null || !empty) {
              setGraphic(getDropdownContent());
            }
          }
        };
      }
    });
    getItems().add(ELesxPropertyType.PRODUCT_TYPE.toString());
    setSkin(new ComboBoxListViewSkin<String>(this) {
      @Override
      protected boolean isHideOnClickEnabled() {
        return false;
      }

      @Override
      public void hide() {
        fxProperty.setValue(product);
        super.hide();
      }
    });
    getStylesheets().add(LesxString.STYLE_PRODUCT_COMBO);
  }

  private void initializeCombo() {
    //Adds data
    productsVidaCombo.setListValues(ELesxProductType.stringList());
    productsAPCombo.setListValues(ELesxProductType.stringList());
    //Adds text
    vida.setText(LesxMessage.getMessage("TEXT-CHECKBOX_VIDA"));
    ap.setText(LesxMessage.getMessage("TEXT-CHECKBOX_AP"));
    // Bindings
    editorVida.disableProperty()
        .bind(Bindings.not(vida.selectedProperty()));
    editorAP.disableProperty()
        .bind(Bindings.not(ap.selectedProperty()));
    //create formatter
    LesxNumberFormatter formatter = new LesxNumberFormatter();
    editorVida.setTextFormatter(formatter.getFormat());
    formatter = new LesxNumberFormatter();
    editorAP.setTextFormatter(formatter.getFormat());
    if (product != null && (product.getTypeVida() != null || product.getTypeAP() != null)) {
      reBuildValue();
    }
    //Create Listeners
    productsAPCombo.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> buildValue());
    productsVidaCombo.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> buildValue());
    vida.selectedProperty()
        .addListener(obs -> buildValue());
    ap.selectedProperty()
        .addListener(obs -> buildValue());
    editorVida.textProperty()
        .addListener(obs -> buildValue());
    editorAP.textProperty()
        .addListener(obs -> buildValue());
  }

  private void buildValue() {
    if (!ignoreListener) {
      ignoreListener = true;
      try {
        if (!LesxMisc.isEmptyString(productsVidaCombo.getSelectionModel()
            .getSelectedItem())) {
          product.setTypeVida(ELesxProductType.get(productsVidaCombo.getSelectionModel()
              .getSelectedItem()));
        }
        else {
          product.setTypeVida(null);
        }
        if (!LesxMisc.isEmptyString(productsAPCombo.getSelectionModel()
            .getSelectedItem())) {
          product.setTypeAP(ELesxProductType.get(productsAPCombo.getSelectionModel()
              .getSelectedItem()));
        }
        else {
          product.setTypeAP(null);
        }
        if (vida.isSelected()) {
          product.setPrimaVida(Long.valueOf(LesxMisc.isEmptyString(editorVida.getText()) ? "0" : editorVida.getText()));
        }
        else {
          product.setPrimaVida(null);
          editorVida.setText("");
        }
        if (ap.isSelected()) {
          product.setPrimaAP(Long.valueOf(LesxMisc.isEmptyString(editorAP.getText()) ? "0" : editorAP.getText()));
        }
        else {
          product.setPrimaVida(null);
          editorAP.setText("");
        }
      }
      finally {
        ignoreListener = false;
      }
    }
  }

  private void reBuildValue() {
    if (product.getTypeVida() != null) {
      productsVidaCombo.getSelectionModel()
          .select(product.getTypeVida()
              .toString());
    }
    if (product.getTypeAP() != null) {
      productsAPCombo.getSelectionModel()
          .select(product.getTypeAP()
              .toString());
    }
    if (product.getPrimaAP() != null) {
      ap.setSelected(true);
      editorAP.setText(String.valueOf(product.getPrimaAP()));
    }
    if (product.getPrimaVida() != null) {
      vida.setSelected(true);
      editorVida.setText(String.valueOf(product.getPrimaVida()));
    }
  }

  private Node getDropdownContent() {
    VBox wrapper = new VBox();
    wrapper.setSpacing(5);
    Label titleVida = new Label();
    titleVida.setText(LesxMessage.getMessage("TEXT-CHECKBOX_VIDA"));
    HBox wrapperCheckBoxVida = new HBox();
    wrapperCheckBoxVida.setSpacing(5);
    wrapperCheckBoxVida.getChildren()
        .addAll(vida, editorVida);
    Label titleAP = new Label();
    titleAP.setText(LesxMessage.getMessage("TEXT-CHECKBOX_AP"));
    HBox wrapperCheckBoxAP = new HBox();
    wrapperCheckBoxAP.setSpacing(5);
    wrapperCheckBoxAP.getChildren()
        .addAll(ap, editorAP);
    wrapper.getChildren()
        .addAll(titleVida, productsVidaCombo, wrapperCheckBoxVida, new Separator(Orientation.HORIZONTAL), titleAP, productsAPCombo, wrapperCheckBoxAP);
    return wrapper;
  }

  public LesxProductType productTypeProperty() {
    return product;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

  public boolean isValid() {
    return valid.get();
  }

  public BooleanProperty validProperty() {
    return valid;
  }

}
