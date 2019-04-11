package com.example.kashish.picit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    private ListView mListView;
    private RecyclerView rv_main_active,rv_main_inactive;
    private FloatingActionButton camera_fab, gallery_fab,add_grp_fab;

    static String [] active ={"Hiren", "Pratik", "Dhruv", "Narendra", "Piyush", "Priyank"};
    static String [] inactive ={"Kirit", "Miral", "Bhushan", "Jiten", "Ajay", "Kamlesh"};

    static String [] activeIds ={"Hiren", "Pratik", "Dhruv", "Narendra", "Piyush", "Priyank"};
    static String [] inactiveIds ={"Kirit", "Miral", "Bhushan", "Jiten", "Ajay", "Kamlesh"};
//    private
    static ArrayList<String> active_chats,inactive_chats;
    static ArrayList<String> active_chat_ids,inactive_chat_ids;

    static main_rv_adapter_active active_adapter;
    static main_rv_adapter_inactive inactive_adapter;

    static void addGrp(Group g){
        active_chats.add(g.name);
        active_chat_ids.add(g.id);

        active_adapter.notifyDataSetChanged();
    }

    static void active2inactive(int position){

        String chatName = active_chats.remove(position);
        String chatId = active_chat_ids.remove(position);

        inactive_chats.add(0,chatName);
        inactive_chat_ids.add(0,chatId);

        active_adapter.notifyDataSetChanged();
        inactive_adapter.notifyDataSetChanged();
    }

    static void inactive2active(int position){
        String chatName = inactive_chats.remove(position);
        String chatId = inactive_chat_ids.remove(position);

        active_chats.add(0,chatName);
        active_chat_ids.add(0,chatId);

        active_adapter.notifyDataSetChanged();
        inactive_adapter.notifyDataSetChanged();
    }

    void initChats(){
        active_chats = new ArrayList<>(Arrays.asList(active));
        inactive_chats = new ArrayList<>(Arrays.asList(inactive));

        active_chat_ids = new ArrayList<>(Arrays.asList(activeIds));
        inactive_chat_ids = new ArrayList<>(Arrays.asList(inactiveIds));
    }

    void initViews(){
        rv_main_active = (RecyclerView) findViewById(R.id.rv_main_active);
        rv_main_inactive = (RecyclerView) findViewById(R.id.rv_main_inactive);
        camera_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_camera);
        gallery_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_gallery);
        add_grp_fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_addGrp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        chats = new ArrayList<>();

        if(active_chats==null){
            initChats();
        }

        initViews();

        rv_main_active.setLayoutManager(new LinearLayoutManager(this));
        rv_main_inactive.setLayoutManager(new LinearLayoutManager(this));

        active_adapter = new main_rv_adapter_active(active_chats,null,this);
        inactive_adapter = new main_rv_adapter_inactive(inactive_chats,null,this);

        rv_main_active.setAdapter(active_adapter);
        rv_main_inactive.setAdapter(inactive_adapter);

        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, takePic.class));
            }
        });

        gallery_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, gallery.class));
            }
        });

        add_grp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, addGrp.class));
            }
        });

//        mListView = (ListView)findViewById(R.id.listView_chats);
//        mListView2 = (ListView)findViewById(R.id.listView2);

//        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chats));



//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                PopupMenu popupGroup = new PopupMenu(MainActivity.this, view);
////                MenuInflater inflater = popupGroup.getMenuInflater();
////                inflater.inflate(R.menu.grp_action, popupGroup.getMenu());
//                popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()){
//                            case R.id.profile_update1:
//                                Toast.makeText(MainActivity.this, "profle update 1",Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case R.id.profile_update2:
//                                Toast.makeText(MainActivity.this, "profle update 2",Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case R.id.profile_update3:
//                                Toast.makeText(MainActivity.this, "profle update 3",Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case R.id.profile_update4:
//                                Toast.makeText(MainActivity.this, "profle update 4",Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupGroup.inflate(R.menu.grp_action);
//                popupGroup.show();
//                return false;
//            }
//        });
//        mListView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2));

//        ListUtils.setDynamicHeight(mListView);
//        ListUtils.setDynamicHeight(mListView2);
    }


//    public static class ListUtils {
//        public static void setDynamicHeight(ListView mListView) {
//            ListAdapter mListAdapter = mListView.getAdapter();
//            if (mListAdapter == null) {
//                // when adapter is null
//                return;
//            }
//            int height = 0;
//            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//            for (int i = 0; i < mListAdapter.getCount(); i++) {
//                View listItem = mListAdapter.getView(i, null, mListView);
//                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//                height += listItem.getMeasuredHeight();
//            }
//            ViewGroup.LayoutParams params = mListView.getLayoutParams();
//            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
//            mListView.setLayoutParams(params);
//            mListView.requestLayout();
//        }
//    }
}
