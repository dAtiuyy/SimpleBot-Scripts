package Figher;

import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimplePrayers;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.filters.SimpleSkills;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.*;

import java.awt.*;
import java.util.regex.Pattern;
import java.awt.Graphics;
import java.awt.Graphics2D;

@ScriptManifest(
        author = "alex",
        category = Category.COMBAT,
        description = "<html>"
                + "<p><strong>Helps you do slayer</strong>.</p>"
                + "<ul>"
                + "<li><strong>Start whenever you want, it does actions only when it needs to</strong>.</li>"
                + "<li><strong>It drinks prayer pots and collects items for you</strong>.</li>"
                + "<li><strong>It picks up clues, ckey pieces,barrows amulets, dark totem pieces, ancient sharks, brim keys and some valuable items</strong>.</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "Figher",
        servers = {"RuneX"},
        version = "1.0"
)

public class FigherMain extends Script implements SimpleMessageListener, SimplePaintable {
    public String status;
    public long startTime;
    private static final int[] DESIRED_ITEMS = {405};

    public int targetID = 1265;

    @Override
    public boolean onExecute() {
        System.out.println("Started daSlayerHelper");
        this.startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void onProcess() {
        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleGroundItem items = ctx.groundItems.populate().filter(DESIRED_ITEMS).nearest().next();

        if (!ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty()) {
            status("Looting items");
            items.interact("Take");
            ctx.sleepCondition(() -> !ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty(), 1000);
        } // picks up whatever u tell it to lol

        if (prayers.points() <= 30){
            final SimpleItem potion = ctx.inventory.populate().filter(Pattern.compile("Prayer potion\\(\\d+\\)")).next();
            final int cached = ctx.prayers.points();
            status("Drinking Prayer Potion");
            if (potion != null && potion.interact(0)) {
                ctx.onCondition(() -> ctx.prayers.points() > cached, 250, 12);
            }
        } // da pray point check and use pot

        if (ctx.players.getLocal().getInteracting() == null) {
            SimpleNpc target = ctx.npcs.populate().filter(targetID).nextNearest();
            if (target != null && ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty()) {
                target.interact("Attack");
            }
        }
        status("Attacking shit");
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void onChatMessage(ChatMessageEvent m) {
    }


    public void status(final String status) {
        this.status = status;
    }

    @Override
    public void onPaint(Graphics2D g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(368, 260, 150, 75);
        g.setColor(Color.BLACK);
        g.drawRect(368, 260, 150, 75);
        //g.fillRect(0, 338, 519, 165); //perfect black chat box

        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.setColor(Color.GRAY);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
        g.drawString("Status: " + status, 380, 298);
        Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
        g.setFont(font2);
        g.setColor(Color.WHITE);
        g.drawString("Figher  v. " + "2.0", 380, 274);
    }
}
