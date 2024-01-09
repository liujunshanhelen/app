package com.example.sign_up;

import android.animation.Animator;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

public class MenuButton{
    //中心按钮
    private ImageButton button;
    // 子按钮列表
    private List<ImageButton> buttonItems = new ArrayList<ImageButton>(3);
    // 标识当前按钮弹出与否，1代表已经未弹出，-1代表已弹出
    private int flag = 1;
    private int button_click_num = 0;

    public MenuButton(ImageButton button,ImageButton button1,ImageButton button2,ImageButton button3){
        // 实例化按钮并设立监听
        this.button = button;
        // 将子按钮们加入列表中
        buttonItems.add(button1);
        buttonItems.add(button2);
        buttonItems.add(button3);
    }
    public void ClickButton(){
        //记录button被触发的次数，奇数弹出，偶数收回
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radius = 300;//这个值是弹出的圆弧的范围
                button_click_num+=1;
                System.out.println(button_click_num);
                if(button_click_num%2==1){
                    for (int i = 0; i < buttonItems.size(); i++) {
                        ObjectAnimator objAnimatorX;//向X轴方向移动
                        ObjectAnimator objAnimatorY;//向Y轴方向移动
                        ObjectAnimator objAnimatorR;//旋转
                        //将按钮设置成可见
                        buttonItems.get(i).setVisibility(View.VISIBLE);
                        float distanceX = (float) (flag * radius * (Math.cos(getAngle(buttonItems.size(), i))));
                        float distanceY = (float) (flag * radius * (Math.sin(getAngle(buttonItems.size(), i))));
                        // X方向移动
                        objAnimatorX = ObjectAnimator.ofFloat(buttonItems.get(i), "x", buttonItems.get(i).getX(), buttonItems.get(i).getX() + distanceX);
                        objAnimatorX.setDuration(200);
                        objAnimatorX.setStartDelay(100);
                        objAnimatorX.start();
                        // Y方向移动
                        objAnimatorY = ObjectAnimator.ofFloat(buttonItems.get(i), "y", buttonItems.get(i).getY(), buttonItems.get(i).getY() + distanceY);
                        objAnimatorY.setDuration(200);
                        objAnimatorY.setStartDelay(100);
                        objAnimatorY.start();
                        // 按钮旋转
                        objAnimatorR = ObjectAnimator.ofFloat(buttonItems.get(i), "rotation", 0, 360);
                        objAnimatorR.setDuration(200);
                        objAnimatorY.setStartDelay(100);
                        objAnimatorR.start();
                    }
                }else{
                    for (int i = 0; i < buttonItems.size(); i++) {
                        ObjectAnimator objAnimatorX;//向X轴方向移动
                        ObjectAnimator objAnimatorY;//向Y轴方向移动
                        ObjectAnimator objAnimatorR;//旋转
                        float distanceX = (float) (flag * radius * (Math.cos(getAngle(buttonItems.size(), i))));
                        float distanceY = (float) (flag * radius * (Math.sin(getAngle(buttonItems.size(), i))));
                        // X方向移动
                        objAnimatorX = ObjectAnimator.ofFloat(buttonItems.get(i), "x", buttonItems.get(i).getX(), buttonItems.get(i).getX() - distanceX);
                        objAnimatorX.setDuration(200);
                        objAnimatorX.setStartDelay(100);
                        objAnimatorX.start();
                        // Y方向移动
                        objAnimatorY = ObjectAnimator.ofFloat(buttonItems.get(i), "y", buttonItems.get(i).getY(), buttonItems.get(i).getY() - distanceY);
                        objAnimatorY.setDuration(200);
                        objAnimatorY.setStartDelay(100);
                        objAnimatorY.start();
                        // 按钮旋转
                        objAnimatorR = ObjectAnimator.ofFloat(buttonItems.get(i), "rotation", 0, 360);
                        objAnimatorR.setDuration(200);
                        objAnimatorY.setStartDelay(100);
                        objAnimatorR.start();
                        if(i==buttonItems.size()-1){
                            objAnimatorX.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) { }
                                @Override
                                public void onAnimationRepeat(Animator animation) { }
                                @Override
                                public void onAnimationCancel(Animator animation) { }
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    for (int j = 0; j < buttonItems.size(); j++) {
                                        buttonItems.get(j).setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    }
                }

            }
            //返回每个按钮应该出现的角度(弧度单位)
            private double getAngle(int total,int index){
                return Math.toRadians(90/(total-1)*index+90+90);
            }
        });
    }
}

