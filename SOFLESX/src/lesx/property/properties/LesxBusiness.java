package lesx.property.properties;

import java.util.Arrays;

import lesx.utils.LesxString;

public class LesxBusiness extends LesxComponent {

  private LesxProperty id;
  private LesxProperty producto;
  private LesxProperty prima;
  private LesxProperty nbs;

  public LesxBusiness() {
    initializeProperty();
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
    setPropertyValues(Arrays.asList(id, producto, prima, nbs));
  }

}
