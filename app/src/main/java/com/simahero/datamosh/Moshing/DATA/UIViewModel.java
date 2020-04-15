package com.simahero.datamosh.Moshing.DATA;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.simahero.datamosh.Fragments.CREATE.PFrame;

import java.util.ArrayList;

public class UIViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PFrame>> framelist = new MutableLiveData<>();
    private MutableLiveData<Integer> pFrameTask = new MutableLiveData<>();
    private MutableLiveData<Integer> sectionEnd = new MutableLiveData<>();
    private MutableLiveData<Integer> section = new MutableLiveData<>();

    private MutableLiveData<String> preset = new MutableLiveData<>();
    private MutableLiveData<Bitmap> video1 = new MutableLiveData<>();
    private MutableLiveData<Bitmap> video2 = new MutableLiveData<>();
    private MutableLiveData<Bitmap> wb = new MutableLiveData<>();
    private MutableLiveData<Integer> count = new MutableLiveData<>();

    private MutableLiveData<String> selectedFrame = new MutableLiveData<>();
    private MutableLiveData<String> selectedTime = new MutableLiveData<>();
    private MutableLiveData<String> selectedEnd = new MutableLiveData<>();

    private MutableLiveData<Uri> cutVid = new MutableLiveData<>();
    private MutableLiveData<String> moshedVideoName = new MutableLiveData<>();
    private MutableLiveData<String> moshedVideoPath = new MutableLiveData<>();


    public MutableLiveData<Integer> getCount() { return count; }
    public void setCount (Integer s) { count.setValue(s); }

    public MutableLiveData<String> getPreset() { return preset; }
    public void setPreset (String p) { preset.setValue(p); }

    public MutableLiveData<Bitmap> getVideo1(){
        return video1;
    }
    public MutableLiveData<Bitmap> getVideo2(){
        return video2;
    }
    public MutableLiveData<String> getSelectedFrame() { return selectedFrame; }
    public void setSelectedFrame(String s){ selectedFrame.setValue(s); }
    public MutableLiveData<String> getSelectedTime() { return selectedTime; }
    public void setSelectedTime(String s){ selectedTime.setValue(s); }
    public MutableLiveData<String> getSelectedEnd() { return selectedEnd; }
    public void setSelectedEnd(String s){ selectedEnd.setValue(s); }
    public MutableLiveData<Bitmap> getWb(){
        return wb;
    }

    public MutableLiveData<Uri> getCutVid() {return cutVid;}
    public MutableLiveData<String> getMoshedVideoPath() { return moshedVideoPath; }
    public MutableLiveData<String> getMoshedVideoName() { return moshedVideoName; }
    public LiveData<ArrayList<PFrame>> getFramelist() { return framelist; }
    public void setFramelist (ArrayList<PFrame> list) {framelist.setValue(list);}

    public MutableLiveData<Integer> getpFrameTask() { return pFrameTask; }
    public void setpFrameTask(int i) { pFrameTask.setValue(i); }
    public MutableLiveData<Integer> getSectionEnd() { return sectionEnd; }
    public void setSectionEnd(int i) { sectionEnd.setValue(i); }
    public MutableLiveData<Integer> getSection() { return section; }
    public void setSection(int i) { section.setValue(i); }

    public void setCutVid(Uri uri) { cutVid.setValue(uri); }
    public void setMoshedVideoPath(String s) {
        moshedVideoPath.setValue(s); }
    public void setMoshedvidname(String s) {
        moshedVideoName.setValue(s); }

    public void cleanUp(){
        video1.setValue(null);
        video2.setValue(null);
        selectedFrame.setValue(null);
        wb.setValue(null);
        cutVid.setValue(null);
        moshedVideoPath.setValue(null);
        moshedVideoName.setValue(null);
        selectedFrame.setValue(null);
        selectedTime.setValue(null);
        selectedEnd.setValue(null);
        preset.setValue(null);
        count.setValue(null);
    }
}
