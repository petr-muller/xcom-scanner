package cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class ScannerUpgrade implements Parcelable {
  public static ScannerUpgrade load(SharedPreferences preferences, int instId) {
    StringBuilder keyBuilder = new StringBuilder();
    keyBuilder.append(ScannerUpgrade.PREFKEY).append(SEP).append(instId);

    String record = preferences.getString(keyBuilder.toString(), null);
    String[] records = record.split(SEP, 2);
    String type = records[0];
    String prefString = records[1];

    if (type.equals(ScannerUpgradeSnifferSpeed.type)) {
      return new ScannerUpgradeSnifferSpeed(instId, prefString);
    }
    return null;
  }

  public static void save(SharedPreferences preferences, ScannerUpgrade pUpgrade) {
    StringBuilder keyBuilder = new StringBuilder();

    keyBuilder.append(PREFKEY).append(SEP).append(pUpgrade.getId());

    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(keyBuilder.toString(), pUpgrade.getType() + SEP + pUpgrade.asPreference());
    editor.commit();
  }

  public abstract String asPreference();

  public abstract String getType();

  static public final String SEP = ":";
  static public String PREFKEY = "UPGRADE";
  private final int id;

  protected ScannerUpgrade(int pId) {
    id = pId;
  }

  protected ScannerUpgrade(Parcel source) {
    id = source.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ScannerUpgrade other = (ScannerUpgrade) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  public abstract float processSnifferSpeed(float snifferSpeed, float snifferSpeedBase);

  public int getId() {
    return id;
  }

  abstract public String getMessage();

  public static final Parcelable.Creator<ScannerUpgrade> CREATOR = new Creator<ScannerUpgrade>() {
    @Override
    public ScannerUpgrade createFromParcel(Parcel source) {
      throw new AssertionError("ScannerUpgrade should not be directly instantiated");
    }

    @Override
    public ScannerUpgrade[] newArray(int size) {
      return new ScannerUpgrade[size];
    }
  };
}
