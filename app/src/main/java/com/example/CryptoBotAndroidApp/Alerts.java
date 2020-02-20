package com.example.CryptoBotAndroidApp;
import android.os.Looper;
import android.widget.TextView;

import org.knowm.xchange.Exchange;

import java.io.IOException;
import java.math.BigDecimal;

import javax.mail.MessagingException;

import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;

public class Alerts implements Runnable{
    private Exchange exchange;
    private UserInfo user;
    private TextView textView;
    private TextView price;

    public Alerts(Exchange exchange, UserInfo user, TextView info, TextView price) {
        this.exchange = exchange;
        this.user = user;
        this.textView = info;
        this.price = price;
    }

    @Override
    public void run() {
        try {
            System.out.println(exchange.getExchangeMetaData().getCurrencies());
            Looper.prepare();
            runProgram(exchange, user, textView, price);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void runProgram(Exchange exchange, UserInfo user, TextView textView, TextView price) throws IOException {
        String status = "OK";
        BreakCheck breakCheck = new BreakCheck();

        while (status.equals("OK")){
            status = breakCheck.breakCheck(exchange, textView, price);
        }

        if (status.equals("BREAKDOWN")){
            generateAlert("BREAKDOWN", exchange, user.getRecipientEmail());
        } else generateAlert("BREAKOUT", exchange, user.getRecipientEmail());
        runProgram(exchange, user, textView, price);
    }

    static void generateAlert(String status, Exchange exchange, String recipientEmail)throws IOException{
        String username = "INSERT_USERNAME"; //Enter a valid Gmail account for emails to be sent from
        String password = "INSERT_PASSWORD";
        BigDecimal price;

        int notified = 0;
        while (notified < 1) {
            try {
                price = exchange.getMarketDataService().getTicker(BTC_USD).getLast();
                GoogleMail.Send(username, password, recipientEmail, "BITCOIN TECHNICAL ALERT!!", "BTC ALERT TECHNICAL " + status);
                System.out.println(price + " " + status);
                Thread.sleep(30000);
                notified++;
            } catch (InterruptedException | MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    static String status(BigDecimal price, BigDecimal min, BigDecimal max) {
        if (price.doubleValue() < min.floatValue()){
            System.out.println(price + " " + max);
            return "BREAKDOWN";
        } else {
            System.out.println(price + " " + max);
            return "BREAKOUT";
        }
    }
}
