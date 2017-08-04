package com.john.contacts.models;

import com.john.contacts.data.DataManager;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by johns on 7/30/2017.
 */

public class Model
{
    private static Model instance;
    private final DataManager dm;
    private List<Contact> contact;
    public static synchronized Model getInstance()
    {
        if(instance == null)
        {
            DataManager manager = new DataManager();
            instance = new Model(manager);
        }
        return instance;
    }

    private Model(DataManager manager)
    {
        dm = manager;
    }

    public void setContacts(List<Contact> contact)
    {
        this.contact = contact;
    }

    public List<Contact> getContacts()
    {
        return this.contact;
    }

    public Observable<List<Contact>> getAllContacts()
    {
        return dm.getAllContacts();
    }

    public Observable<Boolean> isNetworkUsed()
    {
        return dm.networkInUse().distinctUntilChanged();
    }

    public BehaviorSubject<Boolean> getNetworkLoading()
    {
        return dm.getNetworkLoadingSubject();
    }
}
