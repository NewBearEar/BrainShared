package com.example.a123.sharedbrain.fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class FragmentBase extends Fragment{
    @Override
    public void onAttach(Context context) {
        Log.i(this.getClass().getSimpleName(),"onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(),"onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Log.i(this.getClass().getSimpleName(),"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        Log.i(this.getClass().getSimpleName(),"onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(this.getClass().getSimpleName(),"onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(this.getClass().getSimpleName(),"onStop");
        super.onStop();
    }
}
