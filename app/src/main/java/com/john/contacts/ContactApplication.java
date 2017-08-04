package com.john.contacts;

import android.app.Application;
import com.john.contacts.models.Contact;
import java.util.List;
/**
 * Created by johns on 7/30/2017.
 */

public class ContactApplication extends Application
{
    private static ContactApplication instance;
    private final String TAG = this.getClass().getSimpleName();

    public static synchronized ContactApplication getAppInstance()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }


}
