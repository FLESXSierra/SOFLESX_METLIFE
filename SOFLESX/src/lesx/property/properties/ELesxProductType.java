package lesx.property.properties;

import java.util.HashMap;
import java.util.Map;

public enum ELesxProductType {

  VIDA(0) {
    @Override
    public String toString() {
      return "Vida";
    }
  };

  private Integer key;
  private static Map<Integer, ELesxProductType> mapValues = new HashMap<>();

  static {
    for (ELesxProductType product : ELesxProductType.values()) {
      mapValues.put(product.getKey(), product);
    }
  }

  private ELesxProductType(int key) {
    this.key = key;
  }

  public Integer getKey() {
    return key;
  }

  public static ELesxProductType valueOf(Integer key) {
    return mapValues.get(key);
  }

}
