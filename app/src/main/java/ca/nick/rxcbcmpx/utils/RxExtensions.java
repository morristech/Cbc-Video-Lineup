package ca.nick.rxcbcmpx.utils;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class RxExtensions {
    private RxExtensions() {}

    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return flowable -> flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
