package KrakenKiller;

import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimplePlayers;
import simple.api.filters.SimplePrayers;
import simple.api.filters.SimpleSkills;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.regex.Pattern;

@ScriptManifest(
        author = "alex",
        category = Category.MONEYMAKING,
        description = "<html>"
                + "<p><strong>Kraken Killer</strong>.</p>"
                + "<ul>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "Kraken Killer but Better",
        servers = {"RuneX"},
        version = "1.0"
)

public class KrakenKillerMain extends Script implements SimpleMessageListener, SimplePaintable {
    public long startTime;
    public String status;
    public int startExperience;
    private KrakenKillerGUI gui;
    private static boolean hidePaint = false;
    public static final WorldArea HOME = new WorldArea(
            new WorldPoint(3086, 3482, 0),
            new WorldPoint(3097, 3498, 0));
    @Override
    public void onChatMessage(ChatMessageEvent chatMessageEvent) {
    }
    @Override
    public boolean onExecute() {
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.getExperience(SimpleSkills.Skill.MINING);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new KrakenKillerGUI();
            }
        });
        return true;
    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.onCloseGUI();
        }
    }
    @Override
    public void onProcess() {
        SimplePrayers prayers = new SimplePrayers(ctx);
        String selectedItem1 = gui.getSelectedItem1();
        String selectedItem2 = gui.getSelectedItem2();
        SimpleNpc Kraken = ctx.npcs.populate().filter("Kraken").nextNearest();
        SimpleItem potion = ctx.inventory.populate().filter(Pattern.compile("Super restore\\(\\d+\\)"), Pattern.compile("Prayer potion\\(\\d+\\)"), Pattern.compile("Sanfew serum\\(\\d+\\)"), Pattern.compile("Prayer flask\\(\\d+\\)"), Pattern.compile("Sanfew flask\\(\\d+\\)")).next();
        SimpleSceneObject bank = ctx.objects.populate().filter("Bank Chest", "Bank Booth").nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (prayers.points() <= 30) {
                final int cached = ctx.prayers.points();
                status("Drinkin Potion");
                if (potion != null && potion.interact(74)) {
                    ctx.onCondition(() -> ctx.prayers.points() > cached, 250, 12);
                }
            }
            if (ctx.inventory.inventoryFull() || potion == null) {
                if (!ctx.players.getLocal().within(HOME)){
                    ctx.menuActions.sendAction(646, 0, 0, 30000, 0);
                } else if (bank != null) {
                    bank.interact(900);
                    status("Restock");
                }
            }
            if (ctx.players.getLocal().within(HOME) && potion != null) {
                //ctx.menuActions.sendAction(315,0,0,60448,0);
                status("goin bacc");
            }
            if ("Normal".equals(selectedItem1) && !prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC, true);
                if ("Eagle Eye".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.EAGLE_EYE, true);
                }
                if ("Rigour".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.RIGOUR, true);
                }
                if ("Mystic Might".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.MYSTIC_MIGHT, true);
                }
                if ("Augury".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.AUGURY, true);
                }
            } else if ("Curses".equals(selectedItem1) && !prayers.prayerActive(SimplePrayers.Prayers.DEFLECT_MAGIC)) {
                prayers.prayer(SimplePrayers.Prayers.DEFLECT_MAGIC, true);
                if ("Leech Magic".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.LEECH_MAGIC, true);
                }
                if ("Leech Range".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.LEECH_RANGE, true);
                }
                if ("Turmoil".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.TURMOIL, true);
                }
            } else if ("Curses - SoulSplit".equals(selectedItem1) && !prayers.prayerActive(SimplePrayers.Prayers.SOUL_SPLIT)) {
                prayers.prayer(SimplePrayers.Prayers.SOUL_SPLIT);
                if ("Leech Magic".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.LEECH_MAGIC, true);
                }
                if ("Leech Range".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.LEECH_RANGE, true);
                }
                if ("Turmoil".equals(selectedItem2)) {
                    prayers.prayer(SimplePrayers.Prayers.TURMOIL, true);
                }
            }
            if (Kraken != null && Kraken.getInteracting() != null && Kraken.getInteracting().equals(ctx.players.getLocal()) && ctx.players.getLocal().getInteracting() == null) {
                Kraken.interact(412);
                status("attacking");
            }
        }
    }
    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void onPaint(Graphics2D g1) {
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(368, 260, 150, 75);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }
        Graphics2D g = (Graphics2D) g1;
        if (!hidePaint) {
            Font font1 = new Font("Karla", Font.BOLD, 10);
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("KrakenKiller  v. " + "1.0", 380, 274);
        }
    }
}
