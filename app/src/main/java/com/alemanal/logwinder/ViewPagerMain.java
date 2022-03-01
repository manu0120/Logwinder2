package com.alemanal.logwinder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alemanal.logwinder.Utilidades.Utilidades;
import com.alemanal.logwinder.fragments.Fragment9;
import com.alemanal.logwinder.fragmentsMain.FragmentTips;
import com.alemanal.logwinder.fragmentsMain.Home;
import com.alemanal.logwinder.fragmentsMain.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPagerMain extends FragmentActivity {
    //Dicta el número de paginas
    private static final int NUM_PAGES = 4;
    //Pager widget, que otorga animacion y posibilidad de swipear lateralmente
    private ViewPager2 view_pager;
    //El adapter que provee las páginas al ViewPager
    private FragmentStateAdapter pagerAdapter;
    BottomNavigationView btNav;
    public static HashMap<String, Boolean> data = new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_main);

        data= setDataFromDataBase();
        // Instanciacion del ViewPager2 y PagerAdapter.
        view_pager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        view_pager.setAdapter(pagerAdapter);
        btNav = findViewById(R.id.bottom_navigation);
        btNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        item.setChecked(true);
                        view_pager.setCurrentItem(0);
                        break;
                    case R.id.tips:
                        item.setChecked(true);
                        view_pager.setCurrentItem(1);
                        break;
                    case R.id.profile:
                        item.setChecked(true);
                        view_pager.setCurrentItem(2);
                        break;
                    case R.id.settings:
                        item.setChecked(true);
                        view_pager.setCurrentItem(3);
                        break;
                }
                return false;
            }

        });

        view_pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                btNav.getMenu().getItem(0).setChecked(false);
                btNav.getMenu().getItem(position).setChecked(true);
                MenuItem prevMenuItem = btNav.getMenu().getItem(position);
                super.onPageSelected(position);

            }
        });

//        Gson gson = new Gson();
//        JsonObject json = gson.toJsonTree(data).getAsJsonObject();
//        try {
//            FileOutputStream fos = getApplicationContext().openFileOutput("json.json",Context.MODE_PRIVATE);
//            fos.write(data.toString().getBytes(),0,data.toString().length());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onBackPressed() {
        if (view_pager.getCurrentItem() == 0) {
            // Si el usuario esta en el primer paso, le da la posibilidad de volver.
            // lo cual llama al metodo finish() el cual sale de la aplicacion
            super.onBackPressed();
        } else {
            // Sino vuelve a la página anterior
            view_pager.setCurrentItem(view_pager.getCurrentItem() - 1);
        }
    }

    //Un simple adaptador el cual representa NUM_PAGES objetos de pagina en secuencia.
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case 0:
                    return new Home();
                case 1:
                    return new FragmentTips();
                case 2:
                    return new Profile();
                case 3:
                default:
                    return new Home();
            }
        }
        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onPause() {
        try {
            guardarDatos(data, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    public static void guardarDatos(HashMap<String, Boolean> data, Context context) throws IOException {
        SQLite sqlite = new SQLite(context,"Datos",null,1);
        if(data.get("frigo")) {
            ArrayList<String> arrFrigo = new ArrayList<String>();
            arrFrigo.add("t");
            if (data.get("frigoA3")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            if (data.get("frigoA2")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            if (data.get("frigoA1")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            if (data.get("frigoA")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            if (data.get("frigoB")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            if (data.get("frigoC")) {
                arrFrigo.add("t");
            } else arrFrigo.add("f");
            sqlite.insert_frig_db(arrFrigo.get(0),arrFrigo.get(1),arrFrigo.get(2),arrFrigo.get(3),arrFrigo.get(4),arrFrigo.get(5),arrFrigo.get(6));
        }else sqlite.insert_frig_db("f","f","f","f","f","f","f");

        if(data.get("conge")) {
            ArrayList<String> arrCong = new ArrayList<String>();
            arrCong.add("t");

            if (data.get("congeA2")) {
                arrCong.add("t");
            } else arrCong.add("f");
            if (data.get("congeA1")) {
                arrCong.add("t");
            } else arrCong.add("f");
            if (data.get("congeA")) {
                arrCong.add("t");
            } else arrCong.add("f");
            if (data.get("congeB")) {
                arrCong.add("t");
            } else arrCong.add("f");
            if (data.get("congeC")) {
                arrCong.add("t");
            } else arrCong.add("f");
            if (data.get("congeD")) {
                arrCong.add("t");
            } else arrCong.add("f");
            sqlite.insert_conge_db(arrCong.get(0),arrCong.get(1),arrCong.get(2),arrCong.get(3),arrCong.get(4),arrCong.get(5),arrCong.get(6));
        }else sqlite.insert_conge_db("f","f","f","f","f","f","f");

        if(data.get("vitro")) {
            ArrayList<String> arrVitro = new ArrayList<String>();
            arrVitro.add("t");
            if (data.get("vitroRad")) {
                arrVitro.add("t");
            } else arrVitro.add("f");
            if (data.get("vitroEle")) {
                arrVitro.add("t");
            } else arrVitro.add("f");
            if (data.get("vitroInd")) {
                arrVitro.add("t");
            } else arrVitro.add("f");
            sqlite.insert_vitro_db(arrVitro.get(0),arrVitro.get(1),arrVitro.get(2),arrVitro.get(3));
        }else sqlite.insert_vitro_db("f","f","f","f");

        if(data.get("lava")) {
            ArrayList<String> arrLava = new ArrayList<String>();
            arrLava.add("t");
            if (data.get("lavaA3")) {
                arrLava.add("t");
            } else arrLava.add("f");
            if (data.get("lavaA2")) {
                arrLava.add("t");
            } else arrLava.add("f");
            if (data.get("lavaA1")) {
                arrLava.add("t");
            } else arrLava.add("f");
            if (data.get("lavaA")) {
                arrLava.add("t");
            } else arrLava.add("f");
            if (data.get("lavaB")) {
                arrLava.add("t");
            } else arrLava.add("f");
            if (data.get("lavaC")) {
                arrLava.add("t");
            } else arrLava.add("f");
            sqlite.insert_lavadora_db(arrLava.get(0),arrLava.get(1),arrLava.get(2),arrLava.get(3),arrLava.get(4),arrLava.get(5),arrLava.get(6));
        }else sqlite.insert_lavadora_db("f","f","f","f","f","f","f");

        if(data.get("vaji")) {
            ArrayList<String> arrVaji = new ArrayList<String>();
            arrVaji.add("t");
            if (data.get("vajiA3")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            if (data.get("vajiA2")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            if (data.get("vajiA1")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            if (data.get("vajiA")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            if (data.get("vajiB")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            if (data.get("vajiC")) {
                arrVaji.add("t");
            } else arrVaji.add("f");
            sqlite.insert_vaji_db(arrVaji.get(0),arrVaji.get(1),arrVaji.get(2),arrVaji.get(3),arrVaji.get(4),arrVaji.get(5),arrVaji.get(6));
        }else sqlite.insert_vaji_db("f","f","f","f","f","f","f");

        if(data.get("seca")) {
            ArrayList<String> arrSeca = new ArrayList<String>();
            arrSeca.add("t");
            if (data.get("secaA3")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            if (data.get("secaA2")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            if (data.get("secaA1")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            if (data.get("secaA")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            if (data.get("secaB")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            if (data.get("secaC")) {
                arrSeca.add("t");
            } else arrSeca.add("f");
            sqlite.insert_seca_db(arrSeca.get(0),arrSeca.get(1),arrSeca.get(2),arrSeca.get(3),arrSeca.get(4),arrSeca.get(5),arrSeca.get(6));
        }else sqlite.insert_seca_db("f","f","f","f","f","f","f");

        if (data.get("micro")){
            ArrayList<String> arrMicro = new ArrayList<String>();
            arrMicro.add("t");
            if (data.get("micro")){
                arrMicro.add("t");
            } else  arrMicro.add("f");
            sqlite.insert_horno_db(arrMicro.get(0));
        } else sqlite.insert_horno_db("f");
        sqlite.close();

        if (data.get("horno")){
            ArrayList<String> arrHorno = new ArrayList<String>();
            arrHorno.add("t");
            if (data.get("horno")){
                arrHorno.add("t");
            } else  arrHorno.add("f");
            sqlite.insert_horno_db(arrHorno.get(0));
        } else sqlite.insert_horno_db("f");
        sqlite.close();
    }
    public static HashMap<String,Boolean> getData(){
        return data;
    }

    public HashMap<String,Boolean> setDataFromDataBase(){
        ArrayList<String> arrFrigo = (ArrayList<String>) SQLite.get_frig_db(this);
        if (arrFrigo.get(0).equalsIgnoreCase("t")){
            data.put("frigo", true);
        } else data.put("frigo", false);
        if (arrFrigo.get(1).equalsIgnoreCase("t")){
            data.put("frigoA3", true);
        } else data.put("frigoA3", false);
        if (arrFrigo.get(2).equalsIgnoreCase("t")){
            data.put("frigoA2", true);
        } else data.put("frigoA2", false);
        if (arrFrigo.get(3).equalsIgnoreCase("t")){
            data.put("frigoA1", true);
        } else data.put("frigoA1", false);
        if (arrFrigo.get(4).equalsIgnoreCase("t")){
            data.put("frigoA", true);
        } else data.put("frigoA", false);
        if (arrFrigo.get(5).equalsIgnoreCase("t")){
            data.put("frigoB", true);
        } else data.put("frigoB", false);
        if (arrFrigo.get(6).equalsIgnoreCase("t")){
            data.put("frigoC", true);
        } else data.put("frigoC", false);

//        ArrayList<String> arrConge = (ArrayList<String>) SQLite.get_frig_db(this);
//        if (arrConge.equals("t")){
//            data.put("frigo", true);
//        } else data.put("frigo", false);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(data.get("frigo"));
        return data;

    }

}











