package lesx.property.properties;

import lesx.utils.LesxString;
import lesx.xml.property.LesxProductTypeXMLParser;

public class LesxProductType extends LesxComponent implements Cloneable {

  private LesxProperty primaVida;
  private LesxProperty typeVida;
  private LesxProperty primaAP;
  private LesxProperty typeAP;

  public LesxProductType() {
    initializeProperties();
  }

  public LesxProductType(LesxProductTypeXMLParser product) {
    initializeProperties();
    typeVida.setValue(ELesxProductType.valueOf(product.getTypeVida()));
    this.primaVida.setValue(product.getPrimaVida());
    this.primaAP.setValue(product.getPrimaAP());
    typeAP.setValue(ELesxProductType.valueOf(product.getTypeAP()));
  }

  private void initializeProperties() {
    primaVida = new LesxProperty();
    primaVida.setMandatory(true);
    primaVida.setName(LesxString.PROPERTY_PRIMA);
    primaVida.setType(ELesxPropertyType.LONG);
    typeVida = new LesxProperty();
    typeVida.setName(LesxString.PROPERTY_PRODUCT_TYPE);
    typeVida.setType(ELesxPropertyType.PRODUCT_TYPE);
    primaAP = new LesxProperty();
    primaAP.setMandatory(true);
    primaAP.setName(LesxString.PROPERTY_PRIMA);
    primaAP.setType(ELesxPropertyType.LONG);
    typeAP = new LesxProperty();
    typeAP.setName(LesxString.PROPERTY_PRODUCT_TYPE);
    typeAP.setType(ELesxPropertyType.PRODUCT_TYPE);
    getPropertyValues().addAll(primaVida, typeVida, primaAP, typeAP);
  }

  public Long getPrimaVida() {
    return (Long) primaVida.getValue();
  }

  public LesxProperty primaVidaProperty() {
    return primaVida;
  }

  public void setPrimaVida(Long prima) {
    this.primaVida.setValue(prima);
  }

  public ELesxProductType getTypeVida() {
    return (ELesxProductType) typeVida.getValue();
  }

  public LesxProperty typeVidaProperty() {
    return typeVida;
  }

  public void setTypeVida(ELesxProductType type) {
    this.typeVida.setValue(type);
  }

  public Long getPrimaAP() {
    return (Long) primaAP.getValue();
  }

  public LesxProperty primaAPProperty() {
    return primaAP;
  }

  public void setPrimaAP(Long prima) {
    this.primaAP.setValue(prima);
  }

  public ELesxProductType getTypeAP() {
    return (ELesxProductType) typeAP.getValue();
  }

  public LesxProperty typeAPProperty() {
    return typeAP;
  }

  public void setTypeAP(ELesxProductType typeAP) {
    this.typeAP.setValue(typeAP);
  }

  @Override
  public LesxProductType clone() {
    return (LesxProductType) super.clone();
  }

}
