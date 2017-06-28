/**
 * 
 */
package com.rockerhieu.emojicon;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import com.rockerhieu.emojicon.custom.ui.EmojiLinearLayout;

/**
 *@author mikes
 *@version 2016-2-19 上午11:45:36
 *Note:
 *
 */
public class EmojiconCustomGridFragment extends EmojiconGridFragment implements OnItemClickListener{
	public static boolean mIsCustomExp = false;
	private EmojiconRecents mRecents;
	private EventListener mListener;
	
	public interface EventListener {
		void onEmojiSelected(String res);
	}

    protected static EmojiconCustomGridFragment newInstance(EmojiconRecents recents,boolean isCustomExp) {
    	mIsCustomExp = isCustomExp;
    	EmojiconCustomGridFragment fragment = new EmojiconCustomGridFragment();
    	fragment.setRecents(recents);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return inflater.inflate(com.rockerhieu.emojicon.R.layout.emoji_tab_emoji_qq, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	((EmojiLinearLayout)view).setEventListener(mListener);
    	((EmojiLinearLayout)view).setRecents(mRecents);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnEmojiconClickedListener) {
        	mListener = (EventListener) activity;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener) {
        	mListener = (EventListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + EventListener.class.getSimpleName());
        }
    }
    
    @Override
    public void onDetach() {
    	mListener = null;
        super.onDetach();
    }
    
    private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }
}
