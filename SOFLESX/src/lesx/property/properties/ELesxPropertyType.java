package lesx.property.properties;

import java.util.HashMap;
import java.util.Map;

import lesx.utils.LesxString;

public enum ELesxPropertyType {
  INTEGER(0) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_INTEGER;
    }
  },
  TEXT(1) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_TEXT;
    }
  },
  LONG(2) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LONG;
    }
  },
  LOCATION(3) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LOCATION;
    }
  },
  PRICE_TYPE(4) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_PRICE_TYPE;
    }
  },
  LIST(5) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LIST;
    }
  },
  DATE(6) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_DATE;
    }
  },
  PRODUCT(7) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_PRODUCT;
    }
  },
  PRODUCT_TYPE(8) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_PRODUCT_TYPE;
    }
  };

  private int key;
  private static Map<Integer, ELesxPropertyType> mapValues = new HashMap<>();

  static {
    for (ELesxPropertyType product : ELesxPropertyType.values()) {
      mapValues.put(product.getValue(), product);
    }
  }

  ELesxPropertyType(int key) {
    this.key = key;
  }

  public int getValue() {
    return key;
  }

  public static ELesxPropertyType valueOf(Integer key) {
    return mapValues.get(key);
  }

}
