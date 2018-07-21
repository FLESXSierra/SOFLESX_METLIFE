package lesx.property.properties;

public enum ELesxUseCase {
  UC_TREE(0) {
    @Override
    public String toString() {
      return "Árbol";
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
  UC_TREE_MODIFY(5) {
    @Override
    public String toString() {
      return "Árbol Modificable";
    }
  },
  UC_ADD_REMOVE_ONLY(6) {
    @Override
    public String toString() {
      return "Agregar y Remover";
    }
  },
  UC_DELETE_ONLY(7) {
    @Override
    public String toString() {
      return "Solo Eliminar";
    }
  },
  UC_RESOURCES(8) {
    @Override
    public String toString() {
      return "Recursos";
    }
  },
  UC_TREE_TABLE_INICIO(9) {
    @Override
    public String toString() {
      return "Inicio";
    }
  },
  EDIT(10),
  ADD_ONLY(11),
  ADD(12);

  int key;

  private ELesxUseCase(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }
}
