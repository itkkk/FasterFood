package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewTreeObserver;
import android.animation.Animator;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

//Implementa preferenze per città e chain da usare nelle interfacce come la home
//oltre al cambio password e all'attivazione notifiche (es. notifica review post consumazione)
//i salvataggi sono fatti solo tramite bottone SAVE //da implementare col db l'onclick
public class AccSettingsFragment extends android.app.Fragment {

    //mLinearLayout gestisce il layout da espandere
    LinearLayout mLinearLayout;
    //mLinearLayoutHeader gestisce il layout sempre visibile
    LinearLayout mLinearLayoutHeader;
    //mAnimator gestisce l'animazione e lo slide
    ValueAnimator mAnimator;

    LinearLayout mLinearLayout2;
    LinearLayout mLinearLayoutHeader2;
    ValueAnimator mAnimator2;

    LinearLayout mLinearLayout3;
    LinearLayout mLinearLayoutHeader3;
    ValueAnimator mAnimator3;

    LinearLayout mLinearLayout4;
    LinearLayout mLinearLayoutHeader4;
    ValueAnimator mAnimator4;

    ImageView setIcon1;
    ImageView setIcon2;
    ImageView setIcon3;
    ImageView setIcon4;

    Switch notification;
    Button change_psw;
    EditText psw_to_change;

    View layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout=inflater.inflate(R.layout.fragment_acc_settings, container, false);

        mLinearLayout = (LinearLayout) layout.findViewById(R.id.expandable);
        mLinearLayout2 = (LinearLayout) layout.findViewById(R.id.expandable2);
        mLinearLayout3 = (LinearLayout) layout.findViewById(R.id.expandable3);
        mLinearLayout4 = (LinearLayout) layout.findViewById(R.id.expandable4);

        mLinearLayoutHeader = (LinearLayout) layout.findViewById(R.id.header);
        mLinearLayoutHeader2 = (LinearLayout) layout.findViewById(R.id.header2);
        mLinearLayoutHeader3 = (LinearLayout) layout.findViewById(R.id.header3);
        mLinearLayoutHeader4= (LinearLayout) layout.findViewById(R.id.header4);

        //icone per unfold less/more
        setIcon1 = (ImageView) layout.findViewById(R.id.imgSettingsUnfold);
        setIcon2 = (ImageView) layout.findViewById(R.id.imgSettingsUnfold2);
        setIcon3 = (ImageView) layout.findViewById(R.id.imgSettingsUnfold3);
        setIcon4 = (ImageView) layout.findViewById(R.id.imgSettingsUnfold4);

        notification = (Switch) layout.findViewById(R.id.switch_notification);

        change_psw = (Button) layout.findViewById(R.id.psw_btn);
        psw_to_change = (EditText) layout.findViewById(R.id.password_old);

        //inizializzato come in schermata home e locals
        Spinner spinner = (Spinner) layout.findViewById(R.id.favSpinnerChain);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.chains, R.layout.spinner_element);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //gestione listener dello switch di notifiche
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    notification.setText(layout.getResources().getString(R.string.notification_on));
                }else{
                    notification.setText(layout.getResources().getString(R.string.notification_off));
                }
            }

        });

        //gestione listener bottone in cambio password
        //se il bottone è settato su change allora si controlla la password //da implementare con db
        //se la password è verificata viene richiesta nuova password, il bottone è settato in save
        //quindi viene confermata
        change_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(psw_control() && change_psw.getText().toString().equals("Change"))
                    change_psw_btn();
                else
                    confirm_psw_btn();
            }
        });

        //Gestione listener per settare animazione, dimensioni, slider, visibilità del layout nascosto
        mLinearLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
                        return true;
                    }
                });

        //gestione listener del click sul layout sempre visibile con animazione di espansione e collapse
        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility()==View.GONE){
                    expand();
                }else{
                    collapse();
                }
            }
        });

        //Ogni layout ha la sua gestione viste le differenti funzionalità
        //DA IMPLEMENTARE CLASSE UNICA PER RIDURRE CODICE

        //gestione seconda impostazione
        mLinearLayout2.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout2.getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout2.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout2.measure(widthSpec, heightSpec);

                        mAnimator2 = slideAnimator2(0, mLinearLayout2.getMeasuredHeight());
                        return true;
                    }
                });

        mLinearLayoutHeader2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout2.getVisibility()==View.GONE){
                    expand2();
                }else{
                    collapse2();
                }
            }
        });

        //gestione terza impostazione

        mLinearLayout3.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout3.getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout3.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout3.measure(widthSpec, heightSpec);

                        mAnimator3 = slideAnimator3(0, mLinearLayout3.getMeasuredHeight());
                        return true;
                    }
                });

        mLinearLayoutHeader3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout3.getVisibility()==View.GONE){
                    expand3();
                }else{
                    collapse3();
                }
            }
        });

        //gestione quarta impostazione

        mLinearLayout4.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLinearLayout4.getViewTreeObserver().removeOnPreDrawListener(this);
                        mLinearLayout4.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        mLinearLayout4.measure(widthSpec, heightSpec);

                        mAnimator4 = slideAnimator4(0, mLinearLayout4.getMeasuredHeight());
                        return true;
                    }
                });

        mLinearLayoutHeader4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout4.getVisibility()==View.GONE){
                    expand4();
                }else{
                    collapse4();
                }
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //per evitare sovvrascrizioni in fase di animazione
        psw_to_change.setHint(layout.getResources().getString(R.string.pass_hint));
    }

    //gestione espansione settando visibilità al layout nascosto , cambiando icona di unfold e facendo partire l'animazione di slide
    private void expand() {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);
        setIcon1.setImageResource(R.drawable.ic_unfold_less);

        mAnimator.start();
    }

    //gestione collapse settando visibilità off, avendo prima attivato animazione di slide, a fine animazione
    //visibilità e icona di unfold cambiano
    private void collapse() {
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout.setVisibility(View.GONE);
                setIcon1.setImageResource(R.drawable.ic_unfold_more);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    //Gestione animazione di slide
    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //Come in precedenza ogni impostazione ha la sua gestione di animazione

    //Gestione seconda impstazione
    private void expand2() {
        //set Visible
        mLinearLayout2.setVisibility(View.VISIBLE);
        setIcon2.setImageResource(R.drawable.ic_unfold_less);

		/* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/

        mAnimator2.start();
    }

    private void collapse2() {
        int finalHeight = mLinearLayout2.getHeight();

        ValueAnimator mAnimator = slideAnimator2(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mLinearLayout2.setVisibility(View.GONE);
                setIcon2.setImageResource(R.drawable.ic_unfold_more);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator2(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mLinearLayout2.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout2.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //Gestione terza impstazione
    private void expand3() {
        //set Visible
        mLinearLayout3.setVisibility(View.VISIBLE);
        setIcon3.setImageResource(R.drawable.ic_unfold_less);
		/* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/

        mAnimator3.start();
    }

    private void collapse3() {
        int finalHeight = mLinearLayout3.getHeight();

        ValueAnimator mAnimator = slideAnimator3(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout3.setVisibility(View.GONE);
                setIcon3.setImageResource(R.drawable.ic_unfold_more);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator3(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mLinearLayout3.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout3.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //Gestione quarta impstazione
    private void expand4() {
        //set Visible
        mLinearLayout4.setVisibility(View.VISIBLE);
        setIcon4.setImageResource(R.drawable.ic_unfold_less);
		/* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/

        mAnimator4.start();
    }

    private void collapse4() {
        int finalHeight = mLinearLayout4.getHeight();

        ValueAnimator mAnimator = slideAnimator4(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout4.setVisibility(View.GONE);
                setIcon4.setImageResource(R.drawable.ic_unfold_more);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator4(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mLinearLayout4.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout4.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //da implementare per controllo vecchia password (con db)
    private boolean psw_control(){
        return true;
    }

    //setta il bottone per salvare nuova password, cambia anche l'edit text per acquisizione password
    private void change_psw_btn(){
        change_psw.setText(layout.getResources().getString(R.string.pass_btn_save));
        Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_confirm), Toast.LENGTH_SHORT).show();
        psw_to_change.setText("");
        psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
    }

    //setta il bottone per confermare nuova password.
    //se la password nuova è valida allora viene salvata e il bottone è settato come inizio in change
    //altrimenti il bottone non è modificato e la password non è salvata
    private void confirm_psw_btn(){
        if(passValid(psw_to_change.getText().toString())){
            change_psw.setText(layout.getResources().getString(R.string.pass_btn_change));
            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_changed), Toast.LENGTH_SHORT).show();
            psw_to_change.setText("");
            psw_to_change.setHint(layout.getResources().getString(R.string.pass_hint));
        }else{
            Toast.makeText(getActivity(), layout.getResources().getString(R.string.pass_wrong), Toast.LENGTH_SHORT).show();
            psw_to_change.setText("");
            psw_to_change.setHint(layout.getResources().getString(R.string.new_pass));
        }
    }

    //controllo base della password da modificare
    private boolean passValid(String pass){
        return pass.length() > 4;
    }
}
