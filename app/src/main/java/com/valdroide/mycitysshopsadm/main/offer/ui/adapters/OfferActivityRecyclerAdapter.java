package com.valdroide.mycitysshopsadm.main.offer.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Offer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivityRecyclerAdapter extends RecyclerView.Adapter<OfferActivityRecyclerAdapter.ViewHolder> {


    private List<Offer> offersList;
    private OnItemClickListener onItemClickListener;

    public OfferActivityRecyclerAdapter(List<Offer> offersList, OnItemClickListener onItemClickListener) {
        this.offersList = offersList;
        this.onItemClickListener = onItemClickListener;
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
            holder.textViewTitle.setText(offer.getTITLE());
            holder.textViewDescription.setText(offer.getOFFER());
            //    holder.textViewDateInit.setText(offer.getDATE_INIT());
            holder.textViewDateEnd.setText(offer.getDATE_END());
            holder.setOnItemClickListener(onItemClickListener, position, offer);
        }
    }

    @Override
    public int getItemCount() {
        return this.offersList.size();
    }

    public void removeOffer(Offer offer) {
        this.offersList.remove(offer);
       notifyDataSetChanged();
    }

    public void setOffers(List<Offer> offers) {
        this.offersList = offers;
        notifyDataSetChanged();
    }

    public void setOffer(Offer offer) {
        this.offersList.add(offer);
        notifyDataSetChanged();
    }

    public void updateAdapter(Offer offer) {
        for (int i = 0; i < this.offersList.size(); i++) {
            if (this.offersList.get(i).getID_OFFER_KEY() == offer.getID_OFFER_KEY()) {
                this.offersList.get(i).setDATE_EDIT(offer.getDATE_EDIT());
                this.offersList.get(i).setTITLE(offer.getTITLE());
                this.offersList.get(i).setOFFER(offer.getOFFER());
                break;
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewTitle)
        TextView textViewTitle;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.textViewDateInit)
        TextView textViewDateInit;
        @Bind(R.id.textViewDateEnd)
        TextView textViewDateEnd;
        @Bind(R.id.linearConteiner)
        LinearLayout linearConteiner;
//        @Bind(R.id.imageButtonClose)
//        ImageButton imageButtonClose;
//        @Bind(R.id.linearCloseButton)
//        LinearLayout linearCloseButton;

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

            linearConteiner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(v, position, offer);
                    return true;
                }
            });
        }
    }
}

