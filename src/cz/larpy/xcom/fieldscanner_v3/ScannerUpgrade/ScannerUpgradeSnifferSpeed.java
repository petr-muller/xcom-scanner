package cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade;

import android.os.Parcel;
import android.os.Parcelable;

public final class ScannerUpgradeSnifferSpeed extends ScannerUpgrade {
  static public final String type = "SNIFFERSPEED";

  private final float modifier;

  public ScannerUpgradeSnifferSpeed(int pId, float pModifier) {
    super(pId);
    modifier = pModifier;
  }

  public ScannerUpgradeSnifferSpeed(Parcel source) {
    super(source);
    modifier = source.readFloat();
  }

  public ScannerUpgradeSnifferSpeed(int pId, String pPreference) {
    super(pId);
    modifier = Float.valueOf(pPreference);
  }

  @Override
  public float processSnifferSpeed(float pSnifferSpeed, float pSnifferSpeedBase) {
    return pSnifferSpeed + (pSnifferSpeedBase * modifier);
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("Rychlost snifferu zvýšena o ");
    sb.append((Float.valueOf(modifier * 100)).intValue());
    sb.append("%");

    return sb.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeFloat(modifier);
  }

  public static final Parcelable.Creator<ScannerUpgradeSnifferSpeed> CREATOR = new Creator<ScannerUpgradeSnifferSpeed>() {
    @Override
    public ScannerUpgradeSnifferSpeed createFromParcel(Parcel source) {
      return new ScannerUpgradeSnifferSpeed(source);
    }

    @Override
    public ScannerUpgradeSnifferSpeed[] newArray(int size) {
      return new ScannerUpgradeSnifferSpeed[size];
    }
  };

  @Override
  public String toString() {
    return "Upgrade rychlosti snifferu: (modifikátor=" + modifier + ")";
  }

  @Override
  public String asPreference() {
    return new StringBuilder().append(modifier).toString();
  }

  @Override
  public String getType() {
    return type;
  }
}
