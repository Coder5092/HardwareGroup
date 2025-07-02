package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;

public class VariableGamepadInterface {
  private HashMap<String, Double> doubles = null;
  private ArrayList<String> doubleKeys = null;
  private HashMap<String, Integer> ints = null;
  private ArrayList<String> intKeys = null;

  Gamepad gamepad;
  Telemetry telemetry;

  private int selectedMap = 0;
  private int selectedVar = 0;

  private boolean gamepadButtonPressed = false;

  /**
   * Creates a new <code>VariableGamepadInterface</code>. It allows a gamepad to control variables during runtime.
   * <b style="color: red;">WARNING: This is not for use during competition.</b>
   *
   * @param gamepad2 Gamepad #2
   * @param telemetryIn Telemetry as in the <code>OpMode</code>
   */
  public VariableGamepadInterface(Gamepad gamepad2, Telemetry telemetryIn) {
    gamepad = gamepad2;
    telemetry = telemetryIn;
  }

  /**
   * Binds strings <code>s</code> to integers <code>i</code>
   * @param s Variable names (<code>String</code> array)
   * @param d <code>Doubles</code> (array)
   */
  public void link(String[] s, double[] d) {
    if (s.length != d.length) throw new IllegalArgumentException("Arrays not the same length");

    if (doubles != null) {
      doubles = new HashMap<>();
      doubleKeys = new ArrayList<>();
    }
    assert doubles != null;

    for (int i = 0; i < s.length; i++) {
      doubles.put(s[i], d[i]);
      doubleKeys.add(s[i]);
    }
  }

  /**
   * Binds strings <code>s</code> to integers <code>i</code>
   * @param s Variable names (<code>String</code> array)
   * @param i <code>Integers</code> (array)
   */
  public void link(String[] s, int[] i) {
    if (s.length != i.length) throw new IllegalArgumentException("Arrays not the same length");

    if (ints != null) {
      ints = new HashMap<>();
      intKeys = new ArrayList<>();
    }
    assert ints != null;

    for (int j = 0; j < s.length; j++) {
      ints.put(s[j], i[j]);
      intKeys.add(s[j]);
    }
  }

  /**
   * Returns the <code>Double</code> bound to variable name in parameter <code>key</code>
   * @param key The variable name when assigned using <code>link</code>.
   * @return The <code>double</code> associated with the key.
   * @see #link(String[], double[])
   */
  public double retrieveDouble(String key) {
    try {
      return doubles.get(key);
    } catch (NullPointerException e) {
      return 0;
    }
  }

  /**
   * Returns the <code>Integer</code> bound to variable name in parameter <code>key</code>
   * @param key The variable name when assigned using <code>link</code>.
   * @return The <code>int</code> associated with the key.
   * @see #link(String[], int[])
   */
  public int retrieveInt(String key) {
    try {
      return ints.get(key);
    } catch (NullPointerException e) {
      return 0;
    }
  }

  public void tick() {
    String currentKey = (selectedMap == 0 ? intKeys : doubleKeys).get(selectedVar);

    if (gamepadButtonPressed) {
      if (! (gamepad.x || gamepad.y || gamepad.a || gamepad.b || gamepad.dpad_up || gamepad.dpad_down)) gamepadButtonPressed = false;
    } else if (gamepad.a || gamepad.b) {
      gamepadButtonPressed = true;
      selectedMap = 1 - selectedMap;
    } else if (gamepad.x) {
      gamepadButtonPressed = true;
      selectedVar += 1;
    } else if (gamepad.y) {
      gamepadButtonPressed = true;
      selectedVar -= 1;
    }

    HashMap<String, ? extends Number> currentMap = (selectedMap == 0 ? ints : doubles);

    if (selectedVar > currentMap.size() - 1) selectedVar = currentMap.size() - 1;
    else if (selectedVar < 0) selectedVar = 0;

    if (gamepadButtonPressed) {}
    else if (gamepad.dpad_up) {
      if (selectedMap == 0) {
        ints.put(currentKey, ints.get(currentKey) + 1);
      } else {
        doubles.put(currentKey, doubles.get(currentKey) + 0.01);
      }
    } else if (gamepad.dpad_down) {
      if (selectedMap == 0) {
        ints.put(currentKey, ints.get(currentKey) - 1);
      } else {
        doubles.put(currentKey, doubles.get(currentKey) - 0.01);
      }
    }


    telemetry.addLine();
    telemetry.addLine("---------- VariableGamepadInterface ----------");
    telemetry.addData("Selected variable map: ", (selectedMap == 0 ? "Integers" : "Doubles"));
    telemetry.addData("Selected variable: ", currentKey);
    if (selectedMap == 0) telemetry.addData("Value of variable: ", ints.get(currentKey));
    else telemetry.addData("Value of variable: ", doubles.get(currentKey));
    telemetry.update();
  }
}
