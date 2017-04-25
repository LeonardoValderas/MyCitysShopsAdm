package com.valdroide.mycitysshopsadm.main.offer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivityRecyclerAdapter extends RecyclerView.Adapter<OfferActivityRecyclerAdapter.ViewHolder> {


    private List<Offer> offersList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public OfferActivityRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener, Context context) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = this.offersList.get(position);
        if (offer != null) {
            Utils.setPicasso(context, offer.getURL_IMAGE(), android.R.drawable.ic_menu_crop, holder.imageViewImage);
            holder.textViewTitle.setText(offer.getTITLE());
            holder.textViewDescription.setText(offer.getOFFER());
            holder.switchOffer.setChecked(offer.getIS_ACTIVE() == 0 ? false : true);
            if (offer.getIS_ACTIVE() != 1)
                holder.switchOffer.setText(R.string.offer_unable);
            holder.setOnItemClickListener(onItemClickListener, position, offer);
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
        @Bind(R.id.switchOffer)
        Switch switchOffer;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Offer offer) {
            linearConteiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position, offer);
                }
            });

            switchOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int isactive = isChecked ? 1 : 0;
                    offer.setIS_ACTIVE(isactive);
                    listener.onClickSwitch(position, offer);
                }
            });
        }
    }
}

