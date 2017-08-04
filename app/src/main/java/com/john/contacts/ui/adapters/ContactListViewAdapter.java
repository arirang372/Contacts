package com.john.contacts.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.john.contacts.R;
import com.john.contacts.models.Contact;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johns on 7/30/2017.
 */

public class ContactListViewAdapter extends ArrayAdapter<Contact>
{
    private final LayoutInflater inflater;

    public ContactListViewAdapter(Context context, List<Contact> contacts)
    {
        super(context, R.layout.single_contact_rv_item);
        setNotifyOnChange(false);
        addAll(contacts);

        inflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        ViewHolder holder;
        if(v == null)
        {
            v = inflater.inflate(R.layout.single_contact_rv_item, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }

        Contact c = getItem(position);
        if(c!= null)
        {
            holder.iv_favorite.setImageResource( c.getIsFavorite()
                    ? R.drawable.favorite_true : R.drawable.favorite_false);
            holder.txtContactName.setText(c.getName());
            holder.txtCompanyName.setText(c.getCompanyName());
        }

        return v;
    }

    static class ViewHolder
    {
        @BindView(R.id.iv_favorite)
        ImageView iv_favorite;

        @BindView(R.id.txtContactName)
        TextView txtContactName;

        @BindView(R.id.txtCompanyName)
        TextView txtCompanyName;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
