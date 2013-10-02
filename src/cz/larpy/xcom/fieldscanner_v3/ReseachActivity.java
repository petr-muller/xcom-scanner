package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ReseachActivity extends Activity {
  private ResearchManager manager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reseach);
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
  }
}
