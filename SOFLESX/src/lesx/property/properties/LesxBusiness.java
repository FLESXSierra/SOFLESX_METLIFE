package lesx.property.properties;

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxBusinessXMLParser;

public class LesxBusiness extends LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxResource.class.getName());

  private LesxProperty id;
  private LesxProperty producto;
  private LesxProperty date;
  private LesxProperty resource_id;
  private LesxProperty cancelled;

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
    producto.setValue(new LesxProductType(parse.getProducto()));
    cancelled.setValue(new LesxCancelledBusiness(parse.getCancelled()));
    date.setValue(parse.getDate());
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
      if (resource_id.propertyEquals(property)) {
        resource_id.setValue(property.getValue());
      }
      if (date.propertyEquals(property)) {
        date.setValue(property.getValue());
      }
      if (cancelled.propertyEquals(property)) {
        cancelled.setValue(property.getValue());
      }
    }
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
    producto.setType(ELesxPropertyType.PRODUCT_TYPE);
    producto.setName(LesxString.PROPERTY_PRODUCT_TYPE);
    producto.setMandatory(true);
    date = new LesxProperty();
    date.setType(ELesxPropertyType.DATE);
    date.setValue(LocalDate.now()
        .format(LesxPropertyUtils.FORMATTER)
        .toString());
    date.setName(LesxString.PROPERTY_BUSINESS_DATE);
    date.setMandatory(true);
    resource_id = new LesxProperty();
    resource_id.setType(ELesxPropertyType.LONG);
    resource_id.setName(LesxString.PROPERTY_RESOURCE_ID);
    resource_id.setMandatory(true);
    resource_id.setReadOnly(true);
    resource_id.setUnique(true);
    resource_id.setVisible(false);
    cancelled = new LesxProperty();
    cancelled.setType(ELesxPropertyType.YES_NO_DATE);
    cancelled.setName(LesxString.PROPERTY_YES_NO_DATE);
    getPropertyValues().addAll(id, resource_id, producto, date, cancelled);
    setKey(ELesxPropertyKeys.BUSINESS);
  }

  public Long getId() {
    return (Long) id.getValue();
  }

  public void setId(Long id) {
    this.id.setValue(id);
  }

  public LesxProductType getProduct() {
    return (LesxProductType) producto.getValue();
  }

  public void setProduct(LesxProductType producto) {
    this.producto.setValue(producto);
  }

  public LesxCancelledBusiness getCancelled() {
    return (LesxCancelledBusiness) cancelled.getValue();
  }

  public void setCancelled(LesxCancelledBusiness producto) {
    this.cancelled.setValue(producto);
  }

  public Long getNbs() {
    Long nbs = null;
    if (getProduct() != null) {
      if (getProduct().getPrimaAP() != null) {
        nbs = getProduct().getPrimaAP() * 12;
      }
      if (getProduct().getPrimaVida() != null) {
        nbs = nbs != null ? (nbs + getProduct().getPrimaVida() * 12) : getProduct().getPrimaVida() * 12;
      }
    }
    return nbs;
  }

  public Long getResource_id() {
    return (Long) resource_id.getValue();
  }

  public void setResource_id(Long resource_id) {
    this.resource_id.setValue(resource_id);
  }

  public String getDate() {
    return (String) date.getValue();
  }

  public void setDate(String date) {
    this.date.setValue(date);
  }

  @Override
  public LesxBusiness clone() {
    return (LesxBusiness) super.clone();
  }

}
