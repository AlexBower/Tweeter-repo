package edu.byu.cs.tweeter.view.main.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.util.ImageUtils;

abstract public class StatusFragment extends PaginatedFragment {

    protected class StatusHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView statusTime;
        private final TextView statusMessage;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        StatusHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            statusTime = itemView.findViewById(R.id.statusTime);
            statusMessage = itemView.findViewById(R.id.statusMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userName != null) {
                        Toast.makeText(getContext(), "You selected '" + userName.getText() + "'. " +
                                statusMessage.getText() + " from " + statusTime.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @SuppressLint("NewApi")
        void bindStatus(Status status) {
            User user = status.getUser();
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
            statusTime.setText(formatTime(status.getTime()));
            setupMessage(status.getMessage());
        }

        void setupMessage(String text) {
            SpannableString spannableString = new SpannableString(text);

            List<String> strings = new ArrayList<>(Arrays.asList(text.split(" ")));
            Collections.reverse(strings);
            for (String string : strings) {
                int startIndex = -1;
                try {
                    URL url = new URL(string);
                    startIndex = text.lastIndexOf(string);
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(string));
                            startActivity(browserIntent);
                        }
                    };
                    spannableString.setSpan(clickableSpan, startIndex,startIndex + string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (MalformedURLException e) {
                    if (string.startsWith("@")) {
                        startIndex = text.lastIndexOf(string);

                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Toast.makeText(getContext(), "USER MENTION: " + string, Toast.LENGTH_SHORT).show();
                            }
                        };
                        spannableString.setSpan(clickableSpan, startIndex,startIndex + string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (startIndex != -1) {
                    text = text.substring(0, startIndex)
                            + text.substring(startIndex + string.length());
                }
            }

            statusMessage.setText(spannableString);
            statusMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @SuppressLint("NewApi")
        String formatTime(LocalDateTime time) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy  hh:mm a");
            return time.format(formatter);
        }
    }

    abstract protected class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusHolder> {

        protected final List<Status> statuses = new ArrayList<>();

        protected Status lastStatus;

        protected boolean hasMorePages;
        protected boolean isLoading = false;

        StatusRecyclerViewAdapter() {
            loadMoreItems();
        }

        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }


        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StatusFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StatusHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusHolder statusHolder, int position) {
            if(!isLoading) {
                statusHolder.bindStatus(statuses.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more status
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            doLoadMoreItems();
        }

        abstract void doLoadMoreItems();

        @SuppressLint("NewApi")
        protected void addLoadingFooter() {
            addItem(new Status("Dummy", LocalDateTime.now(), new User("Dummy", "User", "")));
        }

        protected void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }
}
