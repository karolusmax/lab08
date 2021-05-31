package pollub.ism.lab08;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import pollub.ism.lab08.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private String wybraneWarzywoNazwa=null;
    private Integer wybraneWarzywoIlosc=null;

    public enum OperacjaMagazynowa{ SKLADUJ, WYDAJ}

    public BazaMagazynowa bazaDanych;

    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bazaDanych= Room.databaseBuilder(getApplicationContext(),BazaMagazynowa.class,BazaMagazynowa.NAZWA_BAZY)
                .allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size()==0){
            String[] asortyment=getResources().getStringArray(R.array.Asortyment);
            for(String nazwa: asortyment){
                PozycjaMagazynowa pozycjaMagazynowa=new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME=nazwa;
                pozycjaMagazynowa.QUANTITY=0;
                pozycjaMagazynowa.czas="00:00:00";
                pozycjaMagazynowa.data="01-01-2021";
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=ArrayAdapter.createFromResource(this,R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

        binding.przyciskSkladuj.setOnClickListener(v -> zmienStan(OperacjaMagazynowa.SKLADUJ));

        binding.przyciskWydaj.setOnClickListener(v -> zmienStan(OperacjaMagazynowa.WYDAJ));

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wybraneWarzywoNazwa=adapter.getItem(position).toString();
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    private void aktualizuj(){
        Integer staraIlosc = wybraneWarzywoIlosc;
        if (null == staraIlosc) {
            staraIlosc=0;
        }
        wybraneWarzywoIlosc=bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.tekstStanuMagazynu.setText("Stan magazynu dla "+wybraneWarzywoNazwa+ " wynosi: "+wybraneWarzywoIlosc);


    }

    private void zmienStan(OperacjaMagazynowa operacjaMagazynowa){
        Integer zmianaIlosci,nowaIlosc=null;


        try{
            zmianaIlosci=Integer.parseInt(binding.edycjaIlosc.getText().toString());

        }catch (NumberFormatException ex){
            return;
        }finally {
            binding.edycjaIlosc.setText("");
        }

        switch(operacjaMagazynowa){
            case SKLADUJ: nowaIlosc=wybraneWarzywoIlosc+zmianaIlosci;break;
            case WYDAJ: nowaIlosc=wybraneWarzywoIlosc-zmianaIlosci;break;
        }

        String czas= Calendar.getInstance().getTime().toString();
        String dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String linia=new StringBuilder().append("Data :"+dateFormat+" czas :"+czas+"stara ilość =>" +wybraneWarzywoIlosc+" nowa ilosć: "+nowaIlosc).toString();
        binding.daneWarzywniaka.append(linia+"\n");
        bazaDanych.pozycjaMagazynowaDAO().updateTimeDateAndQuantity(wybraneWarzywoNazwa,wybraneWarzywoIlosc,nowaIlosc,dateFormat,czas);
        aktualizuj();
    }
}