package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<Data> data;
    CustomFilter filter;
    ArrayList<Data> filterList;
    public Adapter(Context ctx, ArrayList<Data> data){
        this.c=ctx;
        this.data = data;
        this.filterList = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return data.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Getting View for listview
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.model,null);
        }
        TextView nameTxt = (TextView) convertView.findViewById(R.id.models);
        TextView artistTxt = (TextView) convertView.findViewById(R.id.artist);
        ImageView arrowImg = (ImageView) convertView.findViewById(R.id.arrow);
        nameTxt.setText(data.get(position).getTitle());
        artistTxt.setText(data.get(position).getCurrArtist());
        arrowImg.setImageResource(R.drawable.arrow);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
        {
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length()>0){
                constraint = constraint.toString().toUpperCase();

                ArrayList<Data> filters = new ArrayList<Data>();

                for (int i=0 ; i<filterList.size();i++){
                    if(filterList.get(i).getTitle().toUpperCase().contains(constraint)) {
                        Data dats = new Data(filterList.get(i).getTitle(), filterList.get(i).getCurrArtist(), filterList.get(i).getCurrDir());
                        filters.add(dats);
                    }
                }
                results.count=filters.size();
                results.values = filters;
            }else {
                results.count=filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<Data>) results.values;
            notifyDataSetChanged();
        }
    }
}


