package com.example.CryptoBotAndroidApp;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import org.knowm.xchange.Exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;

class BreakCheck extends Activity {
    private BigDecimal price = BigDecimal.valueOf(5000);
    private BigDecimal min = BigDecimal.valueOf(500);
    private BigDecimal max = BigDecimal.valueOf(10000);
    private Exchange exchange;
    private TextView textView;

    Exchange getExchange() {
        return exchange;
    }

    void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    private void setTextView(TextView textView) {
        this.textView = textView;
    }

    BigDecimal getPrice() {
        return price;
    }

    void setPrice(BigDecimal price) {
        this.price = price;
    }

    BigDecimal getMin() {
        return min;
    }

    void setMin(BigDecimal min) {
        this.min = min;
    }

    BigDecimal getMax() {
        return max;
    }

    void setMax(BigDecimal max) {
        this.max = max;
    }

    String breakCheck() throws IOException {
        setPrice(exchange.getMarketDataService().getTicker(BTC_USD).getLast());
        setMin(exchange.getMarketDataService().getTicker(BTC_USD).getLow());
        setMax(exchange.getMarketDataService().getTicker(BTC_USD).getHigh());
        setExchange(exchange);
        return breakCheck();
    }

    String breakCheck(Exchange exchange, TextView textView, TextView priceVar) throws IOException {
        setPrice(exchange.getMarketDataService().getTicker(BTC_USD).getLast());
        setMin(exchange.getMarketDataService().getTicker(BTC_USD).getLow());
        setMax(exchange.getMarketDataService().getTicker(BTC_USD).getHigh());
        setExchange(exchange);
        if (this.price.compareTo(min) > 0 && this.price.compareTo(max) < 0) {
            setMin(exchange.getMarketDataService().getTicker(BTC_USD).getLow());
            setMax(exchange.getMarketDataService().getTicker(BTC_USD).getHigh());
            this.price = this.exchange.getMarketDataService().getTicker(BTC_USD).getLast();
            try {
                Thread.sleep(2500);
                BigDecimal volume = this.exchange.getMarketDataService().getTicker(BTC_USD).getVolume();
                Long volumeInDollar = volume.longValue()* this.price.longValue();
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                String volumeInDollarString = numberFormat.format(volumeInDollar);
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String sss = String.format("24 Hour Low: |    Current:    |    24 Hour High: \n%s       <    %s     <    %s%n", min.floatValue(), this.price.floatValue(), max.floatValue());
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        textView.setText(sss);
                        priceVar.setText("$" + price.toString());
                    }
                });
                System.out.printf("24 Hour Low: |    Current:    |    24 Hour High: \n%s       <    %s     <    %s%n", min.floatValue(), this.price.floatValue(), max.floatValue());
                System.out.printf("Current Price: $%s%n", this.price);
                System.out.printf("24 Hour Volume: %s Bitcoin Traded or $%s USD%n", volume.floatValue(), volumeInDollarString);
                System.out.println("************************************************");
                return "OK";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Alerts.status(this.price, min, max);
    }
}