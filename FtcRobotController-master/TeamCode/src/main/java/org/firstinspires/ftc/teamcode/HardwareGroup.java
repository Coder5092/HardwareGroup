package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class HardwareGroup {
  public static class Group<T extends HardwareDevice> {
    public T[] hardware;
    protected boolean[] reversed;
    protected T firstObj;

    @SafeVarargs
    public Group(T... objects) {
      hardware = objects.clone();
      prepareValues();
    }

    public Group(Class<T> hardwareClass, HardwareMap map, String... names) {
      ArrayList<T> list = new ArrayList<>();
      for (String name : names) {
        list.add(map.get(hardwareClass, name));
      }
      hardware = (T[]) list.toArray();
      prepareValues();
    }

    private Group(HardwareMap.DeviceMapping<T> mapping, String... names) {
      ArrayList<T> list = new ArrayList<>();
      for (String name : names) {
        list.add(mapping.get(name));
      }
      hardware = (T[]) list.toArray();
      prepareValues();
    }

    protected void prepareValues() {
      firstObj = hardware[0];
      reversed = new boolean[hardware.length];
      Arrays.fill(reversed, true);
    }

    // Servos begin here

    public void setPosition(double d) {
      if (firstObj instanceof Servo) {
        for (int i = 0; i < hardware.length; i++) {
          ((Servo) hardware[i]).setPosition(reversed[i] ? 1 - d : d);
        }
      } else unsupported("Servo");
    }

    public void pwm(boolean enable) {
      if (firstObj instanceof Servo) {
        if (enable) for (T obj : hardware) ((Servo) obj).getController().pwmEnable();
        else for (T obj : hardware) ((Servo) obj).getController().pwmDisable();
      } else unsupported("Servo");
    }

    // Motors begin here

    public void setPower(double d) {
      if (firstObj instanceof DcMotorEx) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotorEx) hardware[i]).setPower(reversed[i] ? -d : d);
        }
      } else if (firstObj instanceof DcMotor) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotor) hardware[i]).setPower(reversed[i] ? -d : d);
        }
      } else if (firstObj instanceof DcMotorSimple) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotorSimple) hardware[i]).setPower(reversed[i] ? -d : d);
        }
      } else unsupported("DcMotorSimple");
    }

    public void setMode(DcMotor.RunMode mode) {
      if (firstObj instanceof DcMotorEx) for (T obj : hardware) ((DcMotorEx) obj).setMode(mode);
      else if (firstObj instanceof DcMotor) for (T obj : hardware) ((DcMotorEx) obj).setMode(mode);
      else unsupported("DcMotor");
    }

    public void setTargetPosition(int p) {
      if (firstObj instanceof DcMotorEx)
        for (T obj : hardware) ((DcMotorEx) obj).setTargetPosition(p);
      else if (firstObj instanceof DcMotor)
        for (T obj : hardware) ((DcMotorEx) obj).setTargetPosition(p);
      else unsupported("DcMotor");
    }

    public void setDirection(DcMotor.Direction dir) {
      if (firstObj instanceof DcMotorEx) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotorEx) hardware[i]).setDirection(reversed[i] ? dir.inverted() : dir);
        }
      } else if (firstObj instanceof DcMotor) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotor) hardware[i]).setDirection(reversed[i] ? dir.inverted() : dir);
        }
      } else if (firstObj instanceof DcMotorSimple) {
        for (int i = 0; i < hardware.length; i++) {
          ((DcMotorSimple) hardware[i]).setDirection(reversed[i] ? dir.inverted() : dir);
        }
      } else unsupported("DcMotorSimple");
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zpb) {
      if (firstObj instanceof DcMotorEx)
        for (T obj : hardware) ((DcMotorEx) obj).setZeroPowerBehavior(zpb);
      else if (firstObj instanceof DcMotor)
        for (T obj : hardware) ((DcMotorEx) obj).setZeroPowerBehavior(zpb);
      else unsupported("DcMotor");
    }

    // Touch sensors here
    public boolean[] isPressed() {
      if (firstObj instanceof TouchSensor) {
        boolean[] results = new boolean[hardware.length];
        for (int i = 0; i < hardware.length; i++) {
          results[i] = ((TouchSensor) hardware[i]).isPressed();
          if (reversed[i]) results[i] = !results[i];
        }
        return results;
      } else unsupported("TouchSensor");
      return null; // won't be reached, but will be required for compilation
    }

    // color sensors here
    public void led(boolean enable) {
      if (firstObj instanceof ColorSensor) {
        if (enable) for (T obj : hardware) ((ColorSensor) obj).enableLed(true);
        else for (T obj : hardware) ((ColorSensor) obj).enableLed(false);
      } else unsupported("ColorSensor");
    }

    public int[] red() {
      if (firstObj instanceof TouchSensor) {
        int[] results = new int[hardware.length];
        for (int i = 0; i < hardware.length; i++) results[i] = ((ColorSensor) hardware[i]).red();
        return results;
      } else unsupported("ColorSensor");
      return null; // won't be reached, but will be required for compilation
    }

    public int[] blue() {
      if (firstObj instanceof TouchSensor) {
        int[] results = new int[hardware.length];
        for (int i = 0; i < hardware.length; i++) results[i] = ((ColorSensor) hardware[i]).blue();
        return results;
      } else unsupported("ColorSensor");
      return null; // won't be reached, but will be required for compilation
    }

    public int[] green() {
      if (firstObj instanceof TouchSensor) {
        int[] results = new int[hardware.length];
        for (int i = 0; i < hardware.length; i++) results[i] = ((ColorSensor) hardware[i]).green();
        return results;
      } else unsupported("TouchSensor");
      return null; // won't be reached, but will be required for compilation
    }

    private void unsupported(String type) {
      throw new UnsupportedOperationException(firstObj.getClass().getName() + " is not " + type + ".");
    }
  }

  public static final class AlternatePair<T extends HardwareDevice> extends Group<T> {

    @Override
    protected void prepareValues() {
      super.prepareValues();
      reversed = new boolean[] {false, true};
    }

    public AlternatePair(T o1, T o2) {
      ArrayList<T> list = new ArrayList<>();
      list.add(o1);
      list.add(o2);
      hardware = (T[]) list.toArray();
      prepareValues();
    }

    public AlternatePair(Class<T> hardwareClass, HardwareMap map, String name1, String name2) {
      ArrayList<T> list = new ArrayList<>();
      list.add(map.get(hardwareClass, name1));
      list.add(map.get(hardwareClass, name2));
      hardware = (T[]) list.toArray();
      prepareValues();
    }

    private AlternatePair(HardwareMap.DeviceMapping<T> mapping, String name1, String name2) {
      ArrayList<T> list = new ArrayList<>();
      list.add(mapping.get(name1));
      list.add(mapping.get(name2));
      hardware = (T[]) list.toArray();
      prepareValues();
    }
  }
}
