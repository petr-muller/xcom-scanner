package cz.larpy.xcom.fieldscanner_v3.ScannerRecords;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;

public class RecordListActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record_list);

    ScannerDbHelper helper = new ScannerDbHelper(this);
    RecordManager manager = new RecordManager(this);
    ListView recordList = (ListView) findViewById(R.id.recordsList);

    List<ScannerRecord> records = manager.getUnlockedRecords(helper.getReadableDatabase());
    ArrayAdapter<ScannerRecord> adapter = new ArrayAdapter<ScannerRecord>(this, android.R.layout.simple_list_item_1,
        records);
    recordList.setAdapter(adapter);
    recordList.setOnItemClickListener(mRecordClickHandler);
  }

  private final AdapterView.OnItemClickListener mRecordClickHandler = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
      Intent i = new Intent(RecordListActivity.this, RecordDetailActivity.class);
      ScannerRecord record = (ScannerRecord) parent.getItemAtPosition(position);
      i.putExtra(RecordDetailActivity.RECORDID, record.getIdentifier());
      startActivity(i);
    };
  };
}
