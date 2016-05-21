package ro.trenulmeu.mobile.controls;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;

/**
 * A container Object that shows, or hides, a Fragment.
 */
public class ExpandFragment extends LinearLayout {

    private Fragment fragment;
    private String tag;
    private boolean hidden = false;

    private TextView title;
    private ImageView toggle;
    private LinearLayout slot;

    public ExpandFragment(Context context) {
        super(context);
        init();
    }

    public ExpandFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.control_expnad_fragment, this);

        title = (TextView) v.findViewById(R.id.title);
        toggle = (ImageView) v.findViewById(R.id.toggle);
        slot = (LinearLayout) v.findViewById(R.id.fragment_slot);

        v.findViewById(R.id.header).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setHidden(!hidden);
            }
        });
    }


    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = AppContext.activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fragment_slot, fragment, tag);
        ft.commit();

        this.hidden = false;
        this.fragment = fragment;
        this.tag = tag;
        toggle.setImageResource(R.drawable.fold);
    }

    public void setTitle(@StringRes int id) {
        title.setText(id);
    }

    public void setHidden(boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            UpdateUI();
        }
    }

    private void UpdateUI() {
        toggle.setImageResource(hidden ? R.drawable.expand : R.drawable.fold);
        FragmentTransaction ft = AppContext.activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        if (hidden) {
            ft.hide(fragment);
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }
}
