package com.thaleslima.android.popularmovies.utilities;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thales on 18/01/17.
 */

public abstract class ObservableUtil<T> {
    public Observable<T> buildObservable() {
        Observable.OnSubscribe<T> onSubscribe = new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T obj = null;

                try {
                    obj = buildObject();
                } catch (Exception e) {
                    subscriber.onError(e);
                }

                if (obj != null) {
                    subscriber.onNext(obj);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NullPointerException());
                }
            }
        };
        return Observable.create(onSubscribe);
    }

    protected abstract T buildObject() throws Exception;

    public static <U> Observable<U> buildObservable(Observable<U> observable) {
        return observable.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
