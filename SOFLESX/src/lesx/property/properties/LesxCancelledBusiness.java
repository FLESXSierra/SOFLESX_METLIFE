package lesx.property.properties;

import lesx.utils.LesxString;
import lesx.xml.property.LesxCancelledBusinessXMLParser;

public class LesxCancelledBusiness extends LesxComponent implements Cloneable {

  private LesxProperty value;
  private LesxProperty date;

  public LesxCancelledBusiness() {
    initializeProperties();
  }

  public LesxCancelledBusiness(LesxCancelledBusinessXMLParser parser) {
    initializeProperties();
    value.setValue(parser != null && parser.getValue() != null ? parser.getValue() : Boolean.FALSE);
    date.setValue(parser != null ? parser.getDate() : null);
  }

  private void initializeProperties() {
    value = new LesxProperty();
    value.setName(LesxString.PROPERTY_YES_NO_DATE);
    value.setType(ELesxPropertyType.YES_NO_DATE);
    date = new LesxProperty();
    date.setName(LesxString.PROPERTY_DATE);
    date.setType(ELesxPropertyType.DATE);
    getPropertyValues().addAll(value, date);
  }

  /**
   * @return the value property
   */
  public LesxProperty getValueProperty() {
    return value;
  }

  /**
   * @return the value
   */
  public Boolean getValue() {
    return (Boolean) value.getValue();
  }

  /**
   * @param value the value to set
   */
  public void setValue(Boolean value) {
    this.value.setValue(value != null ? value : Boolean.FALSE);
  }

  /**
   * @return the date property
   */
  public LesxProperty getDateProperty() {
    return date;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return (String) date.getValue();
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date.setValue(date);
  }

  @Override
  public LesxCancelledBusiness clone() {
    return (LesxCancelledBusiness) super.clone();
  }

}
