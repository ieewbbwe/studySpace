package com.webber.rxjavademo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mDemoTv;
    private ImageView mDemoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDemoTv = (TextView) findViewById(R.id.demo_tv);
        mDemoIv = (ImageView) findViewById(R.id.demo_iv);

        //demo1();
        //demo2();
        //demo3();
        //demo4();
        //demo5();
        //demo6();
        //demo7();
        //demo8();
        //demo9();
        //demo10();
        //学习了扔物线 的RxJava 大致来总结一下
        //RxJava 是一种支持异步操作的库，并且支持链式调用，内部的实现是基于观察者模式
        //RxJava 最大的亮点在于他能使程序逻辑更明了，并且提高了扩展性
        //简单使用
        // Observable 与 Observer 通过subscribe 联系起来 Observable.subscribe(Observer)
        // Observable.create()\Observable.from()\Observable.just()用于创建一个Observable 对象
        // 通常在使用的时候有库中提供的Fun&Action 来做具体操作
        // 对于线程的处理，subscribeOn() 指定事件产生的线程，例如做一些耗时操作，多次设置时已第一次设置的线程为准;
        // observableOn() 指定事件消费的线程，例如要在UI线程显示，多次设置时已最后一次的线程为准
        // 通常使用的线程有：io()指 I\O线程，用于一些读写操作，例如查找数据库，网络请求,内部是无上限的线程池；
        // computation() 计算线程，用于处理一些CPU密集计算，内部是核数的线程池；
        // newThread() 新起一个线程；immediate() 当前线程，也是默认值；AndroidSchedulers.mainThread()，Android主线程即UI线程；
        // 变换 （重难点）**
        // 简单理解就是传入对象A输出对象B
        // map() 一对一的变换，输入一个对象A，输出一个对象B
        // flatMap() 一对多的变换，输入一个对象A，输出多个对象B
        //原理分析
        // 变换原理 （没搞明白，个人理解就是在Observable操作时生成一个新的Observable,并传递给subscribe）
        //练手示例 利用RxJava 实现三级联动

        demo11();
    }

    //连接符示例
    private void demo11() {
        demo11_1();
        //demo11_2();
        //demo11_3();
    }

    //组合连接符
    private void demo11_3() {
        demo11_3_1();
    }

    // GroupBy 根据条件分组
    // 需求：有一些学生，按照他们的班级分类
    private void demo11_3_1() {
        //TODO GroupBy exc
        List<StudentBean> students = getStudent();

        Observable.from(students)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .groupBy(new Func1<StudentBean, String>() {
                    @Override
                    public String call(StudentBean studentBean) {
                        return studentBean.getUnity();
                    }
                }).subscribe(new Action1<GroupedObservable<String, StudentBean>>() {
            @Override
            public void call(GroupedObservable<String, StudentBean> stringStudentBeanGroupedObservable) {
                stringStudentBeanGroupedObservable.subscribe(new Action1<StudentBean>() {
                    @Override
                    public void call(StudentBean studentBean) {

                    }
                });
            }
        });
    }

    //过滤连接符
    private void demo11_2() {

    }

    //转换连接符 map flatMap concatMap
    private void demo11_1() {
        //TODO flatMap 无序输出 concatMap 有序输出
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just(integer + "");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("demo11_1", s);
                    }
                });
    }

    //执行前的线程操作 doOnScheduler
    //例如用于网络请求开始前 显示进度条
    private void demo10() {
        Observable.just(1, 0, 2, 4)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d("demo10", "doOnSub:" + getCurrentThreadName());
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("demo10", "sub" + getCurrentThreadName());
                    }
                });
    }

    private String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    //变换中的多次线程切换 以map flatMap为例
    //需求：输出学生的多门成绩
    private void demo9() {
        final List<StudentBean> students = getStudent();
        Observable.from(students)
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<StudentBean, Observable<Cause>>() {
                    @Override
                    public Observable<Cause> call(StudentBean studentBean) {
                        Log.d("demo9", "姓名:" + studentBean.getName() + "Thread:" + Thread.currentThread().getName());
                        return Observable.from(studentBean.getCauseList());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cause>() {
                    @Override
                    public void call(Cause cause) {
                        Log.d("demo9", formatCause(cause) + "Thread:" + Thread.currentThread().getName());
                    }
                });
    }

    //变换原理 lift
    //需求：将Integer 转为String
    private void demo8() {
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        });
        observable1.lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {
                return null;
            }
        });
    }

    //变换队列 flatMap
    //有这样一个需求：有若干学生，现在需要输出这些学生所有的成绩
    private void demo7() {
        List<StudentBean> students = getStudent();
        //demo7_1(students);
        //demo7_2(students);
        //demo7_3(students);
        demo7_4(students);
    }

    //写到这里感觉到了 from 和for循环一样，把list里面的对象挨个输出
    //这个例子传入的是Students 输出的是每一个课程信息
    private void demo7_4(List<StudentBean> students) {
        Observable.from(students).flatMap(new Func1<StudentBean, Observable<Cause>>() {
            @Override
            public Observable<Cause> call(StudentBean studentBean) {
                Log.d("demo7_4", "姓名：" + studentBean.getName());
                return Observable.from(studentBean.getCauseList());
            }
        }).subscribe(new Action1<Cause>() {
            @Override
            public void call(Cause cause) {
                Log.d("demo7_4", formatCause(cause));
            }
        });
    }

    private void demo7_3(List<StudentBean> students) {
        Observable.from(students).map(new Func1<StudentBean, List<Cause>>() {
            @Override
            public List<Cause> call(StudentBean studentBean) {
                Log.d("demo7_3", "姓名：" + studentBean.getName());
                return studentBean.getCauseList();
            }
        }).subscribe(new Action1<List<Cause>>() {
            @Override
            public void call(List<Cause> causes) {
                for (Cause cause : causes) {
                    Log.d("demo7_3", formatCause(cause));
                }
            }
        });
    }

    //RxJava
    private void demo7_2(List<StudentBean> students) {
        Observable.from(students).subscribe(new Action1<StudentBean>() {
            @Override
            public void call(StudentBean studentBean) {
                Log.d("demo7_2", "姓名：" + studentBean.getName());
                for (Cause cause : studentBean.getCauseList()) {
                    Log.d("demo7_2", formatCause(cause));
                }
            }
        });
    }

    //for 循环
    private void demo7_1(List<StudentBean> students) {
        for (StudentBean studentBean : students) {
            Log.d("demo7_1", "姓名：" + studentBean.getName());
            for (Cause cause : studentBean.getCauseList()) {
                Log.d("demo7_1", formatCause(cause));
            }
        }
    }

    private String formatCause(Cause cause) {
        return String.format("caseName:%s->>causeGrade:%s", cause.getName(), cause.getGrade());
    }

    //float与double 的区别 单精度4字节，双精度8字节
    private List<StudentBean> getStudent() {
        List<StudentBean> students = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StudentBean student = new StudentBean();
            student.setName("学生" + i);
            student.setUnity(Math.random() * 5 + "班");
            List<Cause> causes = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Cause cause = new Cause(getCauseName(j), (float) (Math.random() * 100));
                causes.add(cause);
            }
            student.setCauseList(causes);
            students.add(student);
        }
        return students;
    }

    private String getCauseName(int j) {
        switch (j) {
            case 0:
                return "语文";
            case 1:
                return "数学";
            case 2:
                return "外语";
            default:
                return "Android开发入门";
        }
    }

    //变换例子 map
    //传入的对象转换为另一个对象 其实就是在map 的Fun 方法中做了操作
    //这只是简单的对象变换，RxJava还支持整个事件队列的变换
    private void demo6() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("file:///android_asset/01.jpg");
            }
        }).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                Log.d("demo5", getAssets().getLocales()[0]);
                //TODO 打开filePath无法获得Bitmap的问题
                //return BitmapFactory.decodeFile(s);
                try {
                    return BitmapFactory.decodeStream(getAssets().open(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mDemoIv.setImageBitmap(bitmap);
            }
        });

    }

    //线程卡顿例子
    private void demo5() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    Log.d("demo5", Thread.currentThread().getName());
                    Thread.sleep(6000);
                    subscriber.onNext("我运行结束了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        mDemoTv.setText((String) o);
                        Log.d("demo5", (String) o);
                    }
                });
    }

    //线程切换例子
    private void demo4() {
        //just 会将传入的参数依次发送
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d("demo4", Thread.currentThread().getId() + "");
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        })
                //事件产生的线程
                //Schedulers.io() IO线程 数据库读写，文件读写，网络请求，内部是一个无上限的线程池
                //Schedulers.immediate() 当前线程，默认的线程
                //Schedulers.computation() 用于CPU密集计算的线程 图形的计算 内部是一个CPU核数的线程池
                //Schedulers.newThread() 新开一个线程
                .subscribeOn(Schedulers.io())
                //事件消费的线程
                //AndroidSchedulers.mainThread() Android主线程 用于UI更新
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("demo4", Thread.currentThread().getName());
                        Log.d("demo4", integer + "");
                    }
                });
    }

    //例子
    private void demo3() {
        final int imgRes = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                subscriber.onNext(getTheme().getDrawable(imgRes));
                Log.d("demo3", "Observable:" + Thread.currentThread().getName());
            }
        }).subscribe(new Action1<Drawable>() {
            @Override
            public void call(Drawable drawable) {
                mDemoIv.setBackground(drawable);
                Log.d("demo3", "Subscribe:" + Thread.currentThread().getName());
            }
        });
    }

    //例子
    private void demo2() {
        String[] names = {"name1", "name2", "name3"};
        //from 表示将传入的集合 拆分成对象后依次输出
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("webber", "demo2:" + s);
            }
        });
    }

    //简单的创建
    private void demo1() {
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("webber", "onCompleted:");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("webber", "onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.d("webber", "onNext:" + s);
            }
        };
        //创建被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("item1");
                subscriber.onNext("item2");
                subscriber.onNext("item3");
                //subscriber.onCompleted();
                subscriber.onError(new Throwable("我出错了！！！"));
            }
        });

        //订阅二者关系
        observable.subscribe(observer);
    }
}
