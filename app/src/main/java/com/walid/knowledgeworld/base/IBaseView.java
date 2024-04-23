package com.walid.knowledgeworld.base;

import androidx.annotation.StringRes;

public interface IBaseView {

    void showLoading();

    void showMessage(@StringRes int resId);

    void showMessage(String message);

    void hideLoading();

    void hideKeyboard();

}
