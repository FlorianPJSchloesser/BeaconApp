package bell.beaconapp.ui.adapter;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bell.beaconapp.R;
import bell.beaconapp.model.Beacon;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.BeaconListViewHolder> {

    /**
     * Context used to inflate layouts.
     */
    private Context mContext;

    /**
     * Stores the expanded position.<br/>-1 when no item is expanded.
     */
    int mExpandedPosition;

    /**
     * Data structure holding the beacons.
     */
    ArrayList<Beacon> mDataSet;

    /**
     * To inform other classes about new beacons.
     */
    OnBeaconAddedListener mOnBeaconAddedListener;

    /**
     * Creates BeaconListAdapter for the use with RecyclerView.
     *
     * @param context Used to inflate layouts.
     */
    public BeaconListAdapter(Context context) {
        mContext = context;
        mExpandedPosition = -1;
        mDataSet = new ArrayList<>();
    }

    /**
     * Set OnBeaconAddedListener.
     * @param onBeaconAddedListener The listener.
     */
    public void setOnBeaconAddedListener (@NonNull OnBeaconAddedListener onBeaconAddedListener) {
        mOnBeaconAddedListener = onBeaconAddedListener;
    }

    /**
     * Adds beacon to the adapter.
     *
     * @param beacon Beacon to add.
     */
    public void add(Beacon beacon) {
        if (mDataSet.contains(beacon)) {
            int beaconIndex = getIndexOfBeacon(beacon);
            mDataSet.remove(beacon);
            mDataSet.add(beaconIndex, beacon);
            notifyItemChanged(beaconIndex);
        } else {
            mDataSet.add(beacon);
            notifyItemInserted(mDataSet.size() - 1);
            mOnBeaconAddedListener.onBeaconAdded(beacon);
        }
    }

    public void setExpanded (Beacon beacon) {
        mExpandedPosition = getIndexOfBeacon(beacon);
        notifyItemChanged(getIndexOfBeacon(beacon));
    }

    /**
     * Removes beacon from adapter.
     *
     * @param beacon Beacon to remove.
     */
    public void remove(Beacon beacon) {
        int beaconIndex = getIndexOfBeacon(beacon);
        mDataSet.remove(beaconIndex);
        notifyItemRemoved(beaconIndex);
    }

    @Override
    public BeaconListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_beacon, null);
        return new BeaconListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(final BeaconListViewHolder holder, final int position) {

        final boolean isExpanded = position == mExpandedPosition;

        holder.mDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.mList.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
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

    private int getIndexOfBeacon(Beacon beacon) {
        return mDataSet.indexOf(beacon);
    }

    /*private boolean beaconAlreadyExist(Beacon beacon) {
        for (int i = 0; i < mDataSet.size(); i++) {
            if (beacon.equals(mDataSet.get(i))) {
                return true;
            }
        }
        return false;
    }*/

    public class BeaconListViewHolder extends RecyclerView.ViewHolder {

        public View mClickableView;
        public View mDetails;
        public View mList;
        public TextView mListTitle;
        public TextView mDetailTitle;
        public TextView mListDistance;
        public TextView mDetailDistance;

        public BeaconListViewHolder(View itemView) {
            super(itemView);
            mClickableView = (View) itemView.findViewById(R.id.beacon_clickable);
            mDetails = (View) itemView.findViewById(R.id.beacon_detail);
            mList = (View) itemView.findViewById(R.id.beacon_list);
            mListTitle = (TextView) itemView.findViewById(R.id.beacon_title);
            mDetailTitle = (TextView) itemView.findViewById(R.id.beacon_detail_title);
            mListDistance = (TextView) itemView.findViewById(R.id.beacon_list_distance);
            mDetailDistance = (TextView) itemView.findViewById(R.id.beacon_detail_distance);
        }

        public void applyBeacon(Beacon beacon) {
            mListTitle.setText(beacon.getDeviceName());
            mDetailTitle.setText(beacon.getDeviceName());
            mListDistance.setText(beacon.getDistance() + "m");
            mDetailDistance.setText(beacon.getDistance() + "m");
        }
    }

    public interface OnBeaconAddedListener {
        void onBeaconAdded (Beacon beacon);
    }
}
