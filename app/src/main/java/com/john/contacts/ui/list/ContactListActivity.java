package com.john.contacts.ui.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.john.contacts.R;
import com.john.contacts.models.Contact;
import com.john.contacts.models.Model;
import com.john.contacts.ui.details.ContactDetailsActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.subscriptions.CompositeSubscription;
/**
 * Created by johns on 7/30/2017.
 */

public class ContactListActivity extends AppCompatActivity
{
    private ContactListPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressbar)
    MaterialProgressBar progressbar;

    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    private SectionedRecyclerViewAdapter adapter;

    private Unbinder unbinder;

    private CompositeSubscription subscriptions;

    private List<Contact> favorites;
    private List<Contact> otherContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        presenter = new ContactListPresenter(this, Model.getInstance());

        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        adapter = new SectionedRecyclerViewAdapter();

        progressbar.setVisibility(View.INVISIBLE);

        subscriptions = new CompositeSubscription();
        presenter.onCreate();
    }


    public void setupFavoriteContactView(List<Contact> favorites)
    {
        this.favorites = favorites;
    }

    public void setupOtherContactView(List<Contact> otherContacts)
    {
        this.otherContacts = otherContacts;
    }


    public void setupUI()
    {
        adapter.addSection(new ContactsSection("FAVORITE CONTACTS", this.favorites));
        adapter.addSection(new ContactsSection("OTHER CONTACTS", this.otherContacts));
        rv_contacts.setLayoutManager(new LinearLayoutManager(this));
        rv_contacts.setAdapter(adapter);
    }



    private class ContactsSection extends StatelessSection
    {
        private String title;
        private List<Contact> contacts;

        public ContactsSection(String title, List<Contact> contacts)
        {
            super(new SectionParameters.Builder(R.layout.single_contact_rv_item)
                       .headerResourceId(R.layout.section_header).build());

            this.title = title;
            this.contacts = contacts;
        }


        @Override
        public int getContentItemsTotal() {
            return contacts.size();
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.txtSectionTitle.setText(title);
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;
            final Contact c = contacts.get(position);
            if(c != null)
            {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.user_small);
                requestOptions.error(R.drawable.user_small);

                Glide.with(ContactListActivity.this)
                        .setDefaultRequestOptions(requestOptions)
                            .load(c.getSmallImageURL())
                            .into(viewHolder.iv_contact);



                viewHolder.iv_favorite.setImageResource(c.getIsFavorite()? R.drawable.favorite_true
                        : R.drawable.favorite_false);
                viewHolder.txtContactName.setText(c.getName());
                viewHolder.txtCompanyName.setText(c.getCompanyName());
                viewHolder.viewMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ContactDetailsActivity.startActivity(ContactListActivity.this, c);
                    }
                });
            }
        }
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtSectionTitle)
        TextView txtSectionTitle;

        HeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.viewMain)
        RelativeLayout viewMain;

        @BindView(R.id.iv_contact)
        ImageView iv_contact;

        @BindView(R.id.iv_favorite)
        ImageView iv_favorite;

        @BindView(R.id.txtContactName)
        TextView txtContactName;

        @BindView(R.id.txtCompanyName)
        TextView txtCompanyName;

        ItemViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy()
    {
        if(unbinder != null)
        {
            unbinder.unbind();
            unbinder = null;
        }

        if(subscriptions != null)
        {
            subscriptions.unsubscribe();
        }

        super.onDestroy();
    }

    public void showNetworkLoading(Boolean networkInUse)
    {
        progressbar.setVisibility(networkInUse? View.VISIBLE : View.INVISIBLE);
    }


}
