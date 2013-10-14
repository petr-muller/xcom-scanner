package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerParameters;
import cz.larpy.xcom.fieldscanner_v3.UpgradeActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade.ScannerUpgrade;

final public class ScannerActionScannerUpgrade extends ScannerAction {
  private final ScannerUpgrade upgrade;
  private boolean already_done = false;

  public ScannerActionScannerUpgrade(ScannerUpgrade pUpgrade) {
    super();
    upgrade = pUpgrade;
  }

  public ScannerActionScannerUpgrade(Parcel parcel) {
    super(parcel);
    upgrade = parcel.readParcelable(ScannerUpgrade.class.getClassLoader());
    already_done = parcel.readByte() != 0 ? true : false;
  }

  public static final Parcelable.Creator<ScannerActionScannerUpgrade> CREATOR = new Creator<ScannerActionScannerUpgrade>() {
    @Override
    public ScannerActionScannerUpgrade createFromParcel(Parcel source) {
      return new ScannerActionScannerUpgrade(source);
    }

    @Override
    public ScannerActionScannerUpgrade[] newArray(int size) {
      return new ScannerActionScannerUpgrade[size];
    }
  };

  @Override
  public String getMessage() {
    if (already_done) {
      return "Tento upgrade je již proveden";
    }

    return "Upgrade čtečky: " + upgrade.getMessage();
  }

  @Override
  public void execute(Context context) {
    ScannerParameters sp = ScannerParameters.load(context);
    if (!sp.upgrade(upgrade)) {
      already_done = true;
    }
    ScannerParameters.save(context, sp);

    super.execute(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(upgrade, flags);
    dest.writeByte((byte) (already_done ? 1 : 0));
  }

  @Override
  public Intent getIntent(Context context) {
    return new Intent(context, UpgradeActivity.class);
  }
}
