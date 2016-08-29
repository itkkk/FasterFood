package it.uniba.di.ivu.sms16.gruppo3.fasterfood.settings_screen;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

//classe per gestire i layout del frag accsettings, usata una classe per unificare e ridurre codice in accsettingsfragment
public class AnimAccSettingsManagement {
    //mLinearLayout gestisce il layout da espandere
    private LinearLayout mLinearLayout;
    //icone per unfold less/more
    private ImageView setIcon;
    //mLinearLayoutHeader gestisce il layout sempre visibile
    private LinearLayout mLinearLayoutHeader;
    //mAnimator gestisce l'animazione e lo slide
    private ValueAnimator mAnimator;

    public void setListeners(){
        setListenerTomLinearLayout();
        setListenerTomLinearLayoutHeader();
    }

    //Gestione listener per settare animazione, dimensioni, slider, visibilità del layout nascosto
    private void setListenerTomLinearLayout(){
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
    }

    //gestione listener del click sul layout sempre visibile con animazione di espansione e collapse
    private void setListenerTomLinearLayoutHeader(){
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
    }

    //gestione espansione settando visibilità al layout nascosto , cambiando icona di unfold e facendo partire l'animazione di slide
    public void expand(){
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);
        setIcon.setImageResource(R.drawable.ic_unfold_less);

        mAnimator.start();
    }

    //gestione collapse settando visibilità off, avendo prima attivato animazione di slide, a fine animazione
    //visibilità e icona di unfold cambiano
    public void collapse(){
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mLinearLayout.setVisibility(View.GONE);
                setIcon.setImageResource(R.drawable.ic_unfold_more);
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

    public void setmLinearLayout(LinearLayout mLinearLayout) {
        this.mLinearLayout = mLinearLayout;
    }

    public void setSetIcon(ImageView setIcon) {
        this.setIcon = setIcon;
    }

    public void setmLinearLayoutHeader(LinearLayout mLinearLayoutHeader) {
        this.mLinearLayoutHeader = mLinearLayoutHeader;
    }
}
