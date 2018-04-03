package me.rickyxe.miniptrdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import me.rickyxe.miniptr.AbsRecyclerAdapter;

public class UserAdapter extends AbsRecyclerAdapter<UserAdapter.TestEntity, UserAdapter.TestViewHolder> {

    public UserAdapter(Context ctx, List datas) {
        super(ctx, datas);
    }

    @Override
    public TestViewHolder getViewHolder(Context ctx, ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.layout_item_view, parent, false);
        TestViewHolder viewHolder = new TestViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void fillData(TestViewHolder holder, List<TestEntity> datas, final int position) {
        if (datas != null && datas.size() > position) {
            TestEntity entity = datas.get(position);
            holder.iconText.setText(entity.getTextA());
            holder.titleText.setText(entity.getTextB());
            holder.contentText.setText(entity.getTextC());
        } else {
            holder.iconText.setText("");
            holder.titleText.setText("");
            holder.contentText.setText("");
        }

        holder.iconText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getAdapterContext(), "icon: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static List<TestEntity> buildDataList() {
        List<TestEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TestEntity entity = new TestEntity();
            entity.setTextA("A.NO " + i);
            entity.setTextB("Title.NO " + i);
            entity.setTextC("Content.No " + i);
            list.add(entity);
        }
        return list;
    }

    /**
     * 测试用ViewHolder
     */
    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView iconText;
        TextView titleText;
        TextView contentText;

        public TestViewHolder(View itemView) {
            super(itemView);

            iconText = (TextView) itemView.findViewById(R.id.text_A);
            titleText = (TextView) itemView.findViewById(R.id.text_B);
            contentText = (TextView) itemView.findViewById(R.id.text_C);

        }
    }

    /**
     * 测试用Entity
     */
    static class TestEntity {
        String textA;
        String textB;
        String textC;

        public String getTextA() {
            return textA;
        }

        public void setTextA(String textA) {
            this.textA = textA;
        }

        public String getTextB() {
            return textB;
        }

        public void setTextB(String textB) {
            this.textB = textB;
        }

        public String getTextC() {
            return textC;
        }

        public void setTextC(String textC) {
            this.textC = textC;
        }

        @Override
        public String toString() {
            return "TestEntity{" +
                    "textA='" + textA + '\'' +
                    ", textB='" + textB + '\'' +
                    ", textC='" + textC + '\'' +
                    '}';
        }
    }
}
