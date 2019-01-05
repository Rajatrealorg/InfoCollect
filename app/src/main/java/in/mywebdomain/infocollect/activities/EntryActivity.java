package in.mywebdomain.infocollect.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.mywebdomain.infocollect.R;

public class EntryActivity extends AppCompatActivity {
    EditText name, pno, dob;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        name = findViewById(R.id.entry_name);
        pno = findViewById(R.id.entry_pno);
        dob = findViewById(R.id.entry_dob);

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        // TODO Add intent if person gets back from LocationActivity

        dob.setOnClickListener(v -> {
            if(dpd == null) {
                dpd = new DatePickerDialog(EntryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - (86400000));
                dpd.show();
            } else if(!dpd.isShowing()) {
//                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - (86400000));
                dpd.show();
            }
        });

        dob.setOnFocusChangeListener((view, b) -> {
            if(b)
                if(dpd == null) {
                    dpd = new DatePickerDialog(EntryActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                } else if(!dpd.isShowing()) {
                    dpd.show();
                }
        });
    }

    @Override
    protected void onResume() { if(hasIntention()){ fillData(); } super.onResume(); }
    private void fillData() { name.setText(getIntent().getStringExtra("name")); pno.setText(getIntent().getStringExtra("pno")); dob.setText(getIntent().getStringExtra("dob"));}
    private boolean hasIntention() { return getIntent().hasExtra("name"); }


    private boolean inputIsValid() {
        boolean isValid = true;
        if(name.getText().length()<3){
            isValid = false;
            Toast.makeText(this, "Name should have atleast 3 Letters", Toast.LENGTH_SHORT).show();
        } else if(pno.getText().length()!=10){
            isValid = false;
            Toast.makeText(this, "Phone Number should have 10 Numbers", Toast.LENGTH_SHORT).show();
        } else if(dob.getText().length()<4){
            isValid = false;
            Toast.makeText(this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
            // TODO Additional Age Verification using birth year
            // Date should not be greater than current date
        }
        return isValid;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.entry_menu_back) {
            finish();
        } else if(item.getItemId() == R.id.entry_menu_next) {
            if(inputIsValid()){
                String name1 = name.getText().toString();
                String pno1 = pno.getText().toString();
                String dob1 = dob.getText().toString();
                Intent intent = new Intent(EntryActivity.this, LocationActivity.class);
                intent.putExtra("name", name1);
                intent.putExtra("pno", pno1);
                intent.putExtra("dob", dob1);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
