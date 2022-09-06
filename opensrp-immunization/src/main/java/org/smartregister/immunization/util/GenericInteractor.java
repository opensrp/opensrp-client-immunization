package org.smartregister.immunization.util;

import java.util.concurrent.Callable;

import timber.log.Timber;

public class GenericInteractor implements CallableInteractor {

    protected AppExecutors appExecutors;

    public GenericInteractor() {
        appExecutors = new AppExecutors();
    }

    public GenericInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    @Override
    public <T> void execute(Callable<T> callable, CallableInteractorCallBack<T> callBack) {
        execute(callable, callBack, AppExecutors.Request.DISK_THREAD);
    }

    @Override
    public <T> void execute(Callable<T> callable, CallableInteractorCallBack<T> callBack, AppExecutors.Request request) {
        Runnable runnable = () -> {
            try {
                T result = callable.call();
                appExecutors.mainThread().execute(() -> callBack.onResult(result));
            } catch (Exception e) {
                Timber.e(e);
                appExecutors.mainThread().execute(() -> callBack.onError(e));
            }
        };
        appExecutors.execute(runnable, request);
    }
}
