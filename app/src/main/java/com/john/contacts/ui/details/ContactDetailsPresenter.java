package com.john.contacts.ui.details;

import com.john.contacts.ContactApplication;
import com.john.contacts.models.Contact;
import com.john.contacts.models.Model;
import com.john.contacts.ui.Presenter;
import java.util.List;

/**
 * Created by johns on 7/30/2017.
 */

public class ContactDetailsPresenter implements Presenter
{
    private ContactDetailsActivity view;
    private Model model;
    private Contact contact;
    private ContactApplication application;

    public ContactDetailsPresenter(ContactDetailsActivity activity, Model model, Contact contact)
    {
        this.view = activity;
        this.model = model;
        this.contact = contact;
        application = ContactApplication.getAppInstance();
    }

    public void updateFavorite(Contact contact)
    {
        List<Contact> contacts = model.getContacts();
        if(contacts != null && !contacts.isEmpty())
        {
            int indexToUpdate = 0;
            for(int i = 0; i < contacts.size(); i++)
            {
                Contact c = contacts.get(i);
                if(c.getId() == contact.getId())
                {
                    indexToUpdate = i;
                    break;
                }
            }

            model.getContacts().set(indexToUpdate, contact);
        }
    }

    @Override
    public void onCreate()
    {
        view.setupToolBar();
        view.bindData(contact);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
