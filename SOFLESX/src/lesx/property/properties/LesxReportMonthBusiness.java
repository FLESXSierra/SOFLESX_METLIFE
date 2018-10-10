package lesx.property.properties;

public class LesxReportMonthBusiness extends LesxComponent {

  Integer month;
  Long vida = 0L;
  Long ap = 0L;
  Long NBS;
  Long comision;
  Long capp = 0L;
  Integer currentCapp = 0;

  public LesxReportMonthBusiness(Integer month) {
    this.month = month;
  }

  public Long getVida() {
    return vida;
  }

  public void setVida(Long vida) {
    this.vida = vida;
    capp = ap + vida;
  }

  public Long getAp() {
    return ap;
  }

  public void setAp(Long ap) {
    this.ap = ap;
    capp = ap + vida;
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

  public long getCapp() {
    return capp;
  }

  public String getStringCapp() {
    StringBuilder cappString = new StringBuilder().append(capp)
        .append("/")
        .append(capp);
    return cappString.toString();
  }

  public void setCurrentCAPP(int capp) {
    currentCapp = capp;
  }

  public int getCurrentCAPP() {
    return currentCapp;
  }

  public Double getPercent() {
    if (currentCapp >= 15) {
      return Double.valueOf(1.5);
    }
    else if (currentCapp >= 9) {
      return Double.valueOf(1.375);
    }
    else if (currentCapp >= 5) {
      return Double.valueOf(1.3);
    }
    return Double.valueOf(1);
  }

}
