package daShopBuyer;

import java.awt.*;
import java.util.Arrays;

import net.runelite.api.Actor;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleShop;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;


@ScriptManifest(author = "alex", category = Category.UTILITY, description = "shop", discord = "empty",
        name = "daShop Buyer", servers = { "Battlescape" }, version = "1.0")
public class daShopBuyerMain extends Script {
    public WorldPoint p = new WorldPoint(0, 0, 0);
    @Override
    public void onExecute() {
        ctx.log("Started daShop Buyer");
    }

    @Override
    public void onProcess() {
        p = ctx.getClient().getLocalPlayer().getWorldLocation();
        SimpleNpc a = ctx.npcs.filter(121).nextNearest();
        int b = ctx.players.getLocal().getInteracting().getHealth();
        ctx.log(String.valueOf(b));
    }

    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        ctx.paint.drawTileMatrix(g, ctx.players.getLocal().getLocation(), Color.BLACK);
    }
}
