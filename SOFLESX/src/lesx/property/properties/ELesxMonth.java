package lesx.property.properties;

import java.util.HashMap;
import java.util.Map;

public enum ELesxMonth {

  ENERO(0) {
    @Override
    public String toString() {
      return "Enero";
    }
  },
  FEBRERO(1) {
    @Override
    public String toString() {
      return "Febrero";
    }
  },
  MARZO(2) {
    @Override
    public String toString() {
      return "Marzo";
    }
  },
  ABRIL(3) {
    @Override
    public String toString() {
      return "Abril";
    }
  },
  MAYO(4) {
    @Override
    public String toString() {
      return "Mayo";
    }
  },
  JUNIO(5) {
    @Override
    public String toString() {
      return "Junio";
    }
  },
  JULIO(6) {
    @Override
    public String toString() {
      return "Julio";
    }
  },
  AGOSTO(7) {
    @Override
    public String toString() {
      return "Agosto";
    }
  },
  SEPTIEMBRE(8) {
    @Override
    public String toString() {
      return "Septiembre";
    }
  },
  OCTUBRE(9) {
    @Override
    public String toString() {
      return "Octubre";
    }
  },
  NOVIEMBRE(10) {
    @Override
    public String toString() {
      return "Noviembre";
    }
  },
  DICIEMBRE(12) {
    @Override
    public String toString() {
      return "Diciembre";
    }
  };

  private Integer key;
  private static Map<Integer, ELesxMonth> mapValues = new HashMap<>();

  static {
    for (ELesxMonth product : ELesxMonth.values()) {
      mapValues.put(product.getKey(), product);
    }
  }

  private ELesxMonth(int key) {
    this.key = key;
  }

  public Integer getKey() {
    return key;
  }

  public static ELesxMonth valueOf(Integer key) {
    return mapValues.get(key);
  }

}
