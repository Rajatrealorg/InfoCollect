package in.mywebdomain.infocollect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import in.mywebdomain.infocollect.R;
import in.mywebdomain.infocollect.entities.TheClient;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardRecyclerViewHolder> {
    private List<TheClient> client_list;
    private Context context;
    private static int currentPosition99 = -1;

    public CardRecyclerAdapter(List<TheClient> client_list, Context context) {
        this.client_list = client_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CardRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_list_card, parent, false);
        return new CardRecyclerViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CardRecyclerViewHolder recyclerViewHolder, @SuppressLint("RecyclerView") final int currentPosition) {
        TheClient theClient = client_list.get(currentPosition);
        recyclerViewHolder.cardView.setTitle(theClient.getFName());
        recyclerViewHolder.dob.setText(theClient.getDob());
        recyclerViewHolder.pno.setText(theClient.getPno());
        recyclerViewHolder.address.setText(theClient.getAddress());
        recyclerViewHolder.lat.setText(theClient.getLat());
        recyclerViewHolder.lng.setText("," + theClient.getLng());
    }

    @Override
    public int getItemCount() {
        return client_list.size();
    }

    class CardRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView pno, dob, address, lat, lng;
        CardView cardView;

        CardRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            AssetManager am = context.getApplicationContext().getAssets();

            Typeface typeface = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "micross.ttf"));

//        setTypeface(typeface);


            cardView = itemView.findViewById(R.id.show_list_card_layout);
            pno = itemView.findViewById(R.id.show_phone_more);
            dob = itemView.findViewById(R.id.show_dob_more);
            address = itemView.findViewById(R.id.show_address_more);
            lat = itemView.findViewById(R.id.show_lat_more);
            lng = itemView.findViewById(R.id.show_lng_more);

            pno.setTypeface(typeface);
            dob.setTypeface(typeface);
            address.setTypeface(typeface);
        }
    }
}

