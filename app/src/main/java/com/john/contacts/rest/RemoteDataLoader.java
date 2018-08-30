package com.john.contacts.rest;


import com.john.contacts.BuildConfig;
import com.john.contacts.data.DataManager;
import com.john.contacts.models.Contact;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by john on 7/19/2017.
 */

public class RemoteDataLoader
{
    private static final String BASE_URL = "https://s3.amazonaws.com/";
    private static final int HTTP_READ_TIMEOUT = 15;
    private static final int HTTP_WRITE_TIMEOUT = 15;
    private static final int HTTP_CONNECT_TIMEOUT = 15;
    private final String TAG = this.getClass().getSimpleName();
    private ContactService service;
    private BehaviorSubject<Boolean> networkInUse;
    private DataManager dm;

    public RemoteDataLoader()
    {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                                 .addConverterFactory(GsonConverterFactory.create())
                                                                 .baseUrl(BASE_URL);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }

        retrofitBuilder.client(clientBuilder.build());
        service = retrofitBuilder.build().create(ContactService.class);
    }


    public Observable<List<Contact>> loadAllContacts(DataManager dm, BehaviorSubject<Boolean> networkLoading)
    {
        this.dm = dm;
        this.networkInUse = networkLoading;
        return service.getContacts();
    }


}
