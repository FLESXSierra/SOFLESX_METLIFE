package lesx.property.properties;

public class LesxReportMonthBusiness extends LesxComponent {

  Integer month;
  Long vida = 0L;
  Long ap = 0L;
  Long NBS;
  Long comision;
  Long capp_ap_15 = 15L;
  Long capp_vida_15 = 15L;

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

  public Long getCappAp15() {
    return capp_ap_15;
  }

  public void setCappAp15(Long capp_ap_15) {
    this.capp_ap_15 = capp_ap_15;
  }

  public Long getCappVida15() {
    return capp_vida_15;
  }

  public void setCappVida15(Long capp_vida_15) {
    this.capp_vida_15 = capp_vida_15;
  }

  public Integer getMonth() {
    return month;
  }

  public String getStringCapp() {
    StringBuilder cappString = new StringBuilder().append(vida)
        .append(" / ")
        .append(ap);
    return cappString.toString();
  }

  public String getStringCapp15() {
    StringBuilder cappString = new StringBuilder().append(capp_vida_15)
        .append(" / ")
        .append(capp_ap_15);
    return cappString.toString();
  }

  public Double getPercent() {
    if (capp_vida_15 == 0 && capp_ap_15 == 0) {
      return Double.valueOf(1.5);
    }
    else if (capp_vida_15 <= 2 && capp_ap_15 <= 2) {
      return Double.valueOf(1.375);
    }
    else if (capp_vida_15 <= 6 && capp_ap_15 <= 6) {
      return Double.valueOf(1.3);
    }
    return Double.valueOf(1);
  }

}
