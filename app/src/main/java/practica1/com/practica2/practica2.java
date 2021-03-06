package practica1.com.practica2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class practica2 extends AppCompatActivity
{
    private double preu;
    private double estalvis;
    private double plas;
    double euribor;
    double diferencial;

    private EditText vistaPreu;
    private EditText vistaEstalvis;
    private EditText vistaPlas;
    private EditText vistaEuribor;
    private EditText vistaDiferencial;

    private SeekBar vistaSeekBarPlas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practica2);

        vistaPreu = (EditText) findViewById(R.id.ETpreu);
        vistaEstalvis = (EditText) findViewById(R.id.ETestalvis);
        vistaPlas = (EditText) findViewById(R.id.ETplas);
        vistaEuribor = (EditText) findViewById(R.id.ETeuribor);
        vistaDiferencial = (EditText) findViewById(R.id.ETdiferencial);

        TextWatcher myTextWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {calcula();}
        };

        TextWatcher myTextWatcher2 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s)
            {
                calcula();
                try{ vistaSeekBarPlas.setProgress(Integer.parseInt(vistaPlas.getText().toString())); }
                catch (Exception seekBarE){ vistaSeekBarPlas.setProgress(0); }
            }
        };

        SeekBar.OnSeekBarChangeListener mySeekBarListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                vistaPlas.setText(String.valueOf(vistaSeekBarPlas.getProgress()));
                calcula();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        vistaPreu.addTextChangedListener(myTextWatcher);
        vistaEstalvis.addTextChangedListener(myTextWatcher);
        vistaPlas.addTextChangedListener(myTextWatcher2);
        vistaEuribor.addTextChangedListener(myTextWatcher);
        vistaDiferencial.addTextChangedListener(myTextWatcher);

        vistaSeekBarPlas = (SeekBar) findViewById(R.id.SBplas);
        vistaSeekBarPlas.setOnSeekBarChangeListener(mySeekBarListener);

        calcula();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practica2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick1(View view)
    {
        calcula();
    }

    private void calcula()
    {
        Calcular calcular = new Calcular().invoke();
        double mes = calcular.getMes();
        double total = calcular.getTotal();
        mostrar(mes, total);
    }

    private void mostrar(double mes, double total)
    {
        TextView mensual = (TextView) findViewById(R.id.TVmes);
        TextView anual = (TextView) findViewById(R.id.TVtotal);
        mensual.setText("Mes: " +String.valueOf(mes));
        anual.setText("Total: "+String.valueOf(total));
    }

    private class Calcular
    {
        private double mes;
        private double total;

        public double getMes()
        {
            return mes;
        }
        public double getTotal()
        {
            return total;
        }

        public Calcular invoke()
        {
            try { preu = Double.parseDouble(vistaPreu.getText().toString());}
            catch(Exception preuE) { preu=0; }

            try { estalvis = Double.parseDouble(vistaEstalvis.getText().toString());}
            catch(Exception estalvisE) { estalvis=0; }

            try {  plas = Double.parseDouble(String.valueOf(vistaSeekBarPlas.getProgress()));}
            catch(Exception plasE) { plas=0; }

            try { euribor = Double.parseDouble(vistaEuribor.getText().toString());}
            catch(Exception euriborE) { euribor=0; }

            try {  diferencial = Double.parseDouble(vistaDiferencial.getText().toString());}
            catch(Exception diferencialE) { diferencial=0; }

            double interes=(euribor+diferencial)/12;
            double calc1=((preu-estalvis)*interes);
            double calc2= 100*(1-(Math.pow(1+((interes/100)), -(plas*12))));
            mes = calc1 / calc2;
            total = mes * 12 * plas;
            return this;
        }
    }
}
