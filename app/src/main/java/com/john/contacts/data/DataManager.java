package com.john.contacts.data;


import com.john.contacts.ContactApplication;
import com.john.contacts.models.Contact;
import com.john.contacts.rest.RemoteDataLoader;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by johns on 7/30/2017.
 */

public class DataManager
{
    private final RemoteDataLoader dataLoader;
    private BehaviorSubject<Boolean> networkLoading = BehaviorSubject.create(false);
    private final String TAG = this.getClass().getSimpleName();
    private final PreferenceManager preferenceManager;

    public DataManager()
    {
        dataLoader = new RemoteDataLoader();
        preferenceManager = new PreferenceManager( ContactApplication.getAppInstance());
    }

    public Observable<Boolean> networkInUse()
    {
        return networkLoading.asObservable();
    }

    public BehaviorSubject<Boolean> getNetworkLoadingSubject()
    {
        return this.networkLoading;
    }


    public Observable<List<Contact>> getAllContacts()
    {
        return dataLoader.loadAllContacts(this, networkLoading);
    }



}
