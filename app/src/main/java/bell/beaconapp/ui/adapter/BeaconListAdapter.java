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
import bell.beaconapp.model.*;


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
     *
     * @param onBeaconAddedListener The listener.
     */
    public void setOnBeaconAddedListener(@NonNull OnBeaconAddedListener onBeaconAddedListener) {
        mOnBeaconAddedListener = onBeaconAddedListener;
    }

    /**
     * Adds beacon to the adapter.
     *
     * @param beacon Beacon to add.
     */
    public void add(BeaconResult beacon) {
        if (mDataSet.contains(beacon)) {
            int beaconIndex = getIndexOfBeacon(beacon);
            mDataSet.get(beaconIndex).update(beacon);
            notifyItemChanged(beaconIndex);
        } else {
            Beacon newBeacon = new Beacon(beacon);
            mDataSet.add(newBeacon);
            notifyItemInserted(mDataSet.size() - 1);
            mOnBeaconAddedListener.onBeaconAdded(newBeacon);
        }
    }

    public void onBeaconResult(BeaconResult beaconResult) {

    }

    public void setExpanded(Beacon beacon) {
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

    private int getIndexOfBeacon(BeaconSuper beacon) {
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
        public TextView mListDistance;

        public TextView mDetailTitle;
        public TextView mDetailDistance;
        public TextView mDetailRssi;
        public TextView mDetailTxPower;
        public TextView mDetailMinMa;
        public TextView mDetailUuid;
        public TextView mDetailManId;

        public BeaconListViewHolder(View itemView) {
            super(itemView);
            mClickableView = (View) itemView.findViewById(R.id.beacon_clickable);
            mDetails = (View) itemView.findViewById(R.id.beacon_detail);
            mList = (View) itemView.findViewById(R.id.beacon_list);
            mListTitle = (TextView) itemView.findViewById(R.id.beacon_title);
            mDetailTitle = (TextView) itemView.findViewById(R.id.beacon_detail_title);
            mListDistance = (TextView) itemView.findViewById(R.id.beacon_list_distance);
            mDetailDistance = (TextView) itemView.findViewById(R.id.beacon_detail_distance);
            mDetailRssi = (TextView) itemView.findViewById(R.id.beacon_detail_rssi);
            mDetailTxPower = (TextView) itemView.findViewById(R.id.beacon_detail_txpower);
            mDetailMinMa = (TextView) itemView.findViewById(R.id.beacon_detail_minma);
            mDetailUuid = (TextView) itemView.findViewById(R.id.beacon_detail_uuid);
            mDetailManId = (TextView) itemView.findViewById(R.id.beacon_detail_manid);
        }

        public void applyBeacon(Beacon beacon) {
            mListTitle.setText(beacon.getDeviceName());
            mDetailTitle.setText(beacon.getDeviceName());
            mListDistance.setText(Math.round(beacon.getDistance()) + "m");
            mDetailDistance.setText(beacon.getDistance() + "m");
            mDetailRssi.setText(beacon.getRssi() + "");
            mDetailTxPower.setText(beacon.getTxPower() + "");
            mDetailMinMa.setText(beacon.getMajor() + ":" +  beacon.getMinor());
            mDetailUuid.setText(beacon.getUuid() + "");
            mDetailManId.setText(beacon.getManId() + "");
        }
    }

    public interface OnBeaconAddedListener {
        void onBeaconAdded(Beacon beacon);
    }
}
