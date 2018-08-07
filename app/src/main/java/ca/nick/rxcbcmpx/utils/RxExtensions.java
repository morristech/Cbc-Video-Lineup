package ca.nick.rxcbcmpx.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class RxExtensions {
    private RxExtensions() {}

    public static CompletableTransformer applySchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
