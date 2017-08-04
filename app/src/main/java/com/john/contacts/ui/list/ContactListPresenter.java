package com.john.contacts.ui.list;

import com.john.contacts.models.Contact;
import com.john.contacts.models.Model;
import com.john.contacts.ui.Presenter;
import com.john.contacts.ui.details.ContactDetailsActivity;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.john.contacts.utils.LogUtils.LOGD;

/**
 * Created by johns on 7/30/2017.
 */

public class ContactListPresenter implements Presenter
{
    private ContactListActivity view;
    private Model model;
    private Subscription loaderSubscription;
    private Subscription dataSubscription;
    private final String TAG = this.getClass().getSimpleName();
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public ContactListPresenter(ContactListActivity activity, Model model)
    {
        this.view = activity;
        this.model = model;
    }

    @Override
    public void onCreate()
    {

        if(model.getContacts() != null && !model.getContacts().isEmpty())
        {
            buildSections(model.getContacts());
        }
        else
        {
            model.getNetworkLoading().onNext(true);
            dataSubscription = model.getAllContacts()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<List<Contact>>() {
                                        @Override
                                        public void call(List<Contact> contacts)
                                        {
                                            model.getNetworkLoading().onNext(false);
                                            model.setContacts(contacts);
                                            buildSections(contacts);
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            model.getNetworkLoading().onNext(false);
                                            LOGD(TAG, "Fail - error occurred ...");
                                        }
                                    });
        }
    }


    private void buildSections(List<Contact> contacts)
    {
        subscriptions.add( Observable.just(contacts)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<List<Contact>, Observable<Contact>>() {

                        @Override
                        public Observable<Contact> call(List<Contact> contacts) {
                            Collections.sort(contacts, new Comparator<Contact>() {
                                @Override
                                public int compare(Contact c1, Contact c2)
                                {
                                    return c1.getName().compareTo(c2.getName());
                                }
                            });

                            return Observable.from(contacts);
                        }
                    })
                    .filter(new Func1<Contact, Boolean>()
                    {
                        @Override
                        public Boolean call(Contact contact)
                        {
                            return contact.getIsFavorite();
                        }
                    })
                    .toList()
                    .subscribe(new Action1<List<Contact>>() {
                        @Override
                        public void call(List<Contact> contacts)
                        {
                            view.setupFavoriteContactView(contacts);
                        }
                    }));

        subscriptions.add( Observable.just(contacts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<Contact>, Observable<Contact>>() {

                    @Override
                    public Observable<Contact> call(List<Contact> contacts)
                    {
                        Collections.sort(contacts, new Comparator<Contact>() {
                            @Override
                            public int compare(Contact c1, Contact c2)
                            {
                                return c1.getName().compareTo(c2.getName());
                            }
                        });
                        return Observable.from(contacts);
                    }
                })
                .filter(new Func1<Contact, Boolean>()
                {
                    @Override
                    public Boolean call(Contact contact) {
                        return !contact.getIsFavorite();
                    }
                })
                .toList()
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        view.setupUI();
                    }
                })
                .subscribe(new Action1<List<Contact>>() {
                    @Override
                    public void call(List<Contact> contacts)
                    {
                        view.setupOtherContactView(contacts);
                    }
                }));


    }


    @Override
    public void onStart()
    {

    }

    public void listContactItemClicked(Contact contact)
    {
        ContactDetailsActivity.startActivity(view, contact);
    }

    @Override
    public void onPause() {
        if(dataSubscription != null)
            dataSubscription.unsubscribe();

        if(loaderSubscription != null)
            loaderSubscription.unsubscribe();

        subscriptions.unsubscribe();
    }

    @Override
    public void onResume() {
        loaderSubscription = model.isNetworkUsed()
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean netyworkInUse)
                    {
                        view.showNetworkLoading(netyworkInUse);
                    }
                });
    }

    @Override
    public void onDestroy() {

    }
}
