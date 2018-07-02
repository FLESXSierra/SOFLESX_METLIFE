package lesx.property.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ELesxProductType {

  AP_PENSION(0) {
    @Override
    public String toString() {
      return "AP PENSIÓN";
    }
  },
  AP_SALUD(1) {
    @Override
    public String toString() {
      return "AP SALUD";
    }
  },
  PENSION_62(2) {
    @Override
    public String toString() {
      return "PENSION 62";
    }
  },
  TEMPORAL_62(3) {
    @Override
    public String toString() {
      return "TEMPORAL 62";
    }
  },
  METCAPITAL_15(4) {
    @Override
    public String toString() {
      return "METCAPITAL 15";
    }
  },
  AP_RENTA_AZUL(5) {
    @Override
    public String toString() {
      return "AP RENTA AZUL";
    }
  },
  PENSION_57(6) {
    @Override
    public String toString() {
      return "PENSION 57";
    }
  },
  TEMPORAL_57(7) {
    @Override
    public String toString() {
      return "TEMPORAL 57";
    }
  },
  PENSION_PLUS(8) {
    @Override
    public String toString() {
      return "PENSION PLUS";
    }
  },
  AP_TRANQUILIDAD(9) {
    @Override
    public String toString() {
      return "AP TRANQUILIDAD";
    }
  },
  VIDA_ENTERA_50(10) {
    @Override
    public String toString() {
      return "VIDA ENTERA 50";
    }
  },
  METCAPITAL_20(11) {
    @Override
    public String toString() {
      return "METCAPITAL 20";
    }
  },
  VIDA_99(12) {
    @Override
    public String toString() {
      return "VIDA 99";
    }
  },
  AP_MUJER(13) {
    @Override
    public String toString() {
      return "AP MUJER";
    }
  },
  AP_HOMBRE(14) {
    @Override
    public String toString() {
      return "AP HOMBRE";
    }
  },
  GASTOS_MEDICOS_AZUL(15) {
    @Override
    public String toString() {
      return "GASTOS MEDICOS AZUL";
    }
  },
  AP_GASTOS_MEDICOS_AZUL(16) {
    @Override
    public String toString() {
      return "AP GASTOS MEDICOS AZUL";
    }
  },
  AP_GASTOS(17) {
    @Override
    public String toString() {
      return "AP GASTOS";
    }
  },
  METCAPITAL_25(18) {
    @Override
    public String toString() {
      return "METCAPITAL 25";
    }
  },
  AP_GASTOS_MEDICOS(19) {
    @Override
    public String toString() {
      return "AP GASTOS MEDICOS";
    }
  },
  VIDA_ENTERA_70(20) {
    @Override
    public String toString() {
      return "VIDA ENTERA 70";
    }
  },
  TEMPORAL_80(21) {
    @Override
    public String toString() {
      return "TEMPORAL 80";
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

  public static ELesxProductType get(String name) {
    return mapValues.values()
        .stream()
        .filter(val -> val.toString()
            .equals(name))
        .findFirst()
        .orElse(null);
  }

  public static List<String> stringList() {
    return mapValues.values()
        .stream()
        .map(type -> type.toString())
        .sorted()
        .collect(Collectors.toList());
  }

}
