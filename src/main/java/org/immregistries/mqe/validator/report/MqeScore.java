package org.immregistries.mqe.validator.report;

public class MqeScore {

  private int potential = 0;
  private int scored = 0;

  @Override
  public String toString() {
    return "MqeScore [potential=" + potential + ", scored=" + scored + "]";
  }

  public int getPotential() {
    return potential;
  }

  public void setPotential(int potential) {
    this.potential = potential;
  }

  public int getScored() {
    return scored;
  }

  public void setScored(int scored) {
    this.scored = scored;
  }
}
