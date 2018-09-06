package lesx.property.properties;

import lesx.utils.LesxPair;

public class LesxReportMonthBusiness extends LesxComponent {

  Integer month;
  Long vida = 0L;
  Long ap = 0L;
  Long NBS;
  Long comision;
  Long capp = 0L;
  LesxPair<Boolean, Boolean> achievedCAPP = LesxPair.of(false, false);

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

  public void setAchievedCAPP(boolean achieved) {
    achievedCAPP = LesxPair.of(true, achieved);
  }

  public LesxPair<Boolean, Boolean> getAchievedCAPP() {
    return achievedCAPP;
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

}
