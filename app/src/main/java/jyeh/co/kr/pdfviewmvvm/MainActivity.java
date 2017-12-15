package jyeh.co.kr.pdfviewmvvm;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jyeh.co.kr.pdfviewmvvm.databinding.MainActivityBinding;

/**
 * Created by jychoi on 2017. 11. 28..
 */

public class MainActivity extends Activity implements View.OnClickListener, IViewModel.View {
    private ImageView mImageView;
    private Button mButtonPrevious;
    private Button mButtonNext;

    private PdfModel model;
    private ViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainActivityBinding dataBinding
                = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mImageView = dataBinding.contentsView;
        //findViewById(R.id.contents_view);
        mButtonNext = dataBinding.buttonNext;
        //findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(this);

        //mButtonPrevious = findViewById(R.id.button_prev);
        mButtonPrevious = dataBinding.buttonPrev;
        mButtonPrevious.setOnClickListener(this);

        model = new PdfModel();
        viewModel = new ViewModel(this, model);
        viewModel.setView(this);
        dataBinding.setVm(viewModel);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_prev: {
                // Move to the previous page
                viewModel.onClickButton(false);
                break;
            }
            case R.id.button_next: {
                // Move to the next page
                viewModel.onClickButton(true);
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.attach();

    }

    @Override
    public void onStop() {
        viewModel.detach();
        super.onStop();
    }
}

