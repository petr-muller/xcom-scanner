package cz.larpy.xcom.fieldscanner_v3.ScannerResearch;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;

public class ResearchDetailActivity extends Activity {

  protected static final String RECORDTRACK = "TRACK";
  protected static final String RECORDLEVEL = "LEVEL";

  private String track;
  private int level;
  private SQLiteDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_research_detail);
  }

  @Override
  public void onResume() {
    super.onResume();
    ResearchManager manager = new ResearchManager(this);

    track = getIntent().getExtras().getString(RECORDTRACK);
    level = getIntent().getExtras().getInt(RECORDLEVEL);

    ScannerDbHelper helper = new ScannerDbHelper(this);
    db = helper.getReadableDatabase();

    ResearchRecord record = manager.getResearchRecord(track, level, db);

    TextView summary = (TextView) findViewById(R.id.researchSummary);
    summary.setText(record.getSummary());

    TextView fluff = (TextView) findViewById(R.id.researchFluff);
    fluff.setText(record.getFluff());

    TextView rules = (TextView) findViewById(R.id.researchRules);
    rules.setText(record.getRules());

    Button researchButton = (Button) findViewById(R.id.researchButton);
    if (manager.getResearchPoints() > 0 && record.isLocked()) {
      researchButton.setEnabled(true);
      researchButton.setOnClickListener(research);
    } else {
      researchButton.setEnabled(false);
    }
  }

  private final Button.OnClickListener research = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      ResearchManager manager = new ResearchManager(ResearchDetailActivity.this);
      manager.unlock(track, level, db);
      Intent i = new Intent(ResearchDetailActivity.this, ResearchActivity.class);
      startActivity(i);
    }
  };
}
