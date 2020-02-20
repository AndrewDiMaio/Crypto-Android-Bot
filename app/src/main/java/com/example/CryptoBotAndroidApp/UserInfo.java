package com.example.CryptoBotAndroidApp;

class UserInfo {
    private String aKey = "default1";
    private String aSecret = "default2";
    private String passphrase = "default3";
    private String recipientEmail;

    public UserInfo(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    String getaKey() {
        return aKey;
    }

    String getaSecret() {
        return aSecret;
    }

    String getPassphrase() {
        return passphrase;
    }

    String getRecipientEmail() {
        return recipientEmail;
    }
}