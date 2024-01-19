package Killer;

import simple.api.actions.SimpleItemActions;
import simple.api.events.ChatMessageEvent;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;

@ScriptManifest(
        author = "alex",
        category = Category.COMBAT,
        description = "<html>"
                + "<p><strong>Kills crabs and experiments</strong>.</p>"
                + "<ul>"
                + "<li><strong>Start at the crab zone or the experiments zone with gear on you</strong>.</li>"
                + "<li><strong>This only works if youre strong enough to not die fighting crabs or experiments</strong>.</li>"
                + "<li><strong>It picks up caskets, oysters and it opens the unnoted caskets</strong>.</li>"
                + "<li>Sadly it will pick up everyones caskets and it does steal npcs from others.</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "Crab&ExperimentsKiller",
        servers = {"RuneX"},
        version = "2.0"
)

public class KillerMain extends Script implements SimpleMessageListener, SimplePaintable {
    public String status;
    public long startTime;
    private KillerGUI gui;
    private static boolean hidePaint = false;
    private static final int[] DESIRED_ITEMS = {405, 406, 407};
    private static final int[] OPEN_ITEMS = {405, 407};
    @Override
    public boolean onExecute() {
        this.startTime = System.currentTimeMillis();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new KillerGUI();
            }
        });
        return true;
    }
    @Override
    public void onProcess() {
        boolean openCEnabled = gui.OpenCEnabled();
        SimpleGroundItem items = ctx.groundItems.populate().filter(DESIRED_ITEMS).nearest().next();
        SimpleItem casket = ctx.inventory.populate().filter(OPEN_ITEMS).next();
        if (gui != null && gui.isScriptStarted()) {
            if (!ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty()) {
                status("Looting items");
                items.interact("Take");
                ctx.sleepCondition(() -> !ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty(), 1000);
            }
            if (casket != null & openCEnabled) {
                status("Opening stuff");
                casket.interact(SimpleItemActions.CONSUME);
                ctx.sleepCondition(() -> !ctx.inventory.contains(casket), 1000);
            }
            if (ctx.players.getLocal().getInteracting() == null) {
                SimpleNpc target = ctx.npcs.populate().filter("Rock Crab", "Experiment").nextNearest();
                if (target != null && ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty()) {
                    target.interact("Attack");
                }
            }
            status("Attacking shit");
        }
    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.onCloseGUI();
        }
    }
    @Override
    public void onChatMessage(ChatMessageEvent m) {}
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
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);
            Font font1 = new Font("Karla", Font.BOLD, 10);
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("Killer  v. " + "2.0", 380, 274);
        }
    }
}
