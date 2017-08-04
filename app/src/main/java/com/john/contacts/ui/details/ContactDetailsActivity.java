package com.john.contacts.ui.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.john.contacts.R;
import com.john.contacts.models.Address;
import com.john.contacts.models.Contact;
import com.john.contacts.models.Model;
import com.john.contacts.models.Phone;
import com.john.contacts.ui.list.ContactListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * Created by johns on 7/30/2017.
 */

public class ContactDetailsActivity extends AppCompatActivity
{
    private ContactDetailsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtContactName)
    TextView txtContactName;

    @BindView(R.id.txtCompanyName)
    TextView txtCompanyName;

    @BindView(R.id.work_phone_layout)
    RelativeLayout work_phone_layout;

    @BindView(R.id.txtWorkPhoneNumber)
    TextView txtWorkPhoneNumber;

    @BindView(R.id.home_phone_layout)
    RelativeLayout home_phone_layout;

    @BindView(R.id.txtHomePhoneNumber)
    TextView txtHomePhoneNumber;

    @BindView(R.id.mobile_phone_layout)
    RelativeLayout mobile_phone_layout;

    @BindView(R.id.txtMobilePhoneNumber)
    TextView txtMobilePhoneNumber;

    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @BindView(R.id.txtBirthDate)
    TextView txtBirthDate;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.iv_contact)
    ImageView iv_contact;

    private MenuItem btnFavorite;

    private Unbinder unbinder;

    private static final String CONTACT = "contact";

    private Contact contact;

    public static void startActivity(Activity context, Contact contact)
    {
        Bundle b = new Bundle();
        b.putSerializable(CONTACT, contact);
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Bundle b = getIntent().getExtras();
        contact = (Contact) b.getSerializable(CONTACT);
        presenter = new ContactDetailsPresenter(this, Model.getInstance(), contact);

        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        presenter.onCreate();
    }

    @Override
    public void onDestroy()
    {
        if(unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    public void setupToolBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void bindData(Contact contact)
    {
        if(contact == null)
            return;

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_large);
        requestOptions.error(R.drawable.user_large);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(contact.getLargeImageURL())
                    .into(iv_contact);


        txtContactName.setText(contact.getName());
        txtCompanyName.setText(contact.getCompanyName());

        bindPhone(contact.getPhone());

        Address addr = contact.getAddress();
        if(addr != null) {
            txtAddress.setText(String.format("%s %s, %s %s, US",
                    addr.getStreet(), addr.getCity(), addr.getState(), addr.getZipCode()));
        }

        txtBirthDate.setText(contact.getBirthdate());
        txtEmail.setText(contact.getEmailAddress());
    }

    private void bindPhone(Phone phone)
    {
        if(TextUtils.isEmpty(phone.getWork()))
        {
            work_phone_layout.setVisibility(View.GONE);
        }
        else
        {
            work_phone_layout.setVisibility(View.VISIBLE);
            txtWorkPhoneNumber.setText(phone.getWork());
        }

        if(TextUtils.isEmpty(phone.getHome()))
        {
            home_phone_layout.setVisibility(View.GONE);
        }
        else
        {
            home_phone_layout.setVisibility(View.VISIBLE);
            txtHomePhoneNumber.setText(phone.getHome());
        }

        if(TextUtils.isEmpty(phone.getMobile()))
        {
            mobile_phone_layout.setVisibility(View.GONE);
        }
        else
        {
            mobile_phone_layout.setVisibility(View.VISIBLE);
            txtMobilePhoneNumber.setText(phone.getMobile());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        btnFavorite = menu.findItem(R.id.btnFavorite);
        btnFavorite.setIcon( contact.getIsFavorite() ? R.drawable.favorite_true : R.drawable.favorite_false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.btnFavorite:
                if(contact.getIsFavorite())
                {
                    contact.setIsFavorite(false);
                    btnFavorite.setIcon( R.drawable.favorite_false);
                }
                else
                {
                    contact.setIsFavorite(true);
                    btnFavorite.setIcon( R.drawable.favorite_true);
                }
                presenter.updateFavorite(contact);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
