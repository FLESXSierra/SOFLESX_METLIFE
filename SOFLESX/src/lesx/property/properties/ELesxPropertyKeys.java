package lesx.property.properties;

import lesx.utils.LesxString;

public enum ELesxPropertyKeys {
  RESOURCE(0L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_COSTUMER;
    }
  },
  BUSINESS(1L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_BUSINESS;
    }
  },
  RESOURCE_BUSINESS(2L) {
    @Override
    public String toString() {
      return "Cliente-Negocio";
    }
  };

  private Long key;

  ELesxPropertyKeys(Long key) {
    this.key = key;
  }

  public Long getValue() {
    return key;
  }

}
