package lesx.property.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lesx.gui.message.LesxMessage;
import lesx.utils.LesxMisc;

public class LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxComponent.class.getName());

  private ObservableList<LesxProperty> propertyValues = FXCollections.observableArrayList();
  private ELesxPropertyKeys key;

  public ELesxPropertyKeys getKey() {
    return key;
  }

  public void setKey(ELesxPropertyKeys key) {
    this.key = key;
  }

  public ObservableList<LesxProperty> getPropertyValues() {
    return propertyValues;
  }

  /**
   * Finds the first property value filtered by name, can be null
   *
   * @param name to be searched
   * @return LesxProperty
   */
  public LesxProperty getPropertyByName(String name) {
    return propertyValues.stream()
        .filter(s -> LesxMisc.equals(name, s.getName()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public LesxComponent clone() {
    try {
      return (LesxComponent) super.clone();
    }
    catch (CloneNotSupportedException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_NOT_CLONNABLE", this), e);
      e.printStackTrace();
    }
    return new LesxComponent();
  }
}
