package fi.jamk.k8760.basiccontrols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.login); // add stings to control
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                new String[]{"Pasi","Juha","Kari","Jouni","Esa","Hannu"});
        actv.setAdapter(aa);
    }

    public void selectButtonClicked(View view) {
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.login);
        String text1 = actv.getText().toString();
        TextView pass = (TextView) findViewById(R.id.password);
        String text2 = pass.getText().toString();
        Toast.makeText(getApplicationContext(), text1 + " " + text2, Toast.LENGTH_SHORT).show();
    }
}
