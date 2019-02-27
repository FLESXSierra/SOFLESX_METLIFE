package lesx.ui.property.editor;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
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
  private VBox graphicCombo;
  //flags
  private boolean ignoreListener = false;
  private boolean listenerInstaled = false;
  //Listener
  private InvalidationListener build;

  public LesxProductEditor(LesxProperty fxProperty) {
    if (fxProperty.getValue() != null) {
      product = ((LesxProductType) fxProperty.getValue()).clone();
    }
    else {
      product = new LesxProductType();
    }
    installDropdownContent();
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
              setGraphic(graphicCombo);
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
        removeListenerToPopup();
        fxProperty.setValue(product.clone()); // Triggers value changed
        super.hide();
      }

      @Override
      public void show() {
        addListenersToPopup();
        super.show();
      }

    });
    getStylesheets().add(LesxString.STYLE_PRODUCT_COMBO);
  }

  private void initializeCombo() {
    //initialize Listener
    build = obs -> buildValue();
    //Adds data
    productsVidaCombo.setListValues(ELesxProductType.stringNotAPList());
    productsAPCombo.setListValues(ELesxProductType.stringAPList());
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
    if (product != null) {
      reBuildValue();
    }
    addListenersToPopup();
  }

  private void removeListenerToPopup() {
    if (listenerInstaled) {
      try {
        productsAPCombo.valueListProperty()
            .removeListener(build);
        productsVidaCombo.valueListProperty()
            .removeListener(build);
        vida.selectedProperty()
            .removeListener(build);
        ap.selectedProperty()
            .removeListener(build);
        editorVida.textProperty()
            .removeListener(build);
        editorAP.textProperty()
            .removeListener(build);
      }
      finally {
        listenerInstaled = false;
      }
    }
  }

  /**
   * Create Listeners
   */
  private void addListenersToPopup() {
    if (!listenerInstaled) {
      listenerInstaled = true;
      productsAPCombo.valueListProperty()
          .addListener(build);
      productsVidaCombo.valueListProperty()
          .addListener(build);
      vida.selectedProperty()
          .addListener(build);
      ap.selectedProperty()
          .addListener(build);
      editorVida.textProperty()
          .addListener(build);
      editorAP.textProperty()
          .addListener(build);
    }
  }

  private void buildValue() {
    if (!ignoreListener && isShowing()) {
      ignoreListener = true;
      try {
        if (!LesxMisc.isEmptyString(productsVidaCombo.getValueList())) {
          product.setTypeVida(ELesxProductType.get(productsVidaCombo.getValueList()));
          vida.setDisable(false);
        }
        else {
          product.setTypeVida(null);
          vida.setDisable(true);
          vida.setSelected(false);
        }
        if (!LesxMisc.isEmptyString(productsAPCombo.getValueList())) {
          product.setTypeAP(ELesxProductType.get(productsAPCombo.getValueList()));
          ap.setDisable(false);
        }
        else {
          product.setTypeAP(null);
          ap.setDisable(true);
          ap.setSelected(false);
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
          product.setPrimaAP(null);
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
    else {
      vida.setDisable(true);
      vida.setSelected(false);
    }
    if (product.getTypeAP() != null) {
      productsAPCombo.getSelectionModel()
          .select(product.getTypeAP()
              .toString());
    }
    else {
      ap.setDisable(true);
      ap.setSelected(false);
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

  private void installDropdownContent() {
    graphicCombo = new VBox();
    graphicCombo.setSpacing(5);
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
    graphicCombo.getChildren()
        .addAll(titleVida, productsVidaCombo, wrapperCheckBoxVida, new Separator(Orientation.HORIZONTAL), titleAP, productsAPCombo, wrapperCheckBoxAP);
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
