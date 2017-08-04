package com.john.contacts.rest;

import com.john.contacts.models.Contact;
import java.util.List;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by johns on 7/30/2017.
 */

public interface ContactService
{
    @GET("technical-challenge/v3/contacts.json")
    Observable<List<Contact>> getContacts();
}
