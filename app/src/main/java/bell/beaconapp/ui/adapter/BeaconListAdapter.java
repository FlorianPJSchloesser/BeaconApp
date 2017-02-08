package bell.beaconapp.ui.adapter;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bell.beaconapp.R;
import bell.beaconapp.model.Beacon;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.BeaconListViewHolder>{

    Context mContext;
    int mExpandedPosition;
    RecyclerView mRecyclerView;
    ArrayList<Beacon> mDataSet;

    public BeaconListAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mExpandedPosition = -1;
        mRecyclerView = recyclerView;
        mDataSet = new ArrayList<>();
    }

    public void add(Beacon beacon) {
        mDataSet.add(beacon);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void remove(int pos) {
        mDataSet.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public BeaconListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_beacon,null);
        return new BeaconListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(final BeaconListViewHolder holder, final int position) {

        final boolean isExpanded = position==mExpandedPosition;

        holder.mDetails.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.mList.setVisibility(isExpanded?View.GONE:View.VISIBLE);
        holder.itemView.setActivated(isExpanded);
        holder.applyBeacon(mDataSet.get(position));
        holder.mClickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyDataSetChanged();
            }
        });
    }

    public class BeaconListViewHolder extends RecyclerView.ViewHolder{

        public View mClickableView;
        public View mDetails;
        public View mList;
        public TextView mListTitle;
        public TextView mDetailTitle;

        public BeaconListViewHolder(View itemView) {
            super(itemView);
            mClickableView = (View) itemView.findViewById(R.id.beacon_clickable);
            mDetails = (View) itemView.findViewById(R.id.beacon_detail);
            mList = (View) itemView.findViewById(R.id.beacon_list);
            mListTitle = (TextView) itemView.findViewById(R.id.beacon_title);
            mDetailTitle = (TextView) itemView.findViewById(R.id.beacon_detail_title);
        }

        public void applyBeacon(Beacon beacon) {
            mListTitle.setText(beacon.getTitle());
            mDetailTitle.setText(beacon.getTitle());
            
        }
    }
}
