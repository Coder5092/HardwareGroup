package org.firstinspires.ftc.teamcode;

public class FakeInt {
  public FakeInt() {}
  public FakeInt(int x) {
    val = x;
  }
  private int val = 0;

  public void add(int x) {
    val += x;
  }

  public void minus(int x) {
    val -= x;
  }

  public void mult(int x) {
    val *= x;
  }

  public void div(int x) {
    val /= x;
  }

  public int get() {
    return val;
  }

  public void set(int x) {
    val = x;
  }
}
