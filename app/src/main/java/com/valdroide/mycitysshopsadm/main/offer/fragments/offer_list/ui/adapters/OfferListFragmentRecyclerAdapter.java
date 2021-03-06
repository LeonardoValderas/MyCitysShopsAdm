package com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferListFragmentRecyclerAdapter extends RecyclerView.Adapter<OfferListFragmentRecyclerAdapter.ViewHolder> {


    private List<Offer> offersList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private boolean is_active;

    public OfferListFragmentRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener, Context context) {
        this.offersList = offersList;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Offer offer = this.offersList.get(position);
        if (offer != null) {
            Utils.setPicasso(context, offer.getURL_IMAGE(), android.R.drawable.ic_menu_crop, holder.imageViewImage);
            holder.textViewTitle.setText(offer.getTITLE());
            holder.textViewDescription.setText(offer.getOFFER());
            is_active = offer.getIS_ACTIVE() != 0;
            if (is_active)
                holder.linearConteiner.setBackgroundResource(android.R.color.transparent);
            else
                holder.linearConteiner.setBackgroundResource(R.color.colorRed);
            holder.setOnItemClickListener(context, onItemClickListener, position, offer, is_active);
        }
    }

    @Override
    public int getItemCount() {
        return this.offersList.size();
    }

    public void setOffers(List<Offer> offers) {
        offersList = offers;
        notifyDataSetChanged();
    }

    public void setOffer(Offer offer) {
        offersList.add(0, offer);
        notifyDataSetChanged();
    }

    public void updateAdapter(Offer offer, int position) {
        offersList.remove(position);
        offersList.add(position, offer);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewTitle)
        TextView textViewTitle;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.linearConteiner)
        LinearLayout linearConteiner;
        @Bind(R.id.imageViewImage)
        ImageView imageViewImage;
        @Bind(R.id.imageViewMenu)
        ImageView imageViewMenu;
//        @Bind(R.id.switchOffer)
//        Switch switchOffer;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setOnItemClickListener(final Context context, final OnItemClickListener listener, final int position, final Offer offer, final boolean is_active) {
            linearConteiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, offer);
                }
            });
            imageViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(imageViewMenu, context, is_active, position, offer, listener);
                }
            });
//            switchOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int isactive = isChecked ? 1 : 0;
//                    offer.setIS_ACTIVE(isactive);
//                    listener.onClickSwitch(position, offer);
//                }
//            });
        }
    }

    private static void showPopupMenu(View view, Context context, boolean is_active, int position, Offer offer, OnItemClickListener listener) {
        PopupMenu popup = new PopupMenu(context, view);
        if (is_active)
            popup.getMenu().add(Menu.NONE, 1, 1, context.getString(R.string.offer_able));
        else
            popup.getMenu().add(Menu.NONE, 2, 2, context.getString(R.string.offer_unable));
        popup.setOnMenuItemClickListener(new MenuItemClickListener(context, position, offer, listener));
        popup.show();
    }

    private static class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Context context;
        int position;
        Offer offer;
        OnItemClickListener listener;

        public MenuItemClickListener(Context context, int position, Offer offer, OnItemClickListener listener) {
            this.context = context;
            this.position = position;
            this.offer = offer;
            this.listener = listener;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case 1:
                    offer.setIS_ACTIVE(0);
                    listener.onClickSwitch(position, offer);
                    return true;
                case 2:
                    offer.setIS_ACTIVE(1);
                    listener.onClickSwitch(position, offer);
                    return true;
            }
            return false;
        }
    }

}

