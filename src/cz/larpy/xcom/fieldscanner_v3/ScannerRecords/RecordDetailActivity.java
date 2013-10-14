package cz.larpy.xcom.fieldscanner_v3.ScannerRecords;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;

public class RecordDetailActivity extends Activity {

  protected static final String RECORDID = "RECORD ID";
  private MediaPlayer player = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record_detail);

    TextView summary = (TextView) findViewById(R.id.recordDetailSummary);
    TextView text = (TextView) findViewById(R.id.recordDetailText);
    Button sound = (Button) findViewById(R.id.recordDetailSound);
    ImageView image = (ImageView) findViewById(R.id.recodDetailImage);

    RecordManager manager = new RecordManager(this);
    String identifier = getIntent().getExtras().getString(RECORDID);

    ScannerDbHelper helper = new ScannerDbHelper(this);

    ScannerRecord record = manager.getRecord(helper.getReadableDatabase(), identifier);

    summary.setText(record.getSummary());
    if (record.hasText()) {
      text.setVisibility(View.VISIBLE);
      text.setText(record.getText(this));
    } else {
      text.setVisibility(View.GONE);
    }

    if (record.hasImage()) {
      image.setVisibility(View.VISIBLE);
      image.setImageDrawable(record.getDrawable(this));
    } else {
      image.setVisibility(View.GONE);
    }

    if (record.hasSound()) {
      sound.setVisibility(View.VISIBLE);
      sound.setOnClickListener(soundPlay);
      player = record.getSoundPlayer(this);
    } else {
      sound.setVisibility(View.GONE);
    }
  }

  private final Button.OnClickListener soundPlay = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      player.start();
    }
  };
}
