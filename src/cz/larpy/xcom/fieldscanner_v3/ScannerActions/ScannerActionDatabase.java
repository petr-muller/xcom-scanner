package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.HubActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;
import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.RecordManager;
import cz.larpy.xcom.fieldscanner_v3.ScannerResearch.ResearchManager;
import cz.larpy.xcom.fieldscanner_v3.ScannerSniffer.SnifferLocationManager;

public final class ScannerActionDatabase extends ScannerAction {
  private static String REPOPULATE = "REPOPULATE";

  public static final ScannerActionDatabase repopulate() {
    return new ScannerActionDatabase(REPOPULATE);
  }

  private final String command;

  private ScannerActionDatabase(String pCommand) {
    super();
    command = pCommand;
  }

  private ScannerActionDatabase(Parcel parcel) {
    super(parcel);
    command = parcel.readString();
  }

  public static final Parcelable.Creator<ScannerActionDatabase> CREATOR = new Creator<ScannerActionDatabase>() {
    @Override
    public ScannerActionDatabase createFromParcel(Parcel source) {
      return new ScannerActionDatabase(source);
    }

    @Override
    public ScannerActionDatabase[] newArray(int size) {
      return new ScannerActionDatabase[size];
    }
  };

  @Override
  public String getMessage() {
    if (command.equals(REPOPULATE)) {
      return "The database will be repopulated to the initial state";
    }
    return null;
  }

  @Override
  public void execute(Context context) {
    super.execute(context);
    if (command.equals(REPOPULATE)) {
      ScannerDbHelper dbHelper = new ScannerDbHelper(context);
      SQLiteDatabase db = dbHelper.getWritableDatabase();

      RecordManager recordManager = new RecordManager(context);
      recordManager.clear(db);
      recordManager.populate(db);

      ResearchManager researchManager = new ResearchManager(context);
      researchManager.clear(db);
      researchManager.populate(db);

      SnifferLocationManager locationManager = new SnifferLocationManager(context);
      locationManager.clear(db);
      locationManager.populate(db);

      db.close();
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(command);
  }

  @Override
  public Intent getIntent(Context context) {
    if (command.equals(REPOPULATE)) {
      return new Intent(context, HubActivity.class);
    }
    return null;
  }
}
