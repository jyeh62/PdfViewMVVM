package jyeh.co.kr.pdfviewmvvm;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.PropertyChangeRegistry;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by jychoi on 2017. 11. 28..
 */

public class ViewModel extends BaseObservable implements IViewModel {
    private Activity activity;
    private IViewModel.View view;
    private PdfModel model;

    private Bitmap bitmap;
    private ObservableBoolean isPreviousButtonEnabled = new ObservableBoolean(true);
    private ObservableBoolean isNextButtonEnabled = new ObservableBoolean(true);
    @Bindable
    private String title;
    private PropertyChangeRegistry registry =
            new PropertyChangeRegistry();

    public ViewModel(Activity activity, PdfModel model) {
        this.activity = activity;
        this.model = model;
    }
    @Override
    public void setView(IViewModel.View view) {
        this.view = view;
    }

    @Override
    public void showPage(int index) {
        setImageBitmap(model.getCurrentPage(index));
        updateUi();
    }

    @Override
    public void onClickButton(boolean isNext) {
        int index = model.getIndex();
        if (isNext) {
            showPage(index + 1);
        } else {
            showPage(index - 1);
        }
    }
    private void updateUi() {
        int index = model.getIndex();
        int pageCount = model.getPageCount();

        setPreviousButtonEnabled(0 != index);
        setNextButtonEnabled(index + 1 < pageCount);
        setTitle(activity.getString(R.string.app_name_with_index, index + 1, pageCount));
    }
    @Bindable
    public boolean isPreviousButtonEnabled() {
        return isPreviousButtonEnabled.get();
    }
    @Bindable
    public void setPreviousButtonEnabled(boolean previousButtonEnabled) {
        isPreviousButtonEnabled.set(previousButtonEnabled);
        registry.notifyChange(this,BR.previousButtonEnabled);
    }
    @Bindable
    public boolean isNextButtonEnabled() {
        return isNextButtonEnabled.get();
    }
    @Bindable
    public void setNextButtonEnabled(boolean nextButtonEnabled) {
        isNextButtonEnabled.set(nextButtonEnabled);
        registry.notifyChange(this,BR.nextButtonEnabled);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Bindable
    public Bitmap getImageBitmap() {
        return bitmap;
    }


    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        registry.notifyChange(this,BR.imageBitmap);
    }
    @BindingAdapter("load")
    public static void setImageBitmap(ImageView iv, Bitmap bitmap) {
        iv.setImageBitmap(bitmap);
    }

    @BindingAdapter("enabled")
    public static void setEnabled(Button v, boolean isEnabled) {
        v.setEnabled(isEnabled);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }


    @Override
    public void attach() {
        model.openPdfFile(activity.getBaseContext());
        showPage(0);
    }

    @Override
    public void detach() {
        try {
            model.closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
