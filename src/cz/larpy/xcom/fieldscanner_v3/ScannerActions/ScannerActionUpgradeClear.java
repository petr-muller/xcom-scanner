package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerParameters;
import cz.larpy.xcom.fieldscanner_v3.UpgradeActivity;

final public class ScannerActionUpgradeClear extends ScannerAction {
  public ScannerActionUpgradeClear() {
    super();
  }

  public ScannerActionUpgradeClear(Parcel parcel) {
    super(parcel);
  }

  public static final Parcelable.Creator<ScannerActionUpgradeClear> CREATOR = new Creator<ScannerActionUpgradeClear>() {
    @Override
    public ScannerActionUpgradeClear createFromParcel(Parcel source) {
      return new ScannerActionUpgradeClear(source);
    }

    @Override
    public ScannerActionUpgradeClear[] newArray(int size) {
      return new ScannerActionUpgradeClear[size];
    }
  };

  @Override
  public String getMessage() {
    return "Všechny upgrady byly zrušeny";
  }

  @Override
  public void execute(Context context) {
    ScannerParameters.clear(context);
    super.execute(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  @Override
  public Intent getIntent(Context context) {
    return new Intent(context, UpgradeActivity.class);
  }
}
