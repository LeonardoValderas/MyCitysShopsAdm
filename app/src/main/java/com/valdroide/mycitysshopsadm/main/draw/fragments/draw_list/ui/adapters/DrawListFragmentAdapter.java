package com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragment;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawListFragmentAdapter extends RecyclerView.Adapter<DrawListFragmentAdapter.ViewHolder> {

    private List<Draw> drawList;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;
    private Spanned fromHtml;

    public DrawListFragmentAdapter(List<Draw> drawList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.drawList = drawList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = (DrawListFragment) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_draw_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Draw draw = drawList.get(position);
        holder.textViewDraw.setText(draw.getDESCRIPTION());
        holder.textViewDraw.setTypeface(Utils.setFontExoTextView(fragment.getActivity()));

        fromHtml = Utils.fromHtml("<b>" + fragment.getActivity().getString(R.string.date_create_hint) + "</b>" + " " +
                   Utils.formatViewDate(draw.getSTART_DATE(),
                   fragment.getActivity().getString(R.string.format_draw_adapter_enter_start),
                   fragment.getActivity().getString(R.string.format_draw_adapter_exit_start)));
        holder.textViewCreateDate.setText(fromHtml);

        fromHtml = Utils.fromHtml("<b>" + fragment.getActivity().getString(R.string.date_end_hint) + "</b>" + " " + Utils.formatViewDate(draw.getEND_DATE(),
                   fragment.getActivity().getString(R.string.format_draw_adapter_enter_end),
                   fragment.getActivity().getString(R.string.format_draw_adapter_exit_end)));
        holder.textViewEndDate.setText(fromHtml);
        fromHtml = Utils.fromHtml("<b>" + fragment.getActivity().getString(R.string.date_limit_hint) + "</b>" + " " + draw.getLIMIT_DATE() +" (inclusive)");
        holder.textViewLimitDate.setText(fromHtml);
        if (!draw.getWinner().isEmpty() && draw.getIS_ACTIVE() == 0) {
            holder.textViewError.setVisibility(View.GONE);
            holder.textViewWinner.setVisibility(View.VISIBLE);
            holder.textViewWinner.setText(draw.getWinner());
            holder.textViewWinner.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
        } else
            holder.textViewWinner.setVisibility(View.GONE);
        if (draw.getERROR_REPORTING() == 1) {
            holder.textViewError.setVisibility(View.VISIBLE);
            holder.textViewError.setText(R.string.error_reporting_draw_adapter);
            holder.textViewError.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorRed));
        } else if (draw.getIS_ZERO() == 1) {
            holder.textViewError.setVisibility(View.VISIBLE);
            holder.textViewError.setText(R.string.draw_zero_adapter);
            holder.textViewError.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
        } else {
            holder.textViewError.setVisibility(View.GONE);
        }
        holder.setOnItemClickListener(onItemClickListener, position, draw);
    }

    @Override
    public int getItemCount() {
        return drawList.size();
    }

    public void setDraw(List<Draw> draws) {
        drawList = draws;
        notifyDataSetChanged();
    }

    public void deleteDraw(int position) {
        drawList.remove(position);
        notifyDataSetChanged();
    }

    public void setUpdateDraw(int position, Draw draw) {
        drawList.remove(position);
        drawList.add(position, draw);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewDraw)
        TextView textViewDraw;
        @Bind(R.id.textViewCreateDate)
        TextView textViewCreateDate;
        @Bind(R.id.textViewEndDate)
        TextView textViewEndDate;
        @Bind(R.id.textViewLimitDate)
        TextView textViewLimitDate;
        @Bind(R.id.textViewWinner)
        TextView textViewWinner;
        @Bind(R.id.textViewError)
        TextView textViewError;
        @Bind(R.id.linearConteiner)
        LinearLayout linearConteiner;
        private View v;

        public ViewHolder(View view) {
            super(view);
            this.v = view;
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Draw draw) {

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickDraw(position, draw);
                }
            });

//            imageViewFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (shop.getIS_FOLLOW() == 0)
//                        listener.onClickFollowOrUnFollow(position, shop, true);
//                    else
//                        listener.onClickFollowOrUnFollow(position, shop, false);
//
//                }
//            });
//            imageViewOffer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickOffer(position, shop);
//                }
//            });
//            imageViewMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setEnabled(false);
//                    listener.onClickMap(shop, v);
//                }
//            });
//            imageViewContact.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickContact(shop);
//                }
//            });
        }
    }
}
