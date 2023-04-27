package com.dengyun.baselibrary.net.rx;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.utils.ObjectUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @titile
 * @desc Created by seven on 2018/4/4.
 */

public class RxSchedulers {


    /**
     * 取代     .subscribeOn(Schedulers.io())
     .observeOn(AndroidSchedulers.mainThread())
     * 使用：.compose(RxSchedulers.io_main())
     *
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 替代 new Thread 的用法，切换到子线程执行，
     * 有内存泄漏风险，请使用下面两个方法（传值fragment或 Activity）
     */
    @Deprecated
    public static void doOnIOThread(IOTask ioTask) {
        doOnIOThread("tag", ioTask);
    }

    public static void doOnIOThread(@NonNull Fragment fragment, IOTask ioTask) {
        doOnIOThread(ObjectUtils.getClassPath(fragment), ioTask);
    }

    public static void doOnIOThread(@NonNull FragmentActivity activity, IOTask ioTask) {
        doOnIOThread(ObjectUtils.getClassPath(activity), ioTask);
    }

    /**
     * 替代 handle 的用法，切换到 main线程 执行，
     * 有内存泄漏风险，请使用下面两个方法（传值fragment或 Activity）
     */
    @Deprecated
    public static void doOnUIThread( UITask uiTask) {
        doOnUIThread("tag", uiTask);
    }

    /**
     * 替代 handle 的用法，先io线程，io结束之后切换到main线程做io操作，
     * 有内存泄漏风险，请使用下面两个方法（传值fragment或 Activity）
     */
    @Deprecated
    public static void doTask(Task task) {
        doTask("tag", task);
    }

    public static void doTask(final Fragment fragment, Task task) {
        doTask(ObjectUtils.getClassPath(fragment), task);
    }

    public static void doTask(final FragmentActivity activity, Task task) {
        doTask(ObjectUtils.getClassPath(activity), task);
    }

    private static void doOnIOThread(final String tag, IOTask ioTask) {
        Observable.just(ioTask)
                .observeOn(Schedulers.io())
                .subscribe(new Observer<IOTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxManager.getInstance().add(tag, d);
                    }

                    @Override
                    public void onNext(IOTask ioTask) {
                        ioTask.doOnIOThread();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private static void doOnUIThread(final String tag, UITask uiTask) {
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UITask>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxManager.getInstance().add(tag, d);
                    }

                    @Override
                    public void onNext(UITask uiTask) {
                        uiTask.doOnUIThread();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private static void doTask(final String tag, final Task task) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                task.doOnIOThread();
                e.onNext("next");
                e.onComplete();
            }
        })
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxManager.getInstance().add(tag, d);
                    }

                    @Override
                    public void onNext(String t) {
                        task.doOnUIThread();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public interface IOTask {
        void doOnIOThread();
    }

    public interface UITask {
        void doOnUIThread();
    }

    public abstract static class Task {

        public abstract void doOnIOThread();

        public abstract void doOnUIThread();
    }
}
