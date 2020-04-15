package com.simahero.datamosh.Moshing.DATA;

import android.net.Uri;
import androidx.lifecycle.ViewModel;

public class URIS extends ViewModel {

    public static Uri v1;
    public static Uri v2;
    public static Uri v3;
    public static Uri v4;
    public static Uri v5;
    public static Uri v6;

    public static Uri wb;

    public static void cleanUp(){
        v1 = null;
        v2 = null;
        v3 = null;
        v4 = null;
        v5 = null;
        v6 = null;
        wb = null;
    }

}
