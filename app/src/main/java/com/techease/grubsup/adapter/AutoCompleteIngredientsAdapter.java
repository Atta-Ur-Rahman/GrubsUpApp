package com.techease.grubsup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.model.getAllIngredientsWithOutCategory.GetAllIngredientsWithOutCategoryDataModel;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteIngredientsAdapter extends ArrayAdapter<GetAllIngredientsWithOutCategoryDataModel> {
    private List<GetAllIngredientsWithOutCategoryDataModel> ingredientsListFull;

    Context context;

    public AutoCompleteIngredientsAdapter(@NonNull Context context, @NonNull List<GetAllIngredientsWithOutCategoryDataModel> ingridientsList) {
        super(context, 0, ingridientsList);
        ingredientsListFull = new ArrayList<>(ingridientsList);
        this.context = context;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.customer_row, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.tv_autocoplete_item_name);
        ImageView imageViewFlag = convertView.findViewById(R.id.iv_autocomplete_item_image);

        GetAllIngredientsWithOutCategoryDataModel countryItem = getItem(position);

        if (countryItem != null) {
            textViewName.setText(countryItem.getItem());
            Glide.with(context).load(countryItem.getThumbnail()).placeholder(R.drawable.progress_animation).into(imageViewFlag);
        }

        return convertView;
    }


    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<GetAllIngredientsWithOutCategoryDataModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(ingredientsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (GetAllIngredientsWithOutCategoryDataModel item : ingredientsListFull) {
                    if (item.getItem().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((GetAllIngredientsWithOutCategoryDataModel) resultValue).getItem();
        }
    };
}