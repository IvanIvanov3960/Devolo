package task.devolo.com.devolotask;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageFragment extends Fragment {

    ImageView imageView;
    GridView gridView;
    Bundle bundle;
    ArrayList<String> urlList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlList = new ArrayList<>();
        bundle = this.getArguments();
        urlList = bundle.getStringArrayList("avatar_url");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);

        gridView = view.findViewById(R.id.grid);

        imageView = view.findViewById(R.id.imageUrl);

        gridView.setAdapter(new ImageAdapter(getActivity()));

        return view;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList urls = urlList;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return urls.size();
        }

        @Override
        public String getItem(int position) {
            return urls.get(position).toString();
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            String url = getItem(position);
            Picasso.with(mContext)
                    .load(url)
                    .fit()
                    .centerCrop().into(imageView);
            return imageView;
        }
    }


}
