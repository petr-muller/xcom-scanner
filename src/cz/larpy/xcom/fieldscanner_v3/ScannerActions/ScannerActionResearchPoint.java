package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerResearch.ResearchActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerResearch.ResearchManager;

final public class ScannerActionResearchPoint extends ScannerAction {
  private final String pointId;
  private boolean already_done = false;

  public ScannerActionResearchPoint(String pId) {
    super();
    pointId = pId;
  }

  public ScannerActionResearchPoint(Parcel parcel) {
    super(parcel);
    pointId = parcel.readString();
    already_done = parcel.readByte() != 0 ? true : false;
  }

  public static final Parcelable.Creator<ScannerActionResearchPoint> CREATOR = new Creator<ScannerActionResearchPoint>() {
    @Override
    public ScannerActionResearchPoint createFromParcel(Parcel source) {
      return new ScannerActionResearchPoint(source);
    }

    @Override
    public ScannerActionResearchPoint[] newArray(int size) {
      return new ScannerActionResearchPoint[size];
    }
  };

  @Override
  public String getMessage() {
    if (already_done) {
      return "Analýza této technologie již byla provedena";
    }

    return "Analýza technologie poskytla dostatek dat pro výzkumnou sekci X-COM";
  }

  @Override
  public void execute(Context context) {
    ResearchManager rm = new ResearchManager(context);

    if (!rm.addResearchPoint(pointId)) {
      already_done = true;
    }

    super.execute(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(pointId);
    dest.writeByte((byte) (already_done ? 1 : 0));
  }

  @Override
  public Intent getIntent(Context context) {
    return new Intent(context, ResearchActivity.class);
  }
}
