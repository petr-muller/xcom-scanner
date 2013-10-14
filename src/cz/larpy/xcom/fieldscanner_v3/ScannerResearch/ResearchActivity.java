package cz.larpy.xcom.fieldscanner_v3.ScannerResearch;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;

public class ResearchActivity extends Activity {
  private ResearchManager manager;
  private SQLiteDatabase database;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ScannerDbHelper helper = new ScannerDbHelper(this);
    database = helper.getWritableDatabase();
    setContentView(R.layout.activity_reseach);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    database.close();
  }

  @Override
  protected void onResume() {
    super.onResume();
    manager = new ResearchManager(this);
    TextView tv = (TextView) findViewById(R.id.researchPointCount);
    int rp = manager.getResearchPoints();
    tv.setText(String.valueOf(rp));
    if (rp > 0) {
      tv.setTextColor(getResources().getColor(R.color.green));
    } else {
      tv.setTextColor(getResources().getColor(R.color.red));
    }

    ResearchAdapter adapter = new ResearchAdapter(this, R.layout.research_item, manager.getVisibleResearch(database));
    ListView lv = (ListView) findViewById(R.id.researchList);
    lv.setAdapter(adapter);
    lv.setOnItemClickListener(mResearchClickedHandler);
  }

  private final AdapterView.OnItemClickListener mResearchClickedHandler = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
      Intent i = new Intent(ResearchActivity.this, ResearchDetailActivity.class);
      ResearchRecord record = (ResearchRecord) parent.getItemAtPosition(position);
      i.putExtra(ResearchDetailActivity.RECORDTRACK, record.getTrack());
      i.putExtra(ResearchDetailActivity.RECORDLEVEL, record.getLevel());
      startActivity(i);
    };
  };

}
