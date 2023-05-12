package com.twit.multiplayer_test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public class MainGame extends AppCompatActivity {

    private DatabaseReference mDatabase;
    boolean trade_nick_clickable = false;
    String lobbyNumber;
    String player_name;
    Lobby lobby;
    TextView gameStateTextView;
    ArrayList<Member> orderedByTurn;
    ArrayList<Member> orderedBySeat;
    SharedPreferences sp;
    HeroAdapter adapter;
    TreasureCardAdapter closedAdapter;
    TreasureCardAdapter openAdapter;
    TradeItemAdapter yoursCloseAdapter;
    TradeItemAdapter yoursOpenAdapter;
    TradeItemAdapter theirsOpenAdapter;
    TreasureDialog treasureDialog;
    TradeDialog tradeDialog;
    MorningTreasureDialog morningTreasureDialog;
    ArrayList<Card> closed;
    ArrayList<Card> open;
    ArrayList<TradeItem> yoursOpen;
    ArrayList<TradeItem> yoursClose;
    ArrayList<TradeItem> theirsOpen;
    ArrayList<Card> theirsClose;
    TextView yourTradeNick;
    TextView theirTradeNick;
    TextView theirsCloseText;
    boolean shown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);


        lobbyNumber = getIntent().getExtras().getString("lobbyNumber");
        sp = getSharedPreferences("auth_data", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance("https://xdlolwtf-default-rtdb.firebaseio.com/").getReference();
        player_name = sp.getString("userLogin", null) + " " + sp.getString("userId", null);
        gameStateTextView = findViewById(R.id.gameState);
        orderedByTurn = new ArrayList<>();
        orderedBySeat = new ArrayList<>();
        closed = new ArrayList<>();
        open = new ArrayList<>();
        yoursClose = new ArrayList<>();
        yoursOpen = new ArrayList<>();
        theirsOpen = new ArrayList<>();
        theirsClose = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.rv_onboardheroes);

        adapter = new HeroAdapter(orderedBySeat, new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Member createWithThatMember = new Member();
                String name = ((TextView) view.findViewById(R.id.hero_nick2)).getText().toString();
                for (Member q : lobby.getMembers().values()) {
                    if (q.getName().equals(name)) {
                        createWithThatMember = q;
                        break;
                    }
                }
                Dialog heroEnlarge = new Dialog(MainGame.this);
                heroEnlarge.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(heroEnlarge.getWindow().getAttributes());
                DisplayMetrics dm = new DisplayMetrics();
                MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                lp.width = ((Double) (dm.widthPixels * 0.9)).intValue();
                lp.height = dm.heightPixels;
                heroEnlarge.getWindow().setAttributes(lp);
                heroEnlarge.setContentView(getLayoutInflater().inflate(R.layout.hero_enlarge, null));
                switch (createWithThatMember.getStats().getRole()) {
                    case "Миледи":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.miledi);
                        break;
                    case "Шкет":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.shket);
                        break;
                    case "Черпак":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.cherpak);
                        break;
                    case "Капитан":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.kapitan);
                        break;
                    case "Боцман":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.botsman);
                        break;
                    case "Сноб":
                        ((ImageView) heroEnlarge.findViewById(R.id.hero_enlarge_pic)).setImageResource(R.drawable.snob);
                        break;
                }
                RecyclerView rv_hero_enlarge = heroEnlarge.findViewById(R.id.rv_hero_enlarge);
                TreasureCardAdapter enlarge_adapter = new TreasureCardAdapter(createWithThatMember.getTreasures().getOpen(), null);
                rv_hero_enlarge.setAdapter(enlarge_adapter);
                rv_hero_enlarge.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                if (createWithThatMember.getTreasures().getClose() != null) {
                    ((TextView) heroEnlarge.findViewById(R.id.closed_treasures)).setText("Закрытых сокровищ: " + createWithThatMember.getTreasures().getClose().size());
                }
                else{
                    ((TextView) heroEnlarge.findViewById(R.id.closed_treasures)).setText("Закрытых сокровищ: 0");
                }
                switch (createWithThatMember.getState().getStatus()) {
                    case "alive":
                        ((TextView) heroEnlarge.findViewById(R.id.status)).setText("Статус: Жив");
                        break;
                    case "dead":
                        ((TextView) heroEnlarge.findViewById(R.id.status)).setText("Статус: Мертв");
                        break;
                    case "unconscious":
                        ((TextView) heroEnlarge.findViewById(R.id.status)).setText("Статус: Без Сознания");
                        break;
                }

            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                String receiver = ((TextView)view.findViewById(R.id.hero_nick)).getText().toString();
                receiver = receiver.substring(0, receiver.lastIndexOf("#"))+" "+receiver.substring(receiver.lastIndexOf("#")+1);
                System.out.println(trade_nick_clickable);
                System.out.println(lobby.getMembers().get(receiver).getState().getStatus().equals("alive"));
                System.out.println(!receiver.equals(player_name));
                System.out.println(trade_nick_clickable && lobby.getMembers().get(receiver).getState().getStatus().equals("alive") && !receiver.equals(player_name));
                if (trade_nick_clickable && lobby.getMembers().get(receiver).getState().getStatus().equals("alive") && !receiver.equals(player_name)){
                    yoursOpenAdapter = new TradeItemAdapter(yoursOpen, new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            Integer posit = Integer.parseInt(((TextView)view.findViewById(R.id.trade_position)).getText().toString());
                            boolean isChosen = yoursOpen.get(posit).isChoosen();
                            ArrayList<Card> senderOpen = lobby.getDialog().getSender_open();
                            if (isChosen){
                                for (Card q:senderOpen){
                                    if (q.getName().equals(yoursOpen.get(posit).getCard().getName())){
                                        senderOpen.remove(q);
                                        break;
                                    }
                                }
                            }
                            else{
                                if (senderOpen == null){
                                    senderOpen = new ArrayList<>();
                                }
                                senderOpen.add(yoursOpen.get(posit).getCard());
                            }
                            yoursOpen.get(posit).setChoosen(!isChosen);
                            lobby.getDialog().setSender_open(senderOpen);
                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                        }
                    });
                    yoursCloseAdapter = new TradeItemAdapter(yoursClose, new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            Integer posit = Integer.parseInt(((TextView)view.findViewById(R.id.trade_position)).getText().toString());
                            boolean isChosen = yoursClose.get(posit).isChoosen();
                            ArrayList<Card> senderClose = lobby.getDialog().getSender_close();
                            if (isChosen){
                                for (Card q:senderClose){
                                    if (q.getName().equals(yoursClose.get(posit).getCard().getName())){
                                        senderClose.remove(q);
                                        break;
                                    }
                                }
                            }
                            else{
                                if (senderClose == null){
                                    senderClose = new ArrayList<>();
                                }
                                senderClose.add(yoursClose.get(posit).getCard());
                            }
                            yoursClose.get(posit).setChoosen(!isChosen);
                            lobby.getDialog().setSender_close(senderClose);
                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                        }
                    });
                    theirsOpenAdapter = new TradeItemAdapter(theirsOpen, new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view) {

                        }
                    });
                    System.out.println(yoursOpen);
                    System.out.println(theirsOpen);
                    HeroDialog hd = new HeroDialog();
                    hd.setSender(lobby.getMembers().get(player_name));
                    hd.setReciever(lobby.getMembers().get(receiver));
                    hd.setSender_accept(0);
                    hd.setReciever_accept(0);
                    lobby.setDialog(hd);
                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);

                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                        yoursClose.add(new TradeItem(q, false));
                    }
                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getOpen()){
                        yoursOpen.add(new TradeItem(q, false));
                    }
                    tradeDialog.create();
                    tradeDialog.show();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(tradeDialog.getWindow().getAttributes());
                    DisplayMetrics dm = new DisplayMetrics();
                    MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    lp.width = ((Double) (dm.widthPixels*0.95)).intValue();
                    lp.height = dm.heightPixels;
                    tradeDialog.getWindow().setAttributes(lp);
                    tradeDialog.setContentView(getLayoutInflater().inflate(R.layout.trade_dialog, null));
                    RecyclerView yoursOpenRv = tradeDialog.findViewById(R.id.yours_open_offer);
                    RecyclerView yoursCloseRv = tradeDialog.findViewById(R.id.yours_close_offer);
                    RecyclerView theirsOpenRv = tradeDialog.findViewById(R.id.theirs_open_offer);

                    yoursOpenRv.setAdapter(yoursOpenAdapter);
                    yoursCloseRv.setAdapter(yoursCloseAdapter);
                    theirsOpenRv.setAdapter(theirsOpenAdapter);
                    yoursOpenRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                    yoursCloseRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                    theirsOpenRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                    TextView theirsName = tradeDialog.findViewById(R.id.trade_offer_theirs_name);
                    theirsName.setText("Предложение "+((TextView)view.findViewById(R.id.hero_nick)).getText().toString());
                    theirsCloseText = tradeDialog.findViewById(R.id.theirs_close_offer);
                    theirsCloseText.setText("Закрытых сокровищ:\n"+theirsClose.size());
                    yourTradeNick = tradeDialog.findViewById(R.id.trade_offer_yours_name);
                    theirTradeNick = tradeDialog.findViewById(R.id.trade_offer_theirs_name);
                    Button refuse = tradeDialog.findViewById(R.id.trade_refuse_button);
                    Button agree = tradeDialog.findViewById(R.id.trade_agree_button);
                    agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            yourTradeNick.setTextColor(getResources().getColor(R.color.agree));
                            lobby.getDialog().setSender_accept(1);
                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                        }
                    });
                    refuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lobby.getDialog().setSender_accept(-1);
                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                            shown = false;
                            yoursClose = new ArrayList<>();
                            yoursOpen = new ArrayList<>();
                            theirsOpen = new ArrayList<>();
                            theirsClose = new ArrayList<>();
                            tradeDialog.dismiss();
                        }
                    });
                }
            }
        });
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.SPACE_AROUND);
        manager.setAlignItems(AlignItems.FLEX_START);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        treasureDialog = new TreasureDialog(MainGame.this);
        treasureDialog.setCanceledOnTouchOutside(true);
        morningTreasureDialog = new MorningTreasureDialog(MainGame.this);
        morningTreasureDialog.setCanceledOnTouchOutside(false);
        tradeDialog = new TradeDialog(MainGame.this);
        tradeDialog.setCanceledOnTouchOutside(false);
        closedAdapter = new TreasureCardAdapter(closed, new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                adb.setTitle("Вы хотите сделать предмет \""+((TextView)view.findViewById(R.id.treasure_text)).getText().toString()+"\" открытым?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String clicked = ((TextView)view.findViewById(R.id.treasure_text)).getText().toString();
                                ArrayList<Card> open = new ArrayList<>(lobby.getMembers().get(player_name).getTreasures().getOpen());
                                ArrayList<Card> close = new ArrayList<>(lobby.getMembers().get(player_name).getTreasures().getClose());
                                for(Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                    if (q.getName().equals(clicked)){
                                        close.remove(q);
                                        open.add(q);
                                        break;
                                    }
                                }
                                mDatabase.child("lobby").child(lobbyNumber).child("members").child(player_name).child("treasures").setValue(new Treasures(open, close));

                            }
                        })
                        .setNegativeButton("Нет", null);
                adb.show();
            }
        });
        openAdapter = new TreasureCardAdapter(open, new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {

            }
        });


        ImageView supply = findViewById(R.id.player_treasures);
        supply.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                treasureDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(treasureDialog.getWindow().getAttributes());
                DisplayMetrics dm = new DisplayMetrics();
                MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                lp.width = ((Double) (dm.widthPixels*0.7)).intValue();
                lp.height = dm.heightPixels;
                treasureDialog.getWindow().setAttributes(lp);
                treasureDialog.setContentView(getLayoutInflater().inflate(R.layout.supply_dialog, null));
                RecyclerView closedRv = treasureDialog.findViewById(R.id.closed_rv);
                RecyclerView openRv = treasureDialog.findViewById(R.id.open_rv);
                closedRv.setAdapter(closedAdapter);
                openRv.setAdapter(openAdapter);
                closedRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                openRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));

            }
        });

        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lobby = snapshot.getValue(Lobby.class);
                if (!(null == lobby)) {
                    orderedBySeat.clear();
                    orderedByTurn.clear();
                    closed.clear();
                    open.clear();
                    for (Member q : lobby.getMembers().values()) {
                        if (!(q.getState().getStatus().equals("dead") || q.getState().getStatus().equals("unconscious"))) {
                            orderedByTurn.add(q);
                        }
                        if (!(q.getState().getStatus().equals("dead") && q.getState().getOverboard().equals(1))) {
                            orderedBySeat.add(q);
                        }
                    }
                    for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                        closed.add(q);
                    }
                    for (Card q : lobby.getMembers().get(player_name).getTreasures().getOpen()) {
                        open.add(q);
                    }
                    orderedByTurn.sort(new SortByTurn());
                    orderedBySeat.sort(new SortBySeat());
                    if (lobby.getGameState().equals("created")) {
                        ImageView tooltip = findViewById(R.id.hero_tooltip);
                        switch (lobby.getMembers().get(player_name).getStats().getRole()) {
                            case "Миледи":
                                tooltip.setImageResource(R.drawable.miledi_bonus);
                                break;
                            case "Шкет":
                                tooltip.setImageResource(R.drawable.shket_bonus);
                                break;
                            case "Черпак":
                                tooltip.setImageResource(R.drawable.cherpak_bonus);
                                break;
                            case "Капитан":
                                tooltip.setImageResource(R.drawable.kapitan_bonus);
                                break;
                            case "Боцман":
                                tooltip.setImageResource(R.drawable.botsman_bonus);
                                break;
                            case "Сноб":
                                tooltip.setImageResource(R.drawable.snob_bonus);
                                break;
                        }
                        ImageView friend = findViewById(R.id.hero_firend);
                        switch (lobby.getMembers().get(player_name).getStats().getFriend()) {
                            case "Миледи":
                                friend.setImageResource(R.drawable.drug_miledi);
                                break;
                            case "Шкет":
                                friend.setImageResource(R.drawable.drug_shket);
                                break;
                            case "Черпак":
                                friend.setImageResource(R.drawable.drug_cherpak);
                                break;
                            case "Капитан":
                                friend.setImageResource(R.drawable.drug_kapitan);
                                break;
                            case "Боцман":
                                friend.setImageResource(R.drawable.drug_botsman);
                                break;
                            case "Сноб":
                                friend.setImageResource(R.drawable.drug_snob);
                                break;
                        }
                        ImageView enemy = findViewById(R.id.hero_enemy);
                        switch (lobby.getMembers().get(player_name).getStats().getEnemy()) {
                            case "Миледи":
                                enemy.setImageResource(R.drawable.vrag_miledi);
                                break;
                            case "Шкет":
                                enemy.setImageResource(R.drawable.vrag_shket);
                                break;
                            case "Черпак":
                                enemy.setImageResource(R.drawable.vrag_cherpak);
                                break;
                            case "Капитан":
                                enemy.setImageResource(R.drawable.vrag_kapitan);
                                break;
                            case "Боцман":
                                enemy.setImageResource(R.drawable.vrag_botsman);
                                break;
                            case "Сноб":
                                enemy.setImageResource(R.drawable.vrag_snob);
                                break;
                        }
                    }
                    if (lobby.getGameState().equals("created") || lobby.getGameState().equals("morning")) {
                        gameStateTextView.setText("Утро\n" + "Ходит " + orderedByTurn.get(lobby.getTurn() - 1).getName());
                        if(lobby.getMembers().get(player_name).getTurn().equals(lobby.getTurn()) && orderedByTurn.contains(lobby.getMembers().get(player_name))){
                            Integer numberOfChosenCards = orderedByTurn.size()-lobby.getMembers().get(player_name).getTurn()+1;
                            ArrayList<Card> cardsToChoose = new ArrayList<>();
                            for(int i=0;i<numberOfChosenCards;i++){
                                cardsToChoose.add(lobby.getTreasuresDeck().get(i));
                            }
                            TreasureCardAdapter morningAdapter = new TreasureCardAdapter(cardsToChoose, new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view) {
                                    AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                                    adb.setTitle("Вы хотите выбрать предмет \""+((TextView)view.findViewById(R.id.treasure_text)).getText().toString()+"\"?")
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            int card = 0;
                                                            while(!lobby.getTreasuresDeck().get(card).getName().equals(((TextView)view.findViewById(R.id.treasure_text)).getText().toString())){
                                                                card+=1;
                                                            }
                                                            lobby.getMembers().get(player_name).getTreasures().getClose().add(lobby.getTreasuresDeck().get(card));
                                                            lobby.getTreasuresDeck().remove(card);
                                                            if (lobby.getTurn().equals(orderedByTurn.size())){
                                                                lobby.setGameState("trade");
                                                                lobby.setTurn(1);
                                                            }
                                                            else {
                                                                lobby.setTurn(lobby.getTurn() + 1);
                                                            }
                                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                            morningTreasureDialog.dismiss();
                                                        }
                                                    })
                                            .setNegativeButton("Нет", null);
                                    adb.show();

                                }
                            });

                            morningTreasureDialog.show();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(morningTreasureDialog.getWindow().getAttributes());
                            DisplayMetrics dm = new DisplayMetrics();
                            MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                            lp.width = ((Double) (dm.widthPixels*0.7)).intValue();
                            lp.height = dm.heightPixels;
                            morningTreasureDialog.getWindow().setAttributes(lp);
                            morningTreasureDialog.setContentView(getLayoutInflater().inflate(R.layout.morning_treasure_dialog, null));
                            RecyclerView morningRv = morningTreasureDialog.findViewById(R.id.morning_treasure_rv);
                            morningRv.setAdapter(morningAdapter);
                            morningRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                        }

                    }

                    if (lobby.getGameState().equals("trade")){
                        gameStateTextView.setText("Торговля\n" + "Ходит " + orderedByTurn.get(lobby.getTurn() - 1).getName());
                        String name_with_sharp = String.valueOf(player_name);
                        name_with_sharp = name_with_sharp.substring(0, name_with_sharp.lastIndexOf(" "))+"#"+name_with_sharp.substring(name_with_sharp.lastIndexOf(" ")+1);
                        if (!(lobby.getDialog() == null)){
                            if(lobby.getDialog().getReciever().getName().equals(name_with_sharp) && !shown && !lobby.getDialog().getReciever_accept().equals(-1)){
                                shown = !shown;
                                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                        yoursClose.add(new TradeItem(q, false));
                                    }
                                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getOpen()){
                                        yoursOpen.add(new TradeItem(q, false));
                                    }
                                tradeDialog.create();
                                tradeDialog.show();
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(tradeDialog.getWindow().getAttributes());
                                    DisplayMetrics dm = new DisplayMetrics();
                                    MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    lp.width = ((Double) (dm.widthPixels*0.95)).intValue();
                                    lp.height = dm.heightPixels;
                                    tradeDialog.getWindow().setAttributes(lp);
                                    tradeDialog.setContentView(getLayoutInflater().inflate(R.layout.trade_dialog, null));
                                    RecyclerView yoursOpenRv = tradeDialog.findViewById(R.id.yours_open_offer);
                                    RecyclerView yoursCloseRv = tradeDialog.findViewById(R.id.yours_close_offer);
                                    RecyclerView theirsOpenRv = tradeDialog.findViewById(R.id.theirs_open_offer);
                                    yoursOpenAdapter = new TradeItemAdapter(yoursOpen, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            Integer posit = Integer.parseInt(((TextView)view.findViewById(R.id.trade_position)).getText().toString());
                                            boolean isChosen = yoursOpen.get(posit).isChoosen();
                                            ArrayList<Card> receiverOpen = lobby.getDialog().getReceiver_open();
                                            if (isChosen){
                                                for (Card q:receiverOpen){
                                                    if (q.getName().equals(yoursOpen.get(posit).getCard().getName())){
                                                        receiverOpen.remove(q);
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                if (receiverOpen == null){
                                                    receiverOpen = new ArrayList<>();
                                                }
                                                receiverOpen.add(yoursOpen.get(posit).getCard());
                                            }
                                            yoursOpen.get(posit).setChoosen(!isChosen);
                                            lobby.getDialog().setReceiver_open(receiverOpen);
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    });
                                    yoursCloseAdapter = new TradeItemAdapter(yoursClose, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            Integer posit = Integer.parseInt(((TextView)view.findViewById(R.id.trade_position)).getText().toString());
                                            boolean isChosen = yoursClose.get(posit).isChoosen();
                                            ArrayList<Card> receiverClose = lobby.getDialog().getReceiver_close();
                                            if (isChosen){
                                                for (Card q:receiverClose){
                                                    if (q.getName().equals(yoursClose.get(posit).getCard().getName())){
                                                        receiverClose.remove(q);
                                                        break;
                                                    }
                                                }
                                            }
                                            else{
                                                if (receiverClose == null){
                                                    receiverClose = new ArrayList<>();
                                                }
                                                receiverClose.add(yoursClose.get(posit).getCard());
                                            }
                                            yoursClose.get(posit).setChoosen(!isChosen);
                                            lobby.getDialog().setReceiver_close(receiverClose);
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    });
                                    theirsOpenAdapter = new TradeItemAdapter(theirsOpen, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {

                                        }
                                    });
                                    yoursOpenRv.setAdapter(yoursOpenAdapter);
                                    yoursCloseRv.setAdapter(yoursCloseAdapter);
                                    theirsOpenRv.setAdapter(theirsOpenAdapter);
                                    yoursOpenRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                                    yoursCloseRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                                    theirsOpenRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                                    TextView theirsName = tradeDialog.findViewById(R.id.trade_offer_theirs_name);
                                    theirsName.setText("Предложение "+lobby.getDialog().getSender().getName());
                                    theirsCloseText = tradeDialog.findViewById(R.id.theirs_close_offer);
                                    theirsCloseText.setText("Закрытых сокровищ:\n"+theirsClose.size());
                                    yourTradeNick = tradeDialog.findViewById(R.id.trade_offer_yours_name);
                                    theirTradeNick = tradeDialog.findViewById(R.id.trade_offer_theirs_name);
                                    Button refuse = tradeDialog.findViewById(R.id.trade_refuse_button);
                                    Button agree = tradeDialog.findViewById(R.id.trade_agree_button);
                                    agree.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            yourTradeNick.setTextColor(getResources().getColor(R.color.agree));
                                            lobby.getDialog().setReciever_accept(1);
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    });
                                    refuse.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            lobby.getDialog().setReciever_accept(-1);
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                            tradeDialog.dismiss();
                                            shown = false;
                                            yoursClose = new ArrayList<>();
                                            yoursOpen = new ArrayList<>();
                                            theirsOpen = new ArrayList<>();
                                            theirsClose = new ArrayList<>();
                                        }
                                    });
                            }

                            if (lobby.getDialog().getReciever().getName().equals(name_with_sharp)){

                                if (lobby.getDialog().getSender_accept().equals(1)){
                                    theirTradeNick.setTextColor(getResources().getColor(R.color.agree));
                                }
                                theirsClose = lobby.getDialog().getSender_close();
                                if (theirsClose == null){
                                    theirsClose = new ArrayList<>();
                                }
                                theirsCloseText.setText("Закрытых сокровищ:\n" + theirsClose.size());
                                if (lobby.getDialog().getSender_open() != null) {
                                    if (lobby.getDialog().getSender_open().size() != theirsOpen.size() && !lobby.getDialog().getSender_accept().equals(2)) {
                                        theirsOpen.clear();
                                        for (Card i : lobby.getDialog().getSender_open()) {
                                            theirsOpen.add(new TradeItem(i, false));
                                        }
                                    }
                                }
                                if (lobby.getDialog().getSender_accept().equals(2)){
                                    tradeDialog.dismiss();
                                    shown = false;
                                    yoursClose = new ArrayList<>();
                                    yoursOpen = new ArrayList<>();
                                    theirsOpen = new ArrayList<>();
                                    theirsClose = new ArrayList<>();
                                    HeroDialog hd = null;
                                    lobby.setDialog(hd);
                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                }
                                if (lobby.getDialog()!= null) {
                                    if (lobby.getDialog().getSender_accept().equals(-1)) {
                                        tradeDialog.dismiss();
                                        shown = false;
                                        yoursClose = new ArrayList<>();
                                        yoursOpen = new ArrayList<>();
                                        theirsOpen = new ArrayList<>();
                                        theirsClose = new ArrayList<>();
                                        HeroDialog hd = null;
                                        lobby.setDialog(hd);
                                        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);

                                    }
                                }
                            }
                        }
                        if (!(lobby.getDialog() == null)){


                            if (lobby.getDialog().getSender().getName().equals(name_with_sharp)){
                                    theirsClose = lobby.getDialog().getReceiver_close();
                                    if (theirsClose == null){
                                        theirsClose = new ArrayList<>();
                                    }
                                    theirsCloseText.setText("Закрытых сокровищ:\n" + theirsClose.size());
                                if (lobby.getDialog().getReceiver_open() != null)
                                {
                                    if (lobby.getDialog().getReceiver_open().size() != theirsOpen.size()  && !lobby.getDialog().getSender_accept().equals(2)) {
                                        theirsOpen.clear();
                                        for (Card i : lobby.getDialog().getReceiver_open()) {
                                            theirsOpen.add(new TradeItem(i, false));
                                        }
                                    }
                                }
                                if (lobby.getDialog().getReciever_accept().equals(1)){
                                    theirTradeNick.setTextColor(getResources().getColor(R.color.agree));
                                }
                                    if (lobby.getDialog().getSender_accept().equals(1) && lobby.getDialog().getReciever_accept().equals(1)){
                                        lobby.getDialog().setSender_accept(2);
                                        ArrayList<Card> senderOpen = lobby.getDialog().getSender_open();
                                        ArrayList<Card> senderClose = lobby.getDialog().getSender_close();
                                        ArrayList<Card> receiverOpen = lobby.getDialog().getReceiver_open();
                                        ArrayList<Card> receiverClose = lobby.getDialog().getReceiver_close();
                                        String receiverName = changeHashToSpace(lobby.getDialog().getReciever().getName());
                                        String senderName = changeHashToSpace(lobby.getDialog().getSender().getName());
                                        if (senderOpen!=null) {
                                            for (Card q : senderOpen) {
                                                if (lobby.getMembers().get(receiverName).getTreasures().getOpen()!= null) {
                                                    lobby.getMembers().get(receiverName).getTreasures().getOpen().add(q);
                                                }
                                                else{
                                                    ArrayList<Card> temp = new ArrayList<>();
                                                    temp.add(q);
                                                    lobby.getMembers().get(receiverName).getTreasures().setOpen(temp);
                                                }
                                                for (Card m: lobby.getMembers().get(senderName).getTreasures().getOpen()){
                                                    if (m.getName().equals(q.getName())){
                                                        lobby.getMembers().get(senderName).getTreasures().getOpen().remove(m);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (senderClose!=null) {
                                        for (Card q: senderClose){
                                            if (lobby.getMembers().get(receiverName).getTreasures().getClose()!= null) {
                                                lobby.getMembers().get(receiverName).getTreasures().getClose().add(q);
                                            }
                                            else{
                                                ArrayList<Card> temp = new ArrayList<>();
                                                temp.add(q);
                                                lobby.getMembers().get(receiverName).getTreasures().setClose(temp);
                                            }
                                            for (Card m: lobby.getMembers().get(senderName).getTreasures().getClose()){
                                                if (m.getName().equals(q.getName())){
                                                    lobby.getMembers().get(senderName).getTreasures().getClose().remove(m);
                                                    break;
                                                }
                                            }
                                        }
                                        }
                                            if (receiverOpen!=null) {
                                        for (Card q: receiverOpen){
                                            if (lobby.getMembers().get(senderName).getTreasures().getOpen()!= null) {
                                                lobby.getMembers().get(senderName).getTreasures().getOpen().add(q);
                                            }
                                            else{
                                                ArrayList<Card> temp = new ArrayList<>();
                                                temp.add(q);
                                                lobby.getMembers().get(senderName).getTreasures().setOpen(temp);
                                            }
                                            for (Card m: lobby.getMembers().get(receiverName).getTreasures().getOpen()){
                                                if (m.getName().equals(q.getName())){
                                                    lobby.getMembers().get(receiverName).getTreasures().getOpen().remove(m);
                                                    break;
                                                }
                                            }
                                        }
                                            }
                                                if (receiverClose!=null) {
                                        for (Card q: receiverClose){
                                            if (lobby.getMembers().get(senderName).getTreasures().getClose()!= null) {
                                                lobby.getMembers().get(senderName).getTreasures().getClose().add(q);
                                            }
                                            else{
                                                ArrayList<Card> temp = new ArrayList<>();
                                                temp.add(q);
                                                lobby.getMembers().get(senderName).getTreasures().setClose(temp);
                                            }
                                            for (Card m: lobby.getMembers().get(receiverName).getTreasures().getClose()){
                                                if (m.getName().equals(q.getName())){
                                                    lobby.getMembers().get(receiverName).getTreasures().getClose().remove(m);
                                                    break;
                                                }
                                            }
                                        }
                                                }
                                        tradeDialog.dismiss();
                                        shown = false;
                                        yoursClose = new ArrayList<>();
                                        yoursOpen = new ArrayList<>();
                                        theirsOpen = new ArrayList<>();
                                        theirsClose = new ArrayList<>();

                                        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    }
                                    if (lobby.getDialog()!= null){
                                        if (lobby.getDialog().getReciever_accept().equals(-1)){
                                            tradeDialog.dismiss();
                                            HeroDialog hd = null;
                                            shown = false;
                                            yoursClose = new ArrayList<>();
                                            yoursOpen = new ArrayList<>();
                                            theirsOpen = new ArrayList<>();
                                            theirsClose = new ArrayList<>();
                                            lobby.setDialog(hd);
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    }
                            }
                        }

                        if (orderedByTurn.get(lobby.getTurn()-1).getName().equals(name_with_sharp)){
                            trade_nick_clickable = true;
                            Button endTurn = findViewById(R.id.end_turn_button);
                            endTurn.setEnabled(true);
                            endTurn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (lobby.getTurn().equals(orderedByTurn.size())){
                                        lobby.setGameState("noon");
                                        lobby.setTurn(1);
                                    }
                                    else {
                                        lobby.setTurn(lobby.getTurn() + 1);
                                    }
                                    trade_nick_clickable = false;
                                    endTurn.setOnClickListener(null);
                                    endTurn.setEnabled(false);
                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                }
                            });
                        }
                    }
                    if (lobby.getGameState().equals("noon")){
                        shown = false;
                        yoursClose = new ArrayList<>();
                        yoursOpen = new ArrayList<>();
                        theirsOpen = new ArrayList<>();
                        theirsClose = new ArrayList<>();
                    }

                    if (!(yoursCloseAdapter==null)) {
                        yoursCloseAdapter.notifyDataSetChanged();
                    }
                    if (!(yoursOpenAdapter==null)) {
                        yoursOpenAdapter.notifyDataSetChanged();
                    }
                    if (!(theirsOpenAdapter==null)) {
                        theirsOpenAdapter.notifyDataSetChanged();
                    }




                    adapter.notifyDataSetChanged();
                    closedAdapter.notifyDataSetChanged();
                    openAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String changeHashToSpace(String s){
        return s.substring(0, s.lastIndexOf("#"))+" "+s.substring(s.lastIndexOf("#")+1);
    }
    public static String changeSpaceToHash(String s){
        return s.substring(0, s.lastIndexOf(" "))+"#"+s.substring(s.lastIndexOf(" ")+1);
    }

}

class SortByTurn implements Comparator<Member>{
    @Override
    public int compare(Member member, Member t1) {
        if (member.getTurn() > t1.getTurn()) {
            return 1;
        } else if (member.getTurn() < t1.getTurn()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class SortBySeat implements Comparator<Member>{
    @Override
    public int compare(Member member, Member t1) {
        if (member.getState().getSeat() > t1.getState().getSeat()) {
            return 1;
        } else if (member.getState().getSeat() < t1.getState().getSeat()) {
            return -1;
        } else {
            return 0;
        }
    }
}