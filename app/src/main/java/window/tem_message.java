package window;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.example.sign_up.R;

public class tem_message extends Dialog implements View.OnClickListener {
    public TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public tem_message(Context context) {
        super(context);
        this.mContext = context;
    }

    public tem_message(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public tem_message(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        //FIXME      这个地方被wrl注释掉了
        this.listener = listener;
    }

    protected tem_message(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public tem_message setTitle(String title) {
        this.title = title;
        return this;
    }

    public tem_message setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public tem_message setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tem_message);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public tem_message setMessage(String s) {
        contentTxt = (TextView) findViewById(R.id.url);
        return this;
    }

    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

         contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    public TextView getContentTxt() {
        return contentTxt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if(listener != null){
                    listener.onClickCancel(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClickSubmit(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        //        void onClickCancel(Dialog dialog, boolean confirm);
        void onClickSubmit(Dialog dialog, boolean confirm);
    }
}
