package it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private static Menu menu;
    private RecyclerAdapterRVMenu mAdapterRVMenu;
    private static String chain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_menu,container,false);

        mBasketToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        mBasketToolbar.inflateMenu(R.menu.option_menu_fasterfood);

        menu = ScambiaDati.getMenu();
        Bundle bundle = getArguments();
        chain = bundle.getString("chain");

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerViewMenu);
        mAdapterRVMenu = new RecyclerAdapterRVMenu(getActivity(),getData(),getActivity());
        if (recyclerView != null) {
            recyclerView.setAdapter(mAdapterRVMenu);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnPurchase = (Button) getView().findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkQuantity()) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new SummaryFragment()) // TODO Non va fatto new SummaryFragment
                            .addToBackStack("")
                            .commit();
                } else {
                    // System.out.println("Non ho acquistato nulla"); // Andrebbe creato qualcosa
                }
            }
        });
    }

    private boolean checkQuantity() {
        boolean check = false;
            for (int i = 0; i < mAdapterRVMenu.getItemCount(); i++) { //  Scansione di ogni singolo item della RecyclerView
                if (Integer.parseInt(mAdapterRVMenu.getSingleSpinnerValue(i)) != 0) {
                    check = true; // Se almeno un valore all'intero dello spinner è != 0, allora l'utente vuole acquistare qualcosa
                }
            }
        return check; // Ritorna falso se non si è entrati nella condizione
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
