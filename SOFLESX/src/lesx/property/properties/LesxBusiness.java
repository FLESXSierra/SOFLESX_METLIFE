package lesx.property.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxString;
import lesx.xml.property.LesxBusinessXMLParser;

public class LesxBusiness extends LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxResource.class.getName());

  private LesxProperty id;
  private LesxProperty producto;
  private LesxProperty prima;
  private LesxProperty nbs;
  private LesxProperty resource_id;

  public LesxBusiness() {
    initializeProperty();
  }

  public LesxBusiness(LesxBusinessXMLParser parse) {
    initializeProperty();
    if (parse.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(parse.getId());
    }
    if (parse.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_RESOURCE_ID"));
      resource_id.setValue(-1L);
    }
    else {
      resource_id.setValue(parse.getResource_id());
    }
    producto.setValue(ELesxProductType.valueOf(parse.getProducto()));
    prima.setValue(parse.getPrima());
    nbs.setValue(parse.getNbs());
  }

  /**
   * Constructor to create property on the property sheet
   *
   * @param properties List of edited properties
   */
  public LesxBusiness(Collection<LesxProperty> properties) {
    initializeProperty();
    for (LesxProperty property : properties) {
      if (id.propertyEquals(property)) {
        id.setValue(property.getValue());
      }
      if (producto.propertyEquals(property)) {
        producto.setValue(property.getValue());
      }
      if (prima.propertyEquals(property)) {
        prima.setValue(property.getValue());
      }
      if (nbs.propertyEquals(property)) {
        nbs.setValue(property.getValue());
      }
      if (resource_id.propertyEquals(property)) {
        resource_id.setValue(property.getValue());
      }
    }
    setPropertyValues(Arrays.asList(id, resource_id, producto, prima, nbs));
  }

  private void initializeProperty() {
    id = new LesxProperty();
    id.setType(ELesxPropertyType.LONG);
    id.setName(LesxString.PROPERTY_ID);
    id.setMandatory(true);
    id.setReadOnly(true);
    id.setUnique(true);
    id.setVisible(false);
    producto = new LesxProperty();
    producto.setType(ELesxPropertyType.PRODUCT);
    producto.setName(LesxString.PROPERTY_PRODUCT);
    producto.setMandatory(true);
    prima = new LesxProperty();
    prima.setType(ELesxPropertyType.LONG);
    prima.setName(LesxString.PROPERTY_PRIMA);
    prima.setValue(0L);
    prima.setMandatory(true);
    nbs = new LesxProperty();
    nbs.setType(ELesxPropertyType.LONG);
    nbs.setName(LesxString.PROPERTY_PRIMA);
    nbs.setReadOnly(true);
    nbs.setValue(0L);
    resource_id = new LesxProperty();
    resource_id.setType(ELesxPropertyType.LONG);
    resource_id.setName(LesxString.PROPERTY_RESOURCE_ID);
    resource_id.setMandatory(true);
    resource_id.setReadOnly(true);
    resource_id.setUnique(true);
    resource_id.setVisible(false);
    setPropertyValues(Arrays.asList(id, resource_id, producto, prima, nbs));
    setKey(ELesxPropertyKeys.BUSINESS);
  }

  public Long getId() {
    return (Long) id.getValue();
  }

  public void setId(Long id) {
    this.id.setValue(id);
  }

  public ELesxProductType getProduct() {
    return (ELesxProductType) producto.getValue();
  }

  public void setProduct(ELesxProductType producto) {
    this.producto.setValue(producto);
  }

  public void setProduct(Integer producto) {
    this.producto.setValue(ELesxProductType.valueOf(producto));
  }

  public Long getPrima() {
    return (Long) prima.getValue();
  }

  public void setPrima(Long prima) {
    this.prima.setValue(prima);
  }

  public Long getNbs() {
    return (Long) nbs.getValue();
  }

  public void setNbs(Long nbs) {
    this.nbs.setValue(nbs);
  }

  public Long getResource_id() {
    return (Long) resource_id.getValue();
  }

  public void setResource_id(Long resource_id) {
    this.resource_id.setValue(resource_id);
  }

  @Override
  public LesxBusiness clone() {
    return (LesxBusiness) super.clone();
  }

}
