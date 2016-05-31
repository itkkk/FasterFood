package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SearchFragment extends Fragment {
    private ActionBar actionBar;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem menu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        menu = ((HomeActivity)getActivity()).mNavigationView.getMenu().findItem(R.id.nav_home);
        menu.setChecked(true);

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chains, R.layout.spinner_element);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //codice nuovo
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        final String[] array = {"prova", "ciao", "gruppo 3", "altro", "prova2", "prova3"};
        mAdapter = new AdapterRestaurantList(array, getActivity().getApplicationContext());
        recyclerView.setAdapter(mAdapter);


        // Angelo TODO: Si potrebbe "packeggiare" tutto il listener perche cosi fa schifo. Bisogna farlo dopo aver fatto il bind con il DB.
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String s = array[position];
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new RestaurantDetailFragment())
                        .addToBackStack("") //TODO che cosa e la lista
                        .commit();
                menu.setChecked(false);

               //TODO: passare s con intent e inserire questa rig in restauntdetailfragment
                actionBar.setTitle(s);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SearchFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SearchFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}