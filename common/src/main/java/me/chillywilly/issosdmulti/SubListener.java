package me.chillywilly.issosdmulti;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.SubscriptionListener;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SubListener implements SubscriptionListener {
    @Override
    public void onClearSnapshot(@Nullable String itemName, int itemPos) {

    }

    @Override
    public void onCommandSecondLevelItemLostUpdates(int lostUpdates, @NonNull String key) {

    }

    @Override
    public void onCommandSecondLevelSubscriptionError(int code, @Nullable String message, String key) {

    }

    @Override
    public void onEndOfSnapshot(@Nullable String itemName, int itemPos) {

    }

    @Override
    public void onItemLostUpdates(@Nullable String itemName, int itemPos, int lostUpdates) {

    }

    @Override
    public void onItemUpdate(@NonNull ItemUpdate itemUpdate) {
        Issosdmulti.update(itemUpdate.getValue("Value"));
    }

    @Override
    public void onListenEnd() {

    }

    @Override
    public void onListenStart() {

    }

    @Override
    public void onSubscription() {

    }

    @Override
    public void onSubscriptionError(int code, @Nullable String message) {

    }

    @Override
    public void onUnsubscription() {

    }

    @Override
    public void onRealMaxFrequency(@Nullable String frequency) {

    }
}
