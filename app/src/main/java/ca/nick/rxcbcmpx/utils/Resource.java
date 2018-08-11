package ca.nick.rxcbcmpx.utils;

import android.support.annotation.Nullable;

public class Resource<T> {

    private final Status status;
    @Nullable private final T data;
    @Nullable private final Throwable error;

    private Resource(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable error) {
        return new Resource<>(Status.ERROR, null, error);
    }
}
