package it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.HomeActivity;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.db.ScambiaDati;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.summary_screen.SummaryFragment;

public class MenuFragment extends Fragment {
    private Toolbar mBasketToolbar;
    private TextView txtName;
    private static Menu menu;
    private RecyclerAdapterRVMenu mAdapterRVMenu;
    private RecyclerView recyclerView;
    private static String chain;
    private ArrayList<String> nameList;
    private ArrayList<String> priceList;
    private ArrayList<String> quantityList;
    private ArrayList<Integer> positionList;
    private boolean open;
    private boolean updating; //se è 1 stiamo aggiornando un vecchio ordine
    private String name;
    private String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu,container,false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBasketToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        txtName = (TextView) getActivity().findViewById(R.id.txtNameMenu);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewMenu);
        Bundle bundle = getArguments();

        open = bundle.getBoolean("open");
        name= bundle.getString("name");
        updating = bundle.getBoolean("updating");
        if(updating){
            date = bundle.getString("date");
        }

        txtName.setText(name + " - Menu");

        menu = ScambiaDati.getMenu();
        if(menu.getMenu().size() == 0){
            Snackbar.make(getView(), getResources().getString(R.string.not_connected), Snackbar.LENGTH_LONG).show();
        }
        else {

            chain = bundle.getString("chain");

            ArrayList<String> value = ((HomeActivity)getActivity()).getMenuSpinnerValue();
            if(value != null) {
                String[] mSpinnerValue = new String[value.size()];
                for (int i = 0; i < value.size(); i++) {
                    mSpinnerValue[i] = value.get(i);
                }
                mAdapterRVMenu = new RecyclerAdapterRVMenu(getActivity(), getData(), getActivity(), mSpinnerValue);
            }else{
                mAdapterRVMenu = new RecyclerAdapterRVMenu(getActivity(), getData(), getActivity());
            }

            if (recyclerView != null) {
                recyclerView.setAdapter(mAdapterRVMenu);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu_fasterfood,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id) {
            case R.id.basketFF:
                if(checkQuantity()) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("nameList", nameList);
                    bundle.putStringArrayList("priceList", priceList);
                    bundle.putStringArrayList("quantityList", quantityList);
                    bundle.putIntegerArrayList("positionList", positionList);
                    bundle.putBoolean("open", open);
                    bundle.putString("name",name);
                    bundle.putString("chain", chain);
                    bundle.putBoolean("updating", updating);
                    if(updating){
                        bundle.putString("date",date);
                    }
                    bundle.putBoolean("state", false);
                    bundle.putString("posti", getArguments().getString("posti"));
                    bundle.putInt("position", getArguments().getInt("position"));

                    SummaryFragment summaryFragment = new SummaryFragment();
                    summaryFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_exit_right,
                            R.animator.slide_in_right,R.animator.slide_exit_left);
                    transaction.replace(R.id.fragment, summaryFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    /*
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment,summaryFragment)
                            .addToBackStack("")
                            .commit();*/
                } else {
                    Snackbar.make(getView(), getResources().getString(R.string.atLeastOneProduct),Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }

    private boolean checkQuantity() {
        boolean check = false;
        nameList = new ArrayList<>();
        priceList= new ArrayList<>();
        quantityList = new ArrayList<>();
        positionList = new ArrayList<>();
            for (int i = 0; i < mAdapterRVMenu.getItemCount(); i++) { //  Scansione di ogni singolo item della RecyclerView
                if (Integer.parseInt(mAdapterRVMenu.getSingleSpinnerValue(i)) != 0) {
                    check = true;
                    nameList.add(mAdapterRVMenu.getProductName(i));
                    priceList.add(mAdapterRVMenu.getProductPrice(i));
                    quantityList.add(mAdapterRVMenu.getSingleSpinnerValue(i));
                    positionList.add(i);
                }
            }
        return check; // Ritorna falso se non si è entrati nella condizione
    }

    @Override
    public void onPause() {
        super.onPause();
        //salvo i valori dello spinner
        ArrayList<String> value = new ArrayList<>();
        if(mAdapterRVMenu != null) {
            for (int i = 0; i < mAdapterRVMenu.getItemCount(); i++) {
                value.add(mAdapterRVMenu.getSingleSpinnerValue(i));
            }
            ((HomeActivity) getActivity()).setMenuSpinnerValue(value);
        }
    }

    static List<SettingsElementRVMenu> getData(){
        List<SettingsElementRVMenu> data = new ArrayList<>();
        if(menu != null) {
            for (int i = 0; i < menu.getMenu().size(); i++) {
                SettingsElementRVMenu current = new SettingsElementRVMenu();
                current.setmName(menu.getMenu().get(i).getNome());
                current.setmPrice(menu.getMenu().get(i).getPrezzo());
                current.setmDescription(menu.getMenu().get(i).getDescrizione());
                current.setmImage(menu.getMenu().get(i).getImage());
                current.setChain(chain);
                data.add(current);
            }
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        mBasketToolbar.getMenu().clear();
        super.onDestroyView();
    }

}
