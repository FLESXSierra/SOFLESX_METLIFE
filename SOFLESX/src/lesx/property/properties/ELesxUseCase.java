package lesx.property.properties;

public enum ELesxUseCase {
  UC_TREE(0) {
    @Override
    public String toString() {
      return "�rbol";
    }
  },
  UC_TABLE(1) {
    @Override
    public String toString() {
      return "Tabla";
    }
  },
  UC_READ_ONLY(2) {
    @Override
    public String toString() {
      return "Solo lectura";
    }
  },
  UC_XML_RESOURCE(3) {
    @Override
    public String toString() {
      return "Costumer";
    }
  },
  UC_XML_BUSINESS(4) {
    @Override
    public String toString() {
      return "Precios";
    }
  },
  UC_XML_REPORT_TREE(5) {
    @Override
    public String toString() {
      return "�rbol de reportes";
    }
  },
  UC_XML_REPORT_ITEMS(6) {
    @Override
    public String toString() {
      return "Report Items";
    }
  },
  UC_TREE_MODIFY(7) {
    @Override
    public String toString() {
      return "�rbol Modificable";
    }
  },
  UC_ADD_REMOVE_ONLY(8) {
    @Override
    public String toString() {
      return "Agregar y Remover";
    }
  },
  UC_DELETE_ONLY(9) {
    @Override
    public String toString() {
      return "Solo Eliminar";
    }
  },
  UC_RESOURCES(10) {
    @Override
    public String toString() {
      return "Recursos";
    }
  },
  UC_TREE_TABLE_INICIO(11) {
    @Override
    public String toString() {
      return "Inicio";
    }
  };

  int key;

  private ELesxUseCase(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }
}
