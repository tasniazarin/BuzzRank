package com.example.buzzrank;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private List<Participant> participantsList;

    public ParticipantAdapter(List<Participant> participantsList) {
        this.participantsList = participantsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Participant participant = participantsList.get(position);
        holder.textViewParticipantName.setText(participant.getName());
        holder.textViewTimestamp.setText(participant.getTimestamp().toString());
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewParticipantName, textViewTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewParticipantName = itemView.findViewById(R.id.textViewParticipantName);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}