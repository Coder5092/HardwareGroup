package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.function.Supplier;

public interface VariableGamepadInterface {
   final HashMap<String, Double> vals = new HashMap<>();
   ArrayList<String> vars = new ArrayList<>();
   FakeInt selectedVar = new FakeInt();

   abstract double getIncreaseDecrease();
   abstract int getIncreaseDecreaseVar();
   abstract void addToTelemetry(String k, String s);

   default void tick() {
      selectedVar.add(getIncreaseDecreaseVar());

      if (vars.size() - 1 < selectedVar.get()) selectedVar.set(vars.size() - 1);
      if (selectedVar.get() < 0) selectedVar.set(0);

      String key = vars.get(selectedVar.get());
      double val = vals.get(key);
      addToTelemetry("Variable", key);
      addToTelemetry("Value", String.valueOf(val));

      val += getIncreaseDecrease();
      vals.put(key, val);
   }

   default void addKey(String k, double v) {
      vals.put(k, v);
      vars.add(k);
   }
}
