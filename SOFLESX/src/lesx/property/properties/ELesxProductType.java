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

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_SALUD(1) {
    @Override
    public String toString() {
      return "AP SALUD";
    }

    @Override
    protected boolean isAP() {
      return true;
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

    @Override
    protected boolean isAP() {
      return true;
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

    @Override
    protected boolean isAP() {
      return true;
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
  VIDA_ENTERA_99(12) {
    @Override
    public String toString() {
      return "VIDA ENTERA 99";
    }
  },
  AP_MUJER(13) {
    @Override
    public String toString() {
      return "AP MUJER";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_HOMBRE(14) {
    @Override
    public String toString() {
      return "AP HOMBRE";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  GASTOS_MEDICOS_AZUL(15) {
    @Override
    public String toString() {
      return "GASTOS MEDICOS AZUL";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_GASTOS_MEDICOS_AZUL(16) {
    @Override
    public String toString() {
      return "AP GASTOS MEDICOS AZUL";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_GASTOS(17) {
    @Override
    public String toString() {
      return "AP GASTOS";
    }

    @Override
    protected boolean isAP() {
      return true;
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

    @Override
    protected boolean isAP() {
      return true;
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
  },
  VIDA_ENTERA_60(22) {
    @Override
    public String toString() {
      return "VIDA ENTERA 60";
    }
  },
  VIDA_ENTERA_80(23) {
    @Override
    public String toString() {
      return "VIDA ENTERA 80";
    }
  },
  TEMPORAL_50(24) {
    @Override
    public String toString() {
      return "TEMPORAL 50";
    }
  },
  TEMPORAL_60(25) {
    @Override
    public String toString() {
      return "TEMPORAL 60";
    }
  },
  TEMPORAL_70(26) {
    @Override
    public String toString() {
      return "TEMPORAL 70";
    }
  },
  DOTAL_15(27) {
    @Override
    public String toString() {
      return "Dotal 15";
    }
  },
  DOTAL_20(28) {
    @Override
    public String toString() {
      return "Dotal 20";
    }
  },
  AP_SALUD_AZUL(29) {
    @Override
    public String toString() {
      return "AP Salud Azul";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_FAMILIA(30) {
    @Override
    public String toString() {
      return "AP FAMILIA";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_EXPERIENCIA(31) {
    @Override
    public String toString() {
      return "AP EXPERIENCIA";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_BIENESTAR(33) {
    @Override
    public String toString() {
      return "AP BIENESTAR";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_EJECUTIVO(34) {
    @Override
    public String toString() {
      return "AP EJECUTIVO";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  AP_PLUS(35) {
    @Override
    public String toString() {
      return "AP PLUS";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  FAMILIA_AZUL_PESOS(36) {
    @Override
    public String toString() {
      return "FAMILIA AZUL PESOS";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  DO_MAS_PROTECCION(37) {
    @Override
    public String toString() {
      return "DO Más protección";
    }

    @Override
    protected boolean isAP() {
      return true;
    }
  },
  DO_RENTA_CLINICA(36) {
    @Override
    public String toString() {
      return "DO Renta Clínica";
    }

    @Override
    protected boolean isAP() {
      return true;
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

  public static List<String> stringAPList() {
    return mapValues.values()
        .stream()
        .filter(ap -> ap.isAP())
        .map(type -> type.toString())
        .sorted()
        .collect(Collectors.toList());
  }

  public static List<String> stringNotAPList() {
    return mapValues.values()
        .stream()
        .filter(type -> !type.isAP())
        .map(type -> type.toString())
        .sorted()
        .collect(Collectors.toList());
  }

  public static List<String> stringList() {
    return mapValues.values()
        .stream()
        .map(type -> type.toString())
        .sorted()
        .collect(Collectors.toList());
  }

  protected boolean isAP() {
    return false;
  }

}
