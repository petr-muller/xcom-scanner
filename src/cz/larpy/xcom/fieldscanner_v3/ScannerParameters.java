package cz.larpy.xcom.fieldscanner_v3;

import java.util.HashSet;

import android.content.Context;
import android.content.SharedPreferences;
import cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade.ScannerUpgrade;

public final class ScannerParameters {
  private static final String RESOURCE_FILE = "ScannerParameters";
  private static final String UPGRADE_LIST_KEY = "UPGRADES";
  private static final String RESEARCH_KEY = "RESEARCH";
  public static final double DISTANCE_THRESHOLD = 50.0;

  public static void save(Context pContext, ScannerParameters parameters) {
    SharedPreferences preferences = pContext.getSharedPreferences(RESOURCE_FILE, Context.MODE_PRIVATE);
    StringBuilder sb = new StringBuilder();
    String sep = "";

    for (ScannerUpgrade upgrade : parameters.upgrades) {
      ScannerUpgrade.save(preferences, upgrade);
      sb.append(sep).append(upgrade.getId());
      sep = ",";
    }

    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(UPGRADE_LIST_KEY, sb.toString());
    editor.commit();
  }

  public static ScannerParameters load(Context pContext) {
    SharedPreferences preferences = pContext.getSharedPreferences(RESOURCE_FILE, Context.MODE_PRIVATE);
    ScannerParameters parameters = new ScannerParameters();

    if (preferences.contains(UPGRADE_LIST_KEY)) {
      String[] upgrade_ids = preferences.getString(UPGRADE_LIST_KEY, null).split(",");
      for (int i = 0; i < upgrade_ids.length; i++) {
        parameters.upgrades.add(ScannerUpgrade.load(preferences, Integer.valueOf(upgrade_ids[i])));
      }
    }

    return parameters;
  }

  public static void clear(Context pContext) {
    SharedPreferences preferences = pContext.getSharedPreferences(RESOURCE_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();

    editor.clear();
    editor.commit();
  }

  private ScannerParameters() {
  };

  private final float snifferSpeedBase = 10;
  private float snifferSpeed;

  private boolean cacheValid = false;

  private final HashSet<ScannerUpgrade> upgrades = new HashSet<ScannerUpgrade>();

  public float getSnifferSpeed() {
    if (!cacheValid)
      applyUpgrades();
    return snifferSpeed;
  }

  private void applyUpgrades() {
    snifferSpeed = snifferSpeedBase;
    for (ScannerUpgrade upgrade : upgrades) {
      snifferSpeed = upgrade.processSnifferSpeed(snifferSpeed, snifferSpeedBase);
    }
    cacheValid = true;
  }

  public boolean upgrade(ScannerUpgrade pUpgrade) {
    if (upgrades.contains(pUpgrade)) {
      return false;
    }
    upgrades.add(pUpgrade);
    cacheValid = false;
    return true;
  }

  public ScannerUpgrade[] getUpgrades() {
    return upgrades.toArray(new ScannerUpgrade[upgrades.size()]);
  }
}
