
package android.michealcob.calculator.ui;

import android.app.FragmentTransaction;
import android.content.Context;
import android.michealcob.calculator.R;
import android.michealcob.calculator.db.Currency;
import android.michealcob.calculator.db.DBClient;
import android.michealcob.calculator.model.ExchangeEvent;
import android.michealcob.calculator.model.ResultEvent;
import android.michealcob.calculator.ui.dialog.CurrencyDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;



public class MainActivity extends AppCompatActivity {

    Double exValue;
    TextView mExText, mConverted, mCurrencyTo;
    EditText mBase;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        setContentView(R.layout.activity_main);

        mExText = findViewById(R.id.tv_exchange);
        mConverted = findViewById(R.id.tv_convert_to);
        mCurrencyTo = findViewById(R.id.tv_currency_to);

        mBase = findViewById(R.id.et_convert_from);

        mBase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mBase.getText().toString().length()>0 && mBase.getText().toString().length()<10)
                    //size as per your requirement
                {
                    String val = mBase.getText().toString().trim();
                    callCalculator(val);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CardView cardView = findViewById(R.id.cv_currency_to);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyDialog currencyDialog = new CurrencyDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                currencyDialog.show(ft, "here");
            }
        });

        getExchangeValue();

    }

    private void callCalculator(String val) {
        int valInt = Integer.parseInt(val);

        Double result = valInt * exValue;
        String resultString = String.valueOf(result);

        EventBus.getDefault().post(new ResultEvent(resultString));

    }

    private void getExchangeValue() {
        class GetExchange extends AsyncTask<Void, Void, List<Currency>> {

            @Override
            protected List<Currency> doInBackground(Void... voids) {
                List<Currency> currencyList = DBClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .currencyDao()
                        .getAll();
                return currencyList;
            }

            @Override
            protected void onPostExecute(List<Currency> currencyList) {
                super.onPostExecute(currencyList);
                if (currencyList.isEmpty()){
                    exValue = 1.12065;
                    EventBus.getDefault().post(new ExchangeEvent("USD"));
                }else{
                    int len = currencyList.size()-1;
                    Currency currency = currencyList.get(len);
                    exValue = currency.getExchangeAmount();
                    EventBus.getDefault().post(new ExchangeEvent(currency.getCurrencyName()));
                }
            }
        }

        GetExchange gt = new GetExchange();
        gt.execute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResultEvent event) {
        mConverted.setText(event.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExchangeEvent event) {
        mExText.setText(event.currencyName);
        mCurrencyTo.setText(event.currencyName);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        getExchangeValue();
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getExchangeValue();
    }

}
