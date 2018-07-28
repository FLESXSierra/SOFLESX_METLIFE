package lesx.property.properties;

public class LesxReportMonthBusiness extends LesxComponent {

  Integer month;
  Long vida;
  Long ap;
  Long NBS;
  Long comision;

  public LesxReportMonthBusiness(Integer month) {
    this.month = month;
  }

  public Long getVida() {
    return vida;
  }

  public void setVida(Long vida) {
    this.vida = vida;
  }

  public Long getAp() {
    return ap;
  }

  public void setAp(Long ap) {
    this.ap = ap;
  }

  public Long getNBS() {
    return NBS;
  }

  public void setNBS(Long nBS) {
    NBS = nBS;
  }

  public Long getComision() {
    return comision;
  }

  public void setComision(Long comision) {
    this.comision = comision;
  }

  public Integer getMonth() {
    return month;
  }

}
