package com.example.CryptoBotAndroidApp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.coinbasepro.CoinbaseProExchange;

public class CryptoBotAndroidApp extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.chart).setVisibility(View.GONE);
        findViewById(R.id.textView2).setVisibility(View.GONE);
        findViewById(R.id.emailResult).setVisibility(View.GONE);
        findViewById(R.id.currentPriceVar).setVisibility(View.GONE);
        findViewById(R.id.priceTag).setVisibility(View.GONE);
        Button b = findViewById(R.id.submit);
        WebView w = findViewById(R.id.chart);
        WebSettings webSettings = w.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Load the asset file by URL. Note the 3 slashes "file:///".
        w.loadUrl("file:///android_asset/chart.html");
        b.setOnClickListener(v -> {
            TextView t = findViewById(R.id.emailResult);
            EditText s = findViewById(R.id.emailText);
            t.setText(s.getEditableText().toString());
            findViewById(R.id.emailText).setVisibility(View.GONE);
            findViewById(R.id.submit).setVisibility(View.GONE);
            findViewById(R.id.chart).setVisibility(View.VISIBLE);
            UserInfo userInfo = new UserInfo(s.getEditableText().toString());
            t.setText(userInfo.getRecipientEmail());
            TextView info = findViewById(R.id.textView2);
            TextView price = findViewById(R.id.currentPriceVar);
            findViewById(R.id.emailResult).setVisibility(View.VISIBLE);
            findViewById(R.id.currentPriceVar).setVisibility(View.VISIBLE);
            findViewById(R.id.priceTag).setVisibility(View.VISIBLE);
            info.setText("LOADING");
            price.setText("Getting Current Price");
            findViewById(R.id.textView2).setVisibility(View.VISIBLE);

            Thread thread = new Thread(() -> {
                try  {
                    Exchange exchange = generateExchange(userInfo);
                    Thread alert = new Thread(new Alerts(exchange, userInfo, info, price));
                    alert.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });

    }
    static Exchange generateExchange(UserInfo userInfo){
        ExchangeSpecification specification = new ExchangeSpecification(CoinbaseProExchange.class);
        specification.setApiKey(userInfo.getaKey());
        specification.setSecretKey(userInfo.getaSecret());
        specification.setExchangeSpecificParametersItem("passphrase", userInfo.getPassphrase());
        return ExchangeFactory.INSTANCE.createExchange(specification);
    }

}
