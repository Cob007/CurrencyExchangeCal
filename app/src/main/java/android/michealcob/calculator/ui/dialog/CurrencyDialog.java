package android.michealcob.calculator.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;

import android.michealcob.calculator.R;
import android.michealcob.calculator.db.Currency;
import android.michealcob.calculator.db.DBClient;
import android.michealcob.calculator.net.ApiClient;
import android.michealcob.calculator.net.ApiService;
import android.michealcob.calculator.net.response.LatestResponse;
import android.michealcob.calculator.ui.MainActivity;
import android.michealcob.calculator.ui.adapter.CurrencyAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.michealcob.calculator.model.Constant.KEY;


public class CurrencyDialog extends DialogFragment {

    public static final String TAG = "setRoute";

    ImageView mClose;
    RecyclerView mCountry;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    List<String> currencyName;
    List<Double> exchangeRate;

    CurrencyAdapter.ItemClickListener itemClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.currency_layout, container, false);

        itemClickListener = new CurrencyAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                final String cuName = currencyName.get(position);
                final Double exAmount = exchangeRate.get(position);

                Toast.makeText(getActivity(), " "+cuName+" "+exAmount, Toast.LENGTH_SHORT).show();

                class SaveTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        //creating a task
                        Currency currency = new Currency();
                        currency.setCurrencyName(cuName);
                        currency.setExchangeAmount(exAmount);

                        //adding to database
                        DBClient.getInstance(getActivity()).getAppDatabase()
                                .currencyDao()
                                .insert(currency);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(getActivity(), "Configured\n you can now close this dialog, thanks", Toast.LENGTH_LONG).show();
                    }
                }

                SaveTask st = new SaveTask();
                st.execute();
            }
        };


        ApiService apiService = ApiClient.getApi().create(ApiService.class);
        Call<LatestResponse> latestResponseCall = apiService.getLatest(KEY);
        latestResponseCall.enqueue(new Callback<LatestResponse>() {
            @Override
            public void onResponse(Call<LatestResponse> call, Response<LatestResponse> response) {
                if (response.isSuccessful()){
                    Log.v("Response", " " + response.body());
                    LatestResponse latestResponse = response.body();
                    JsonObject jsonObject = latestResponse.getRatesJs();
                    String jsonstring = jsonObject.toString();
                    try {
                        JSONObject resobj = new JSONObject(jsonstring);
                        Log.v("key", " "+ resobj.toString());
                        Iterator keys = resobj.keys();
                        currencyName = new ArrayList<>();
                        exchangeRate = new ArrayList<>();
                        while(keys.hasNext()) {
                            String key = (String)keys.next();
                            //populating latest currency name here
                            currencyName.add(key);
                            double val = resobj.getDouble(key);
                            //populating latest exchange rate here
                            exchangeRate.add(val);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    loaderAdapter();
                }
            }

            @Override
            public void onFailure(Call<LatestResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error" , Toast.LENGTH_SHORT).show();
            }
        });

        mClose = view.findViewById(R.id.iv_close);
        mCountry = view.findViewById(R.id.rv_country);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelUpload();
            }
        });





        return view;

    }

    private void loaderAdapter() {
        adapter = new CurrencyAdapter(currencyName, itemClickListener);
        layoutManager = new LinearLayoutManager(getActivity());
        mCountry.setLayoutManager(layoutManager);
        mCountry.setAdapter(adapter);
    }


    private void cancelUpload() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
