package fi.jamk.k8760.sumcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void countButtonClicked(View view) {
        final EditText n1 = (EditText) findViewById(R.id.number1);
        Float number1 = Float.parseFloat(n1.getText().toString());
        final EditText n2 = (EditText) findViewById(R.id.number2);
        Float number2 = Float.parseFloat(n2.getText().toString());
        final TextView res = (TextView) findViewById(R.id.result);
        n1.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    n1.setText( "", TextView.BufferType.EDITABLE );
                    //n2.setText( "", TextView.BufferType.EDITABLE );
                    res.setText( "0", TextView.BufferType.EDITABLE );
                }
            }
        } );
        n2.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    //n1.setText( "", TextView.BufferType.EDITABLE );
                    n2.setText( "", TextView.BufferType.EDITABLE );
                    res.setText( "0", TextView.BufferType.EDITABLE );
                }
            }
        } );
        Float result = number1 + number2;
        res.setText(result.toString());
    }


}
