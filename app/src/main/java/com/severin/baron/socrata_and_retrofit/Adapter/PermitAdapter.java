package com.severin.baron.socrata_and_retrofit.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.severin.baron.socrata_and_retrofit.Model.BuildingPermit;
import com.severin.baron.socrata_and_retrofit.R;

import java.util.List;

public class PermitAdapter extends
        RecyclerView.Adapter<PermitAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPermitType, tvValue, tvWorkType, tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            tvPermitType = (TextView) itemView.findViewById(R.id.textView_itemPermitType);
            tvValue = (TextView) itemView.findViewById(R.id.textView_itemValue);
            tvWorkType = (TextView) itemView.findViewById(R.id.textView_itemWorkType);
            tvDescription = (TextView) itemView.findViewById(R.id.textView_itemDescription);
        }
    }

    private List<BuildingPermit> mPermit;
    private Context mContext;

    public PermitAdapter(Context context, List<BuildingPermit> permit) {
        mPermit = permit;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public PermitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_permit, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PermitAdapter.ViewHolder viewHolder, int position) {
        BuildingPermit permit = mPermit.get(position);

        TextView tvPermitType = viewHolder.tvPermitType;
        TextView tvValue= viewHolder.tvValue;
        TextView tvWorkType = viewHolder.tvWorkType;
        TextView tvDescription = viewHolder.tvDescription;

        tvPermitType.setText("Permit Type: " + permit.getPermit_type());
        if (permit.getValue() != 0 ) {
            tvValue.setText("Value: $" + String.valueOf(permit.getValue()));
        } else {
            tvValue.setText("Value: unknown");
        }
        tvWorkType.setText("Work Type: " + permit.getWork_type());
        tvDescription.setText(permit.getDescription());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPermit.size();
    }
}

