package jyeh.co.kr.pdfviewmvvm;

import android.graphics.Bitmap;

/**
 * Created by jychoi on 2017. 11. 28..
 */

public interface IViewModel {

    interface View {
        void setTitle(CharSequence title);
    }

    void setView(IViewModel.View view);

    void showPage(int index);
    void onClickButton(boolean isNext);

    void attach();
    void detach();
}
