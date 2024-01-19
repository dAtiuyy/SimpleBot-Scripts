package ImpingHelper;

import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.SimpleActor;
import simple.api.wrappers.SimpleNpc;
import simple.bot.internal.INpc;

import java.awt.*;

@ScriptManifest(
        author = "alex",
        category = Category.UTILITY,
        description = "<html>"
                + "<p><strong>Kills crabs</strong>.</p>"
                + "<ul>"
                + "<li><strong>Start at the crab zone with gear on you</strong>.</li>"
                + "<li><strong>This only works if youre strong enough to not die fighting crabs</strong>.</li>"
                + "<li><strong>It picks up caskets, oysters and it opens the unnoted caskets</strong>.</li>"
                + "<li>Sadly it will pick up everyones caskets and it does steal crabs from others.</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "ShadeKiller",
        servers = {"RuneX"},
        version = "1.0"
)
public class ImpMain extends Script implements SimpleMessageListener, SimplePaintable {
    private WorldPoint ninjaLoc;

    @Override
    public boolean onExecute() {
        return true;
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onProcess() {
        SimpleNpc ninja = ctx.npcs.populate().filter("Ninja impling", "Lucky impling", "Dragon impling", "Lucky impling", "Cursed impling").nextNearest();
        if (ninja != null) {
            ninjaLoc = ninja.getLocation();
        }
    }


    @Override
    public void onPaint(Graphics2D g1) {
        Graphics2D g = (Graphics2D) g1;
        if (ninjaLoc != null) {
            ctx.paint.drawTileMatrix(g, ninjaLoc, "penis");
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);
        }
    }

    @Override
    public void onChatMessage(ChatMessageEvent chatMessageEvent) {

    }
}
