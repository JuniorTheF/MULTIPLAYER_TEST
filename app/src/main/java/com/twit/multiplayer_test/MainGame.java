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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MainGame extends AppCompatActivity {

    private DatabaseReference mDatabase;
    boolean robbery = false;
    boolean fianlShowed = false;
    boolean trade_nick_clickable = false;
    boolean canJoin = true;
    boolean thirst_clickable = false;
    boolean special_action_heal_nick_clickable = false;
    boolean special_action_umbrella_nick_clickable = false;
    boolean noon_action_rob = false;
    boolean noon_action_change_seat = false;
    boolean brawl_showed = false;
    boolean haveMedkit = false;
    boolean haveUmbrella = false;
    boolean haveFlare = false;
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
    BrawlMemberAdapter attackerAdapter;
    BrawlMemberAdapter defenderAdapter;
    PullAdapter pullAdapter;
    PullDialog pullDialog;
    TreasureDialog treasureDialog;
    TradeDialog tradeDialog;
    ActionDialog actionDialog;
    BrawlDialog brawlDialog;
    RobDialog robDialog;
    ChooseNavCardDialog chooseNavCardDialog;
    AddWeaponDialog addWeaponDialog;
    SpecialCardDialog specialCardDialog;
    MorningTreasureDialog morningTreasureDialog;
    ArrayList<BrawlMember> attacker;
    ArrayList<BrawlMember> defender;
    ArrayList<Card> closed;
    ArrayList<Card> open;
    ArrayList<PullItem> pullItems;
    ArrayList<TradeItem> yoursOpen;
    ArrayList<TradeItem> yoursClose;
    ArrayList<TradeItem> theirsOpen;
    ArrayList<Card> theirsClose;
    TextView yourTradeNick;
    TextView theirTradeNick;
    TextView theirsCloseText;
    ValueEventListener mainListener;
    ArrayList<PullItem> listOfNavCards;
    boolean shown = false;
    boolean zhiletCalculated = false;
    boolean give_zhilet_clickable = false;
    String receiver;
    Member chooser = null;
    boolean actionShown = false;
    Random random_method = new Random();
    Member robbedMember;

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
        pullItems = new ArrayList<>();
        attacker = new ArrayList<>();
        defender = new ArrayList<>();
        listOfNavCards = new ArrayList<>();
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
                TreasureCardAdapter enlarge_adapter = new TreasureCardAdapter(createWithThatMember.getTreasures().getOpen(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view) {

                    }
                });
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
                receiver = ((TextView)view.findViewById(R.id.hero_nick)).getText().toString().split(System.lineSeparator())[0];
                receiver = receiver.substring(0, receiver.lastIndexOf("#"))+" "+receiver.substring(receiver.lastIndexOf("#")+1);
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
                if (special_action_heal_nick_clickable){

                    if (!lobby.getMembers().get(receiver).getState().getStatus().equals("dead")){
                        if (lobby.getMembers().get(receiver).getState().getStatus().equals("unconscious")){
                            lobby.getMembers().get(receiver).getState().setStatus("alive");
                        }
                        if (lobby.getMembers().get(receiver).getState().getInjuries()>0) {
                            lobby.getMembers().get(receiver).getState().setInjuries(lobby.getMembers().get(receiver).getState().getInjuries() - 1);
                        }
                        special_action_heal_nick_clickable = false;
                            for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                if (q.getName().equals("Аптечка")){
                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(q);
                                    break;
                                };
                        }
                        nextMove("noon");
                            return;

                    }
                }
                if (special_action_umbrella_nick_clickable){
                    if(lobby.getMembers().get(receiver).getTreasures().getOpen()== null){
                        lobby.getMembers().get(receiver).getTreasures().setOpen(new ArrayList<>());
                    }
                    Card umb = new Card();
                        for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                            if (q.getName().equals("Зонтик")){
                                umb = q;
                                lobby.getMembers().get(player_name).getTreasures().getClose().remove(q);
                                break;
                            };
                    }
                    lobby.getMembers().get(receiver).getTreasures().getOpen().add(umb);
                        nextMove("noon");
                        return;
                }
                if ((noon_action_rob || noon_action_change_seat) && !receiver.equals(player_name)) {
                    if (noon_action_rob && lobby.getMembers().get(player_name).getStats().getRole().equals("Шкет")) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                        adb.setTitle("Использовать способность?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Member shketSpellMember = lobby.getMembers().get(receiver);
                                if (shketSpellMember.getTreasures().getClose() != null){
                                    if (shketSpellMember.getTreasures().getClose().size()>0) {
                                        Card q = shketSpellMember.getTreasures().getClose().get(random_method.nextInt(shketSpellMember.getTreasures().getClose().size()));
                                        shketSpellMember.getTreasures().getClose().remove(q);
                                        lobby.getMembers().get(player_name).getTreasures().getClose().add(q);
                                    }
                                }
                                nextMove("noon");
                            }
                        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Brawl noon_action_brawl = new Brawl();
                                attacker.clear();
                                defender.clear();
                                if (noon_action_change_seat) {
                                    noon_action_brawl.setGoal("change");
                                }
                                if (noon_action_rob) {
                                    noon_action_brawl.setGoal("rob");
                                }
                                noon_action_brawl.setState("waitForReaction");
                                attacker.add(new BrawlMember(lobby.getMembers().get(player_name), false));
                                defender.add(new BrawlMember(lobby.getMembers().get(receiver), false));
                                noon_action_brawl.setAttacker(attacker);
                                noon_action_brawl.setDefender(defender);
                                if (!defender.get(0).getMember().getState().getStatus().equals("alive")) {
                                    noon_action_brawl.setState("acquiesce");
                                }
                                noon_action_rob = false;
                                noon_action_change_seat = false;
                                mDatabase.child("lobby").child(lobbyNumber).child("brawl").setValue(noon_action_brawl);
                            }
                        });
                        Dialog t = adb.show();
                        t.setCanceledOnTouchOutside(false);
                    }
                    else{
                        Brawl noon_action_brawl = new Brawl();
                        attacker.clear();
                        defender.clear();
                        if (noon_action_change_seat) {
                            noon_action_brawl.setGoal("change");
                        }
                        if (noon_action_rob) {
                            noon_action_brawl.setGoal("rob");
                        }
                        noon_action_brawl.setState("waitForReaction");
                        attacker.add(new BrawlMember(lobby.getMembers().get(player_name), false));
                        defender.add(new BrawlMember(lobby.getMembers().get(receiver), false));
                        noon_action_brawl.setAttacker(attacker);
                        noon_action_brawl.setDefender(defender);
                        if (!defender.get(0).getMember().getState().getStatus().equals("alive")) {
                            noon_action_brawl.setState("acquiesce");
                        }
                        noon_action_rob = false;
                        noon_action_change_seat = false;
                        mDatabase.child("lobby").child(lobbyNumber).child("brawl").setValue(noon_action_brawl);
                    }
                }
                if (give_zhilet_clickable){
                    boolean openZhilet = false;
                    boolean closeZhilet = false;
                    Card z = new Card();
                    if (lobby.getMembers().get(player_name).getTreasures().getOpen()!=null) {
                        for (Card q : lobby.getMembers().get(player_name).getTreasures().getOpen()) {
                            if (q.getName().equals("Спасательный жилет")) {
                                openZhilet = true;
                                z = q;
                                break;
                            }
                        }
                    }
                    if (openZhilet){
                        lobby.getMembers().get(receiver).getTreasures().getOpen().add(z);
                        lobby.getMembers().get(player_name).getTreasures().getOpen().remove(z);
                    }
                    if (lobby.getMembers().get(player_name).getTreasures().getClose()!=null) {
                        for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                            if (q.getName().equals("Спасательный жилет")) {
                                closeZhilet = true;
                                z = q;
                                break;
                            }
                        }
                    }
                    if (closeZhilet){
                        lobby.getMembers().get(receiver).getTreasures().getOpen().add(z);
                        lobby.getMembers().get(player_name).getTreasures().getClose().remove(z);
                    }
                    give_zhilet_clickable = false;
                    Button endTurn = findViewById(R.id.end_turn_button);
                    endTurn.setEnabled(false);
                    endTurn.setOnClickListener(null);
                    nextMove("evening_zhilet");
                    return;

                }
                if (thirst_clickable){
                    if (lobby.getMembers().get(receiver).getState().getThirst()>0) {
                        if (lobby.getMembers().get(player_name).getTreasures().getOpen()!=null) {
                            for (Card q : lobby.getMembers().get(player_name).getTreasures().getOpen()) {
                                if (q.getName().equals("Вода")) {
                                    lobby.getMembers().get(receiver).getState().setThirst(lobby.getMembers().get(receiver).getState().getThirst() - 1);
                                    lobby.getMembers().get(player_name).getTreasures().getOpen().remove(q);
                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    return;
                                }
                            }
                        }
                        if (lobby.getMembers().get(player_name).getTreasures().getClose()!=null) {
                            for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                                if (q.getName().equals("Вода")) {
                                    lobby.getMembers().get(receiver).getState().setThirst(lobby.getMembers().get(receiver).getState().getThirst() - 1);
                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(q);
                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    return;
                                }
                            }
                        }
                    Toast.makeText(MainGame.this, "У вас нет воды", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.SPACE_AROUND);
        manager.setAlignItems(AlignItems.FLEX_START);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        robDialog = new RobDialog(MainGame.this);
        robDialog.setCanceledOnTouchOutside(false);
        brawlDialog = new BrawlDialog(MainGame.this);
        brawlDialog.setCanceledOnTouchOutside(false);
        treasureDialog = new TreasureDialog(MainGame.this);
        treasureDialog.setCanceledOnTouchOutside(true);
        morningTreasureDialog = new MorningTreasureDialog(MainGame.this);
        morningTreasureDialog.setCanceledOnTouchOutside(false);
        tradeDialog = new TradeDialog(MainGame.this);
        tradeDialog.setCanceledOnTouchOutside(false);
        actionDialog = new ActionDialog(MainGame.this);
        actionDialog.setCanceledOnTouchOutside(false);
        pullDialog = new PullDialog(MainGame.this);
        pullDialog.setCanceledOnTouchOutside(false);
        addWeaponDialog = new AddWeaponDialog(MainGame.this);
        addWeaponDialog.setCanceledOnTouchOutside(true);
        specialCardDialog = new SpecialCardDialog(MainGame.this);
        specialCardDialog.setCanceledOnTouchOutside(false);
        chooseNavCardDialog = new ChooseNavCardDialog(MainGame.this);
        chooseNavCardDialog.setCanceledOnTouchOutside(false);
        attackerAdapter = new BrawlMemberAdapter(attacker);
        defenderAdapter = new BrawlMemberAdapter(defender);
        closedAdapter = new TreasureCardAdapter(closed, new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                if (lobby.getBrawl() == null) {
                    String type = ((TextView) view.findViewById(R.id.treasure_text)).getText().toString();
                    if (!(type.equals("Аптечка") || type.equals("Зонтик") || type.equals("Сигнальный пистолет"))) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                        adb.setTitle("Вы хотите сделать предмет \"" + ((TextView) view.findViewById(R.id.treasure_text)).getText().toString() + "\" открытым?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String clicked = ((TextView) view.findViewById(R.id.treasure_text)).getText().toString();
                                        ArrayList<Card> open = new ArrayList<>(lobby.getMembers().get(player_name).getTreasures().getOpen());
                                        ArrayList<Card> close = new ArrayList<>(lobby.getMembers().get(player_name).getTreasures().getClose());
                                        for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                                            if (q.getName().equals(clicked)) {
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
                    } else {
                        Toast.makeText(MainGame.this, "Можно использовать только днем", Toast.LENGTH_SHORT).show();
                    }
                }
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

        mainListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lobby = snapshot.getValue(Lobby.class);
                if (!(null == lobby)) {
                    Integer gulls = lobby.getGull();
                    gulls = Math.min(gulls, 4);
                    ((ImageView)MainGame.this.findViewById(R.id.gulls_main)).setImageResource(getResId("gulls"+gulls, R.drawable.class));
                    if (gulls.equals(4) && !fianlShowed){
                        finishGame();
                    }
                    orderedBySeat.clear();
                    orderedByTurn.clear();
                    closed.clear();
                    open.clear();
                    for (Member q : lobby.getMembers().values()) {
                        orderedByTurn.add(q);
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
                        give_zhilet_clickable = false;
                        zhiletCalculated = false;
                        chooser = null;
                        listOfNavCards = new ArrayList<>();
                        gameStateTextView.setText("Утро\n" + "Ходит " + orderedByTurn.get(lobby.getTurn() - 1).getName());
                        if(lobby.getMembers().get(player_name).getTurn().equals(lobby.getTurn())){
                            if (!(lobby.getMembers().get(player_name).getState().getStatus().equals("dead") || lobby.getMembers().get(player_name).getState().getStatus().equals("unconscious")) && lobby.getTreasuresDeck()!=null) {
                                Integer numberOfChosenCards = orderedByTurn.size();
                                for (Member q: lobby.getMembers().values()){
                                    if (!q.getState().getStatus().equals("alive")){
                                        numberOfChosenCards-=1;
                                    }
                                }
                                for (int n = 1; n<lobby.getMembers().get(player_name).getTurn();n++){
                                    if (orderedByTurn.get(n-1).getState().getStatus().equals("alive")){
                                        numberOfChosenCards-=1;
                                    }
                                }
                                numberOfChosenCards = Math.min(numberOfChosenCards, lobby.getTreasuresDeck().size());
                                ArrayList<Card> cardsToChoose = new ArrayList<>();
                                for (int i = 0; i < numberOfChosenCards; i++) {
                                    cardsToChoose.add(lobby.getTreasuresDeck().get(i));
                                }
                                TreasureCardAdapter morningAdapter = new TreasureCardAdapter(cardsToChoose, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view) {
                                        AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                                        adb.setTitle("Вы хотите выбрать предмет \"" + ((TextView) view.findViewById(R.id.treasure_text)).getText().toString() + "\"?")
                                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        int card = 0;
                                                        while (!lobby.getTreasuresDeck().get(card).getName().equals(((TextView) view.findViewById(R.id.treasure_text)).getText().toString())) {
                                                            card += 1;
                                                        }
                                                        lobby.getMembers().get(player_name).getTreasures().getClose().add(lobby.getTreasuresDeck().get(card));
                                                        lobby.getTreasuresDeck().remove(card);
                                                        if (lobby.getTurn().equals(orderedByTurn.size())) {
                                                            lobby.setGameState("trade");
                                                            lobby.setTurn(1);
                                                        } else {
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
                                lp.width = ((Double) (dm.widthPixels * 0.7)).intValue();
                                lp.height = dm.heightPixels;
                                morningTreasureDialog.getWindow().setAttributes(lp);
                                morningTreasureDialog.setContentView(getLayoutInflater().inflate(R.layout.morning_treasure_dialog, null));
                                RecyclerView morningRv = morningTreasureDialog.findViewById(R.id.morning_treasure_rv);
                                morningRv.setAdapter(morningAdapter);
                                morningRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                            }
                            else{
                                nextMove("morning");
                                return;
                            }
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
                                        String receiverName = changeSharpToSpace(lobby.getDialog().getReciever().getName());
                                        String senderName = changeSharpToSpace(lobby.getDialog().getSender().getName());
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

                        if (orderedByTurn.get(lobby.getTurn()-1).getName().equals(name_with_sharp) && orderedByTurn.get(lobby.getTurn()-1).getState().getStatus().equals("alive")){
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
                        }else{
                            if (orderedByTurn.get(lobby.getTurn()-1).getName().equals(name_with_sharp)) {
                                nextMove("trade");
                                return;
                            }
                        }
                    }
                    if (lobby.getGameState().equals("noon")){
                        gameStateTextView.setText("День\n" + "Ходит " + orderedByTurn.get(lobby.getTurn() - 1).getName());
                        shown = false;
                        trade_nick_clickable = false;
                        yoursClose = new ArrayList<>();
                        yoursOpen = new ArrayList<>();
                        theirsOpen = new ArrayList<>();
                        theirsClose = new ArrayList<>();

                        if (lobby.getChosenNavCards() == null){
                            (findViewById(R.id.navcard_count_text)).setVisibility(View.GONE);
                        }
                        else{
                            (findViewById(R.id.navcard_count_text)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.navcard_count_text)).setText(""+lobby.getChosenNavCards().size());
                        }

                        if (!actionShown && orderedByTurn.get(lobby.getTurn()-1).getName().equals(changeSpaceToSharp(player_name)) && orderedByTurn.get(lobby.getTurn()-1).getState().getStatus().equals("alive")){
                            actionShown = !actionShown;

                            actionDialog.show();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(actionDialog.getWindow().getAttributes());
                            DisplayMetrics dm = new DisplayMetrics();
                            MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                            lp.width = ((Double) (dm.widthPixels*0.7)).intValue();
                            lp.height = dm.heightPixels;
                            actionDialog.getWindow().setAttributes(lp);
                            actionDialog.setContentView(getLayoutInflater().inflate(R.layout.noon_action_dialog, null));
                            TextView skip_move = actionDialog.findViewById(R.id.action_skip);
                            skip_move.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actionDialog.dismiss();
                                    nextMove("noon");
                                    return;
                                }
                            });
                            TextView pull = actionDialog.findViewById(R.id.action_gresti);
                            pull.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actionDialog.dismiss();
                                    Integer pullCount = 2;
                                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getOpen()){
                                        if (q.getName().equals("Весло")){
                                            pullCount+=1;
                                        }
                                    }
                                    pullCount = Math.min(4, pullCount);
                                    for (int i=0; i<pullCount; i++){
                                        pullItems.add(new PullItem(lobby.getNavCardsDeck().get(0), false));
                                        lobby.getNavCardsDeck().remove(0);
                                    }
                                    pullAdapter = new PullAdapter(pullItems, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            Integer posit = Integer.valueOf(((TextView) view.findViewById(R.id.pull_position)).getText().toString());
                                            pullItems.get(posit).setChoosen(!pullItems.get(posit).isChoosen());
                                            pullAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    pullDialog.show();
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(pullDialog.getWindow().getAttributes());
                                    DisplayMetrics dm = new DisplayMetrics();
                                    MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    lp.width = ((Double) (dm.widthPixels*0.7)).intValue();
                                    lp.height = dm.heightPixels;
                                    pullDialog.getWindow().setAttributes(lp);
                                    pullDialog.setContentView(getLayoutInflater().inflate(R.layout.pull_dialog, null));
                                    RecyclerView pullRv = pullDialog.findViewById(R.id.rv_pull_dialog);
                                    pullRv.setAdapter(pullAdapter);
                                    pullRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                                    Button submit = pullDialog.findViewById(R.id.pull_confirm);
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (lobby.getChosenNavCards() == null){
                                                lobby.setChosenNavCards(new ArrayList<>());
                                            }
                                            for (PullItem q: pullItems){
                                                if (q.isChoosen()){
                                                    lobby.getChosenNavCards().add(q.getNavCard());
                                                }
                                            }
                                            pullItems = new ArrayList<>();
                                            pullDialog.dismiss();
                                            lobby.getMembers().get(player_name).getState().setPulled(1);
                                            nextMove("noon");
                                            return;
                                        }
                                    });


                                }
                            });
                            TextView actionSpecialCard = actionDialog.findViewById(R.id.action_special_card);
                            haveMedkit = false;
                            haveUmbrella = false;
                            haveFlare = false;
                            for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                if (q.getName().equals("Аптечка")){
                                    haveMedkit = true;
                                }
                                if (q.getName().equals("Зонтик")){
                                    haveUmbrella = true;
                                }
                                if (q.getName().equals("Сигнальный пистолет")){
                                    haveFlare = true;
                                }
                            }
                            if (haveFlare || haveMedkit || haveUmbrella){

                                actionSpecialCard.setTextColor(getResources().getColor(R.color.toolbar));
                                actionSpecialCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        actionDialog.dismiss();
                                        specialCardDialog.show();
                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        lp.copyFrom(specialCardDialog.getWindow().getAttributes());
                                        DisplayMetrics dm = new DisplayMetrics();
                                        MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        lp.width = ((Double) (dm.widthPixels*0.7)).intValue();
                                        lp.height = dm.heightPixels;
                                        specialCardDialog.getWindow().setAttributes(lp);
                                        specialCardDialog.setContentView(getLayoutInflater().inflate(R.layout.special_card_dialog, null));

                                        if (haveMedkit){
                                            specialCardDialog.findViewById(R.id.special_aptechka).setVisibility(View.VISIBLE);
                                            specialCardDialog.findViewById(R.id.special_aptechka).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    specialCardDialog.dismiss();
                                                    special_action_heal_nick_clickable = true;
                                                }
                                            });
                                        }
                                        if (haveFlare){
                                            specialCardDialog.findViewById(R.id.special_pistolet).setVisibility(View.VISIBLE);
                                            specialCardDialog.findViewById(R.id.special_pistolet).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    for (int n=0; n<3; n++){
                                                        NavCard q = lobby.getNavCardsDeck().remove(0);
                                                        lobby.getNavCardsDeck().add(q);
                                                        lobby.setGull(lobby.getGull()+q.getGull());
                                                        if (lobby.getGull()==-1){
                                                            lobby.setGull(0);
                                                        }
                                                    }
                                                    specialCardDialog.dismiss();
                                                        for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                                            if (q.getName().equals("Сигнальный пистолет")){
                                                                lobby.getMembers().get(player_name).getTreasures().getClose().remove(q);
                                                                break;
                                                            };
                                                    }
                                                    nextMove("noon");
                                                        return;
                                                }
                                            });
                                        }
                                        if (haveUmbrella){
                                            specialCardDialog.findViewById(R.id.special_zontik).setVisibility(View.VISIBLE);
                                            specialCardDialog.findViewById(R.id.special_zontik).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    specialCardDialog.dismiss();
                                                    Toast.makeText(MainGame.this, "Выберите кому хотите дать зонтик", Toast.LENGTH_SHORT).show();
                                                    special_action_umbrella_nick_clickable = true;
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                            TextView rob = actionDialog.findViewById(R.id.action_rob);
                            rob.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actionDialog.dismiss();
                                    noon_action_rob = true;
                                }
                            });
                            TextView changeSeat = actionDialog.findViewById(R.id.action_fight);
                            changeSeat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actionDialog.dismiss();
                                    noon_action_change_seat = true;
                                }
                            });
                        }else{
                            if (!orderedByTurn.get(lobby.getTurn()-1).getState().getStatus().equals("alive") && orderedByTurn.get(lobby.getTurn()-1).getName().equals(changeSpaceToSharp(player_name))){
                                nextMove("noon");
                                return;
                            }
                        }
                        if(lobby.getBrawl()!=null){
                            canJoin = true;
                            if (brawl_showed!=true){
                                brawl_showed = true;
                                brawlDialog.show();
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(brawlDialog.getWindow().getAttributes());
                                DisplayMetrics dm = new DisplayMetrics();
                                MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                lp.width = ((Double) (dm.widthPixels*0.9)).intValue();
                                lp.height = dm.heightPixels;
                                brawlDialog.getWindow().setAttributes(lp);
                                brawlDialog.setContentView(getLayoutInflater().inflate(R.layout.brawl_dialog, null));
                                RecyclerView attackRv = brawlDialog.findViewById(R.id.attacker_team_rv);
                                RecyclerView defendRv = brawlDialog.findViewById(R.id.defender_team_rv);
                                attackRv.setAdapter(attackerAdapter);
                                attackRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                                defendRv.setAdapter(defenderAdapter);
                                defendRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.VERTICAL, false));
                                if (lobby.getBrawl().getGoal().equals("rob")){
                                    ((TextView)brawlDialog.findViewById(R.id.brawl_state)).setText("Ограбление");
                                }
                                if (lobby.getBrawl().getGoal().equals("change")){
                                    ((TextView)brawlDialog.findViewById(R.id.brawl_state)).setText("Смена мест");
                                }
                                if (lobby.getBrawl().getDefender().get(0).getMember().getName().equals(changeSpaceToSharp(player_name))){
                                    Button acquiesce = brawlDialog.findViewById(R.id.brawl_acquiesce);
                                    Button refuse = brawlDialog.findViewById(R.id.brawl_refuse);
                                    acquiesce.setVisibility(View.VISIBLE);
                                    acquiesce.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            acquiesce.setVisibility(View.GONE);
                                            refuse.setVisibility(View.GONE);
                                            brawlDialog.dismiss();
                                            lobby.getBrawl().setState("acquiesce");
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    });
                                    refuse.setVisibility(View.VISIBLE);
                                    refuse.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            acquiesce.setVisibility(View.GONE);
                                            refuse.setVisibility(View.GONE);
                                            lobby.getBrawl().setState("brawl");
                                            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                        }
                                    });
                                }
                            }

                            attacker.clear();
                            defender.clear();
                            Integer attackerPower = 0;
                            Integer defenderPower = 0;

                            for (BrawlMember q: lobby.getBrawl().getDefender()){
                                if (q.getMember().getName().equals(changeSpaceToSharp(player_name))){
                                    canJoin = false;
                                }
                                defender.add(q);
                                defenderPower += calculatePower(q);
                            }
                            for (BrawlMember q: lobby.getBrawl().getAttacker()){
                                if (q.getMember().getName().equals(changeSpaceToSharp(player_name))){
                                    canJoin = false;
                                }
                                attacker.add(q);
                                attackerPower += calculatePower(q);
                            }

                            if (!lobby.getMembers().get(player_name).getState().getStatus().equals("alive")){
                                canJoin = false;
                            }

                            ((TextView) brawlDialog.findViewById(R.id.attacker_power)).setText("Общая сила: "+attackerPower);
                            ((TextView) brawlDialog.findViewById(R.id.defender_power)).setText("Общая сила: "+defenderPower);

                            if (lobby.getBrawl().getState().equals("brawl") || lobby.getBrawl().getState().equals("acquiesce")){
                                if (lobby.getBrawl().getState().equals("brawl")) {
                                    ((TextView) brawlDialog.findViewById(R.id.brawl_state)).setText("Драка");
                                }
                                Button joinAttacker = brawlDialog.findViewById(R.id.attacker_team_join);
                                Button joinDefender = brawlDialog.findViewById(R.id.defender_team_join);
                                Button readyToFight = brawlDialog.findViewById(R.id.brawl_ready);
                                Button addWeapon = brawlDialog.findViewById(R.id.brawl_weapon_add);
                                if (lobby.getBrawl().getState().equals("brawl")) {
                                    if (canJoin) {
                                        joinAttacker.setVisibility(View.VISIBLE);
                                        joinDefender.setVisibility(View.VISIBLE);
                                    } else {
                                        joinAttacker.setVisibility(View.GONE);
                                        joinDefender.setVisibility(View.GONE);
                                        readyToFight.setVisibility(View.VISIBLE);
                                        addWeapon.setVisibility(View.VISIBLE);

                                    }
                                }
                                addWeapon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addWeaponDialog.show();
                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        lp.copyFrom(addWeaponDialog.getWindow().getAttributes());
                                        DisplayMetrics dm = new DisplayMetrics();
                                        MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        lp.width = ((Double) (dm.widthPixels*0.8)).intValue();
                                        lp.height = ((Double) (dm.heightPixels*0.9)).intValue();;
                                        addWeaponDialog.getWindow().setAttributes(lp);
                                        addWeaponDialog.setContentView(getLayoutInflater().inflate(R.layout.add_weapon_dialog, null));
                                        for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                            switch (q.getName()){
                                                case "Гарпун":
                                                    addWeaponDialog.findViewById(R.id.garpun).setVisibility(View.VISIBLE);
                                                    addWeaponDialog.findViewById(R.id.garpun).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            addWeaponDialog.findViewById(R.id.garpun).setVisibility(View.GONE);
                                                            for (int j=0; j<lobby.getMembers().get(player_name).getTreasures().getClose().size();j++) {
                                                                if (lobby.getMembers().get(player_name).getTreasures().getClose().get(j).getName().equals("Гарпун")){
                                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().add(lobby.getMembers().get(player_name).getTreasures().getClose().get(j));
                                                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(j);
                                                                    updateBrawlMember();

                                                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;
                                                case "Нож":
                                                    addWeaponDialog.findViewById(R.id.nozh).setVisibility(View.VISIBLE);
                                                    addWeaponDialog.findViewById(R.id.nozh).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            addWeaponDialog.findViewById(R.id.nozh).setVisibility(View.GONE);
                                                            for (int j=0; j<lobby.getMembers().get(player_name).getTreasures().getClose().size();j++) {
                                                                if (lobby.getMembers().get(player_name).getTreasures().getClose().get(j).getName().equals("Нож")){
                                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().add(lobby.getMembers().get(player_name).getTreasures().getClose().get(j));
                                                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(j);
                                                                    updateBrawlMember();
                                                                    view.setVisibility(View.GONE);
                                                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;
                                                case "Cигнальный пистолет":
                                                    addWeaponDialog.findViewById(R.id.special_pistolet).setVisibility(View.VISIBLE);
                                                    addWeaponDialog.findViewById(R.id.special_pistolet).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            addWeaponDialog.findViewById(R.id.special_pistolet).setVisibility(View.GONE);
                                                            for (int j=0; j<lobby.getMembers().get(player_name).getTreasures().getClose().size();j++) {
                                                                if (lobby.getMembers().get(player_name).getTreasures().getClose().get(j).getName().equals("Сигнальный пистолет")){
                                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().add(lobby.getMembers().get(player_name).getTreasures().getClose().get(j));
                                                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(j);
                                                                    updateBrawlMember();
                                                                    view.setVisibility(View.GONE);
                                                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;
                                                case "Дубинка":
                                                    addWeaponDialog.findViewById(R.id.dubinka).setVisibility(View.VISIBLE);
                                                    addWeaponDialog.findViewById(R.id.dubinka).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            addWeaponDialog.findViewById(R.id.dubinka).setVisibility(View.GONE);
                                                            for (int j=0; j<lobby.getMembers().get(player_name).getTreasures().getClose().size();j++) {
                                                                if (lobby.getMembers().get(player_name).getTreasures().getClose().get(j).getName().equals("Дубинка")){
                                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().add(lobby.getMembers().get(player_name).getTreasures().getClose().get(j));
                                                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(j);
                                                                    updateBrawlMember();
                                                                    view.setVisibility(View.GONE);
                                                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;
                                                case "Весло":
                                                    addWeaponDialog.findViewById(R.id.veslo).setVisibility(View.VISIBLE);
                                                    addWeaponDialog.findViewById(R.id.veslo).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            addWeaponDialog.findViewById(R.id.veslo).setVisibility(View.GONE);
                                                            for (int j=0; j<lobby.getMembers().get(player_name).getTreasures().getClose().size();j++) {
                                                                if (lobby.getMembers().get(player_name).getTreasures().getClose().get(j).getName().equals("Весло")){
                                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().add(lobby.getMembers().get(player_name).getTreasures().getClose().get(j));
                                                                    lobby.getMembers().get(player_name).getTreasures().getClose().remove(j);
                                                                    updateBrawlMember();
                                                                    view.setVisibility(View.GONE);
                                                                    mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    });
                                                    break;


                                            }
                                        }


                                    }
                                });
                                readyToFight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        for (BrawlMember q: lobby.getBrawl().getDefender()){
                                            if (q.getMember().getName().equals(changeSpaceToSharp(player_name))){
                                                q.setReady(true);
                                            }
                                        }
                                        for (BrawlMember q: lobby.getBrawl().getAttacker()){
                                            if (q.getMember().getName().equals(changeSpaceToSharp(player_name))){
                                                q.setReady(true);
                                            }
                                        }
                                        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    }
                                });
                                joinAttacker.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        lobby.getBrawl().getAttacker().add(new BrawlMember(lobby.getMembers().get(player_name), false));
                                        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    }
                                });
                                joinDefender.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        lobby.getBrawl().getDefender().add(new BrawlMember(lobby.getMembers().get(player_name), false));
                                        mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
                                    }
                                });

                                boolean allReady = true;
                                for (BrawlMember q: lobby.getBrawl().getDefender()){
                                    allReady = allReady && q.isReady();
                                }
                                for (BrawlMember q: lobby.getBrawl().getAttacker()){
                                    allReady = allReady && q.isReady();
                                }

                            if (lobby.getBrawl().getAttacker().get(0).getMember().getName().equals(changeSpaceToSharp(player_name))){
                                if (allReady || lobby.getBrawl().getState().equals("acquiesce")){
                                    if (attackerPower>defenderPower || lobby.getBrawl().getState().equals("acquiesce")){
                                        if (lobby.getBrawl().getGoal().equals("rob")){
                                            robbedMember = lobby.getMembers().get(changeSharpToSpace(lobby.getBrawl().getDefender().get(0).getMember().getName()));
                                            brawlDialog.dismiss();
                                            robbery = true;
                                            robDialog.show();
                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                            lp.copyFrom(robDialog.getWindow().getAttributes());
                                            DisplayMetrics dm = new DisplayMetrics();
                                            MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                            lp.width = ((Double) (dm.widthPixels*0.9)).intValue();
                                            lp.height = dm.heightPixels;
                                            robDialog.getWindow().setAttributes(lp);
                                            robDialog.setContentView(getLayoutInflater().inflate(R.layout.rob_dialog, null));
                                            TextView robCloseCount = robDialog.findViewById(R.id.rob_close_text);
                                            if (robbedMember.getTreasures().getClose()!=null){
                                                robCloseCount.setText("Закрытых сокровищ: "+robbedMember.getTreasures().getClose().size());
                                            }
                                            else{
                                                robCloseCount.setText("Закрытых сокровищ: 0");
                                            }
                                            Button robCloseButton = robDialog.findViewById(R.id.rob_close_button);
                                            robCloseButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (robbedMember.getTreasures().getClose() != null){
                                                        if (robbedMember.getTreasures().getClose().size()>0) {
                                                            Card q = robbedMember.getTreasures().getClose().get(random_method.nextInt(robbedMember.getTreasures().getClose().size()));
                                                            robbedMember.getTreasures().getClose().remove(q);
                                                            lobby.getMembers().get(player_name).getTreasures().getClose().add(q);
                                                        }
                                                    }
                                                    robDialog.dismiss();
                                                    nextMove("noon");
                                                    return;
                                                }
                                            });
                                            RecyclerView robOpenRv = robDialog.findViewById(R.id.rob_open_rv);
                                            ArrayList<Card> robbedOpen = robbedMember.getTreasures().getOpen();
                                            RobOpenAdapter robOpenAdapter = new RobOpenAdapter(robbedOpen, new OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view) {
                                                    String name = ((TextView)view.findViewById(R.id.treasure_text)).getText().toString();
                                                    for (Card q:robbedMember.getTreasures().getOpen()){
                                                        if (q.getName().equals(name)){
                                                            lobby.getMembers().get(player_name).getTreasures().getOpen().add(q);
                                                            robbedMember.getTreasures().getOpen().remove(q);
                                                            break;
                                                        }
                                                    }
                                                    robDialog.dismiss();
                                                    nextMove("noon");
                                                    return;
                                                }
                                            });
                                            robOpenRv.setAdapter(robOpenAdapter);
                                            robOpenRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));
                                        }
                                        else{
                                            Member attackerRob = lobby.getMembers().get(player_name);
                                            Member defenderRob = lobby.getMembers().get(changeSharpToSpace(lobby.getBrawl().getDefender().get(0).getMember().getName()));
                                            Integer attacker_old_seat = Integer.valueOf(attackerRob.getState().getSeat());
                                            Integer defender_old_seat = Integer.valueOf(defenderRob.getState().getSeat());
                                            attackerRob.getState().setSeat(defender_old_seat);
                                            defenderRob.getState().setSeat(attacker_old_seat);
                                        }
                                        if (allReady) {
                                            for (BrawlMember g : lobby.getBrawl().getDefender()) {
                                                lobby.getMembers().get(changeSharpToSpace(g.getMember().getName())).getState().setInjuries(lobby.getMembers().get(changeSharpToSpace(g.getMember().getName())).getState().getInjuries() + 1);
                                            }
                                        }
                                    }
                                    else{
                                        for (BrawlMember g: lobby.getBrawl().getAttacker()){
                                            lobby.getMembers().get(changeSharpToSpace(g.getMember().getName())).getState().setInjuries(lobby.getMembers().get(changeSharpToSpace(g.getMember().getName())).getState().getInjuries()+1);
                                        }
                                    }
                                    if (allReady) {
                                        for (BrawlMember q : lobby.getBrawl().getAttacker()) {
                                            Member toChange = lobby.getMembers().get(changeSharpToSpace(q.getMember().getName()));
                                            if (toChange.getState().getInjuries().equals(toChange.getStats().getPower())) {
                                                toChange.getState().setStatus("unconscious");
                                            }
                                            toChange.getState().setBrawled(1);
                                            for (Card g : toChange.getTreasures().getOpen()) {
                                                if (g.getName().equals("Cигнальный пистолет")) {
                                                    q.getMember().getTreasures().getOpen().remove(g);
                                                }
                                            }
                                        }
                                        for (BrawlMember q : lobby.getBrawl().getDefender()) {
                                            Member toChange = lobby.getMembers().get(changeSharpToSpace(q.getMember().getName()));
                                            if (toChange.getState().getInjuries().equals(toChange.getStats().getPower())) {
                                                toChange.getState().setStatus("unconscious");
                                            }
                                            toChange.getState().setBrawled(1);
                                            for (Card g : toChange.getTreasures().getOpen()) {
                                                if (g.getName().equals("Cигнальный пистолет")) {
                                                    q.getMember().getTreasures().getOpen().remove(g);
                                                }
                                            }
                                        }
                                    }
                                    lobby.setBrawl(null);
                                    if (!robbery){
                                        nextMove("noon");
                                        return;
                                    }

                                }
                            }
                            if (allReady){
                                brawlDialog.dismiss();
                            }
                            }
                        }else{
                            brawlDialog.dismiss();
                            brawl_showed = false;
                        }


                    }
                    if (lobby.getGameState().equals("evening_choose_card")){
                        if (lobby.getGameState().equals("evening_choose_card")) {
                            brawlDialog.dismiss();
                            robbery = false;
                            canJoin = true;
                            actionShown = false;
                            brawl_showed = false;
                            noon_action_change_seat = false;
                            noon_action_rob = false;
                                for (int n = orderedBySeat.size() - 1; n >= 0; n--) {
                                    if (orderedBySeat.get(n).getState().getStatus().equals("alive")) {
                                        chooser = orderedBySeat.get(n);
                                        break;
                                    }
                                }
                            if (listOfNavCards.size()==0) {
                                if (lobby.getChosenNavCards() != null) {
                                    for (NavCard q : lobby.getChosenNavCards()) {
                                        listOfNavCards.add(new PullItem(q, false));
                                    }
                                } else {
                                    listOfNavCards.add(new PullItem(lobby.getNavCardsDeck().remove(0), false));
                                }
                                if (chooser!=null) {
                                    boolean haveCompass = false;
                                    for (Card q : chooser.getTreasures().getOpen()) {
                                        if (q.getName().equals("Компас")) {
                                            haveCompass = true;
                                            break;
                                        }
                                    }
                                    if (haveCompass) {
                                        listOfNavCards.add(new PullItem(lobby.getNavCardsDeck().remove(0), false));
                                    }
                                }
                            }
                            if (chooser != null) {
                                gameStateTextView.setText("Вечер\n" + "Карту выбирает " + chooser.getName());
                                if (chooser.getName().equals(changeSpaceToSharp(player_name))){
                                    chooseNavCardDialog.show();
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(chooseNavCardDialog.getWindow().getAttributes());
                                    DisplayMetrics dm = new DisplayMetrics();
                                    MainGame.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    lp.width = ((Double) (dm.widthPixels*0.9)).intValue();
                                    lp.height = dm.heightPixels;
                                    chooseNavCardDialog.getWindow().setAttributes(lp);
                                    chooseNavCardDialog.setContentView(getLayoutInflater().inflate(R.layout.choose_nav_card_dialog, null));
                                    RecyclerView chooseNavCardRv = chooseNavCardDialog.findViewById(R.id.choose_nav_card_rv);
                                    PullAdapter chooseNavCardAdapter = new PullAdapter(listOfNavCards, new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            Integer pos = Integer.valueOf(((TextView)view.findViewById(R.id.pull_position)).getText().toString());
                                            NavCard chosen = listOfNavCards.get(pos).getNavCard();
                                            lobby.setEveningNavCard(chosen);
                                            for (PullItem q: listOfNavCards){
                                                lobby.getNavCardsDeck().add(q.getNavCard());
                                            }
                                            listOfNavCards = new ArrayList<>();
                                            lobby.setChosenNavCards(null);
                                            chooseNavCardDialog.dismiss();
                                            nextMove("evening_choose_card");
                                            return;
                                        }
                                    });
                                    chooseNavCardRv.setAdapter(chooseNavCardAdapter);
                                    chooseNavCardRv.setLayoutManager(new LinearLayoutManager(MainGame.this, LinearLayoutManager.HORIZONTAL, false));

                                }else{
                                    chooseNavCardDialog.dismiss();
                                }
                            }
                            else{
                                gameStateTextView.setText("Вечер\n" + "Карта выберется сама");
                                if ((lobby.getHostName()+" "+lobby.getHostId()).equals(player_name)){
                                    NavCard q = listOfNavCards.remove(0).getNavCard();
                                    lobby.setEveningNavCard(q);
                                    lobby.getNavCardsDeck().add(q);
                                    lobby.setChosenNavCards(null);
                                    nextMove("evening_choose_card");
                                    return;
                                }
                            }
                        }
                    }
                    if (lobby.getGameState().equals("evening_gulls")){
                        if ((lobby.getHostName()+" "+lobby.getHostId()).equals(player_name)){
                            lobby.setGull(lobby.getGull()+lobby.getEveningNavCard().getGull());
                            if (lobby.getGull()==-1){
                                lobby.setGull(0);
                            }
                            nextMove("evening_gulls");
                            return;
                        }
                    }
                    if (lobby.getGameState().equals("evening_overboard")){
                        gameStateTextView.setText("Вечер\nРешается кто попадет за борт");
                        if ((lobby.getHostName()+" "+lobby.getHostId()).equals(player_name)){
                            ArrayList<String> overBoarders = new ArrayList<>();
                            if (lobby.getEveningNavCard().getBort() != null) {
                                for (String q : lobby.getEveningNavCard().getBort()) {
                                    overBoarders.add(q);
                                }
                                for (Member q :lobby.getMembers().values()){
                                    if (overBoarders.contains(q.getStats().getRole())){
                                        q.getState().setOverboard(1);
                                    }
                                }
                            }
                            nextMove("evening_overboard");
                            return;
                        }
                    }
                    if (lobby.getGameState().equals("evening_bait")) {
                        gameStateTextView.setText("Вечер\nРешается использование приманки для акул" );
                        if (orderedByTurn.get(lobby.getTurn()-1).getName().equals(changeSpaceToSharp(player_name))) {
                            if (lobby.getMembers().get(player_name).getState().getStatus().equals("alive")) {
                                if (lobby.getEveningNavCard().getBort() != null) {
                                    boolean haveBait = false;
                                    if (lobby.getMembers().get(player_name).getTreasures().getClose()!=null) {
                                        for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                                            if (q.getName().equals("Приманка для акул")) {
                                                haveBait = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (haveBait) {
                                        AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                                        adb.setTitle("Использовать приманку для акул?")
                                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        lobby.setSharkBait(true);
                                                        for (Card q : lobby.getMembers().get(player_name).getTreasures().getClose()) {
                                                            if (q.getName().equals("Приманка для акул")) {
                                                                lobby.getMembers().get(player_name).getTreasures().getClose().remove(q);
                                                                break;
                                                            }
                                                        }
                                                        nextMove("evening_bait");
                                                        return;
                                                    }
                                                })
                                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        nextMove("evening_bait");
                                                        return;
                                                    }
                                                });
                                        AlertDialog dialog = adb.show();
                                        dialog.setCanceledOnTouchOutside(false);
                                    } else {
                                        boolean haveOpenBait = false;
                                        if (lobby.getMembers().get(player_name).getTreasures().getOpen()!=null) {
                                            for (Card q : lobby.getMembers().get(player_name).getTreasures().getOpen()) {
                                                if (q.getName().equals("Приманка для акул")) {
                                                    haveOpenBait = true;
                                                    lobby.getMembers().get(player_name).getTreasures().getOpen().remove(q);
                                                    break;
                                                }
                                            }
                                        }
                                        if (lobby.getMembers().get(player_name).getState().getOverboard().equals(1)) {
                                            if (haveOpenBait) {
                                                lobby.setSharkBait(true);
                                            }
                                        } else {
                                            if (haveOpenBait) {
                                                AlertDialog.Builder adb = new AlertDialog.Builder(MainGame.this);
                                                adb.setTitle("Использовать приманку для акул?")
                                                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                lobby.setSharkBait(true);
                                                                for (Card q : lobby.getMembers().get(player_name).getTreasures().getOpen()) {
                                                                    if (q.getName().equals("Приманка для акул")) {
                                                                        lobby.getMembers().get(player_name).getTreasures().getOpen().remove(q);
                                                                        break;
                                                                    }
                                                                }
                                                                nextMove("evening_bait");
                                                                return;
                                                            }
                                                        })
                                                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                nextMove("evening_bait");
                                                                return;
                                                            }
                                                        });
                                                AlertDialog dialog = adb.show();
                                                dialog.setCanceledOnTouchOutside(false);
                                            }
                                        }
                                        nextMove("evening_bait");
                                        return;
                                    }
                                } else {
                                    nextMove("evening_bait");
                                    return;
                                }
                            }else {
                                nextMove("evening_bait");
                                return;
                            }
                        }
                    }
                    if (lobby.getGameState().equals("evening_shark_damage")){
                        gameStateTextView.setText("Вечер\nПросчитывается урон от акул");
                        if ((lobby.getHostName() + " " + lobby.getHostId()).equals(player_name)){
                            if (lobby.isSharkBait()) {
                                for (Member q :lobby.getMembers().values()){
                                    if (q.getState().getOverboard().equals(1)){
                                        q.getState().setInjuries(q.getState().getInjuries()+1);
                                    }
                                }
                            }
                            nextMove("evening_shark_damage");
                            return;
                        }
                    }
                    if (lobby.getGameState().equals("evening_zhilet")) {
                        gameStateTextView.setText("Вечер\nРешается использование жилетов");
                        if (orderedByTurn.get(lobby.getTurn() - 1).getName().equals(changeSpaceToSharp(player_name))) {
                            if (lobby.getMembers().get(player_name).getState().getStatus().equals("alive")) {
                                if (!zhiletCalculated) {
                                    zhiletCalculated = true;
                                    Member q = lobby.getMembers().get(player_name);
                                    boolean haveOpenZhilet = false;
                                    boolean haveCloseZhilet = false;
                                    if (q.getTreasures().getOpen()!=null){
                                        for (Card g : q.getTreasures().getOpen()) {
                                            if (g.getName().equals("Спасательный жилет")) {
                                                haveOpenZhilet = true;
                                            }
                                        }
                                    }
                                    if (q.getTreasures().getClose()!=null) {
                                        for (Card g : q.getTreasures().getClose()) {
                                            if (g.getName().equals("Спасательный жилет")) {
                                                haveCloseZhilet = true;
                                            }
                                        }
                                    }
                                    if (!(haveCloseZhilet || haveOpenZhilet)) {
                                        nextMove("evening_zhilet");
                                        return;
                                    } else {
                                        Toast.makeText(MainGame.this, "Выберите кому хотите предоставить жилет", Toast.LENGTH_LONG).show();
                                        give_zhilet_clickable = true;
                                        Button endTurn = findViewById(R.id.end_turn_button);
                                        endTurn.setEnabled(true);
                                        endTurn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                give_zhilet_clickable = false;
                                                nextMove("evening_zhilet");
                                                endTurn.setOnClickListener(null);
                                                endTurn.setEnabled(false);
                                                return;
                                            }
                                        });
                                    }
                                }
                            }else{
                                nextMove("evening_zhilet");
                                return;
                            }
                        }
                    }
                    if (lobby.getGameState().equals("evening_damage_overboard")){
                        gameStateTextView.setText("Вечер\nПросчитывается урон от падения за борт");
                        if ((lobby.getHostName() + " " + lobby.getHostId()).equals(player_name)){
                            for (Member q: lobby.getMembers().values()){
                                if (q.getState().getOverboard().equals(1)){
                                    boolean zhilet = false;
                                    if (q.getTreasures().getOpen()!=null) {
                                        for (Card g : q.getTreasures().getOpen()) {
                                            if (g.getName().equals("Спасательный жилет")) {
                                                zhilet = true;
                                                break;
                                            }
                                        }
                                    }
                                        if (zhilet) {
                                            q.getState().setOverboard(0);
                                            continue;
                                        }
                                    if (!q.getStats().getRole().equals("Черпак")){
                                        q.getState().setInjuries(q.getState().getInjuries()+1);
                                    }
                                    q.getTreasures().setOpen(null);
                                }
                            }
                            for (Member q: lobby.getMembers().values()){
                                if (q.getState().getInjuries()==q.getStats().getPower()){
                                    if (q.getState().getOverboard()==1) {
                                        q.getState().setStatus("dead");
                                    }
                                    else{
                                        q.getState().setStatus("unconscious");
                                    }
                                }else if (q.getState().getInjuries()>q.getStats().getPower()){
                                    q.getState().setStatus("dead");
                                }else{
                                    q.getState().setOverboard(0);
                                }
                            }
                            NavCard g = lobby.getEveningNavCard();
                            for (Member q: lobby.getMembers().values()) {
                                boolean haveZontik = false;
                                if (q.getTreasures().getOpen()!=null) {
                                    for (Card m : q.getTreasures().getOpen()) {
                                        if (m.getName().equals("Зонтик")) {
                                            haveZontik = true;
                                            break;
                                        }
                                    }
                                }
                                if (!haveZontik) {
                                    if (g.getFighters() == 1) {
                                        if (q.getState().getBrawled().equals(1)) {
                                            q.getState().setThirst(q.getState().getThirst() + 1);
                                        }
                                    }
                                    if (g.getWorkers() == 1) {
                                        if (q.getState().getPulled().equals(1)) {
                                            q.getState().setThirst(q.getState().getThirst() + 1);
                                        }
                                    }
                                    if (g.getThirst()!=null) {
                                        if (g.getThirst().contains(q.getStats().getRole())) {
                                            q.getState().setThirst(q.getState().getThirst() + 1);
                                        }
                                    }
                                }
                            }
                            nextMove("evening_damage_overboard");
                            return;
                        }
                    }
                    if (lobby.getGameState().equals("evening_thirst")){
                        gameStateTextView.setText("Вечер\n" + "Воду раздает " + orderedByTurn.get(lobby.getTurn() - 1).getName());
                        if (orderedByTurn.get(lobby.getTurn()-1).getName().equals(changeSpaceToSharp(player_name))){
                            if (lobby.getMembers().get(player_name).getState().getStatus().equals("alive")){
                                boolean haveWater = false;
                                if (lobby.getMembers().get(player_name).getTreasures().getOpen()!=null){
                                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getOpen()){
                                        if (q.getName().equals("Вода")){
                                            haveWater = true;
                                        }
                                    }
                                }
                                if (lobby.getMembers().get(player_name).getTreasures().getClose()!=null){
                                    for (Card q: lobby.getMembers().get(player_name).getTreasures().getClose()){
                                        if (q.getName().equals("Вода")){
                                            haveWater = true;
                                        }
                                    }
                                }
                                if (haveWater) {
                                    Toast.makeText(MainGame.this, "Выберите кому дать воду", Toast.LENGTH_SHORT).show();
                                    thirst_clickable = true;
                                    Button endTurn = findViewById(R.id.end_turn_button);
                                    endTurn.setEnabled(true);
                                    endTurn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            thirst_clickable = false;
                                            endTurn.setOnClickListener(null);
                                            endTurn.setEnabled(false);
                                            nextMove("evening_thirst");
                                            return;
                                        }
                                    });
                                }else {
                                    Button endTurn = findViewById(R.id.end_turn_button);
                                    endTurn.setEnabled(false);
                                    endTurn.setOnClickListener(null);
                                    nextMove("evening_thirst");
                                    return;
                                }
                            }else{
                                nextMove("evening_thirst");
                                return;
                            }
                        }
                    }
                    if (lobby.getGameState().equals("evening_final")){
                        if ((lobby.getHostName() + " " + lobby.getHostId()).equals(player_name)) {
                            for (Member q : lobby.getMembers().values()) {
                                q.getState().setInjuries(q.getState().getInjuries() + q.getState().getThirst());
                                q.getState().setThirst(0);
                                if (q.getState().getInjuries() > q.getStats().getPower()) {
                                    q.getState().setStatus("dead");
                                } else if (q.getState().getInjuries() == q.getStats().getPower()) {
                                    q.getState().setStatus("unconscious");
                                }
                                q.getState().setBrawled(0);
                                q.getState().setPulled(0);
                                q.setTurn(q.getState().getSeat());
                            }

                            lobby.setChosenNavCards(null);
                            lobby.setEveningNavCard(null);
                            lobby.setSharkBait(false);
                            nextMove("evening_final");
                            return;
                        }
                        Button endTurn = findViewById(R.id.end_turn_button);
                        endTurn.setEnabled(false);
                        endTurn.setOnClickListener(null);
                    }




                    if (lobby.isSharkBait()){
                        findViewById(R.id.akuli).setVisibility(View.VISIBLE);
                    }
                    else{
                        findViewById(R.id.akuli).setVisibility(View.GONE);
                    }
                    if (lobby.getEveningNavCard() == null) {
                        findViewById(R.id.nav_card_back).setBackgroundResource(R.drawable.back_nav);
                        if (lobby.getChosenNavCards() == null) {
                            (findViewById(R.id.navcard_count_text)).setVisibility(View.GONE);
                        } else {
                            (findViewById(R.id.navcard_count_text)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.navcard_count_text)).setText("" + lobby.getChosenNavCards().size());
                        }
                    }else{
                        (findViewById(R.id.navcard_count_text)).setVisibility(View.GONE);
                        findViewById(R.id.nav_card_back).setBackgroundResource(getResId(lobby.getEveningNavCard().getResource(), R.drawable.class));

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
                    if(!(attackerAdapter==null)){
                        attackerAdapter.notifyDataSetChanged();
                    }
                    if(!(defenderAdapter==null)){
                        defenderAdapter.notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();
                    closedAdapter.notifyDataSetChanged();
                    openAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("lobby").child(lobbyNumber).addValueEventListener(mainListener);
    }

    private void finishGame(){
        mDatabase.child("lobby").child(lobbyNumber).removeEventListener(mainListener);
        Toast.makeText(MainGame.this, "Игра окончена", Toast.LENGTH_LONG).show();
        fianlShowed = true;
        Gson gson = new Gson();
        String lobbyJson = gson.toJson(lobby);
        Intent toFinal = new Intent(MainGame.this, FinalOfGame.class);
        toFinal.putExtra("lobby", lobbyJson);
        finish();
        startActivity(toFinal);
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

    public static String changeSharpToSpace(String s){
        return s.substring(0, s.lastIndexOf("#"))+" "+s.substring(s.lastIndexOf("#")+1);
    }
    public static String changeSpaceToSharp(String s){
        return s.substring(0, s.lastIndexOf(" "))+"#"+s.substring(s.lastIndexOf(" ")+1);
    }

    public void nextMove(String curr_stage){
        if (lobby.getGameState().equals(curr_stage)) {
            if (lobby.getTurn().equals(orderedByTurn.size()) || curr_stage.equals("evening_choose_card") || curr_stage.equals("evening_gulls") || curr_stage.equals("evening_overboard") || curr_stage.equals("evening_shark_damage") || curr_stage.equals("evening_damage_overboard") || curr_stage.equals("evening_final")) {
                if (curr_stage.equals("morning")) {
                    lobby.setGameState("trade");
                }
                if (curr_stage.equals("trade")) {
                    lobby.setGameState("noon");
                }
                if (curr_stage.equals("noon")) {
                    lobby.setGameState("evening_choose_card");
                }
                if (curr_stage.equals("evening_choose_card")) {
                    lobby.setGameState("evening_gulls");
                }
                if (curr_stage.equals("evening_gulls")) {
                    lobby.setGameState("evening_overboard");
                }
                if (curr_stage.equals("evening_overboard")) {
                    lobby.setGameState("evening_bait");
                }
                if (curr_stage.equals("evening_bait")) {
                    lobby.setGameState("evening_shark_damage");
                }
                if (curr_stage.equals("evening_shark_damage")) {
                    lobby.setGameState("evening_zhilet");
                }
                if (curr_stage.equals("evening_zhilet")) {
                    lobby.setGameState("evening_damage_overboard");
                }
                if (curr_stage.equals("evening_damage_overboard")) {
                    lobby.setGameState("evening_thirst");
                }
                if (curr_stage.equals("evening_thirst")) {
                    lobby.setGameState("evening_final");
                }
                if (curr_stage.equals("evening_final")) {
                    lobby.setGameState("morning");
                }
                lobby.setTurn(1);
            } else {
                lobby.setTurn(lobby.getTurn() + 1);
            }
            brawl_showed = false;
            mDatabase.child("lobby").child(lobbyNumber).setValue(lobby);
        }
    }

    private Integer calculatePower(BrawlMember brawlMember){
        Integer toReturn = 0;
        toReturn += brawlMember.getMember().getStats().getPower();
        for (Card q: brawlMember.getMember().getTreasures().getOpen()) {
            if (q.getName().equals("Нож")) {
                toReturn += 3;
            }
            if (q.getName().equals("Дубинка")) {
                toReturn += 2;
            }
            if (q.getName().equals("Весло")) {
                toReturn += 1;
            }
            if (q.getName().equals("Гарпун")) {
                toReturn += 4;
            }
            if (q.getName().equals("Сигнальный пистолет")) {
                toReturn += 8;
            }
        }
        return toReturn;
    }


    private void updateBrawlMember(){
        for (BrawlMember q: lobby.getBrawl().getAttacker()){
            if (q.getMember().getName().equals(lobby.getMembers().get(player_name).getName())){
                q.getMember().getTreasures().setClose(lobby.getMembers().get(player_name).getTreasures().getClose());
                q.getMember().getTreasures().setOpen(lobby.getMembers().get(player_name).getTreasures().getOpen());
            }
        }
        for (BrawlMember q: lobby.getBrawl().getDefender()){
            if (q.getMember().getName().equals(lobby.getMembers().get(player_name).getName())){
                q.getMember().getTreasures().setClose(lobby.getMembers().get(player_name).getTreasures().getClose());
                q.getMember().getTreasures().setOpen(lobby.getMembers().get(player_name).getTreasures().getOpen());
            }
        }
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