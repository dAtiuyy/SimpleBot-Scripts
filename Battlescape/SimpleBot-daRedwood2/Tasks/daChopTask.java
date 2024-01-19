package daTaskScript.Tasks;

import daTaskScript.daTaskScriptMain;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

public class daChopTask extends Task {
    public daChopTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return !ctx.inventory.inventoryFull();
    }

    @Override
    public void run() {

        if (ctx.players.getLocal().getAnimation() == -1) {
            SimpleObject tree = ctx.objects.populate().filter(29668).nextNearest();
            if (tree != null) {
                daTaskScriptMain.status("Cutting Redwood");
                tree.click("Cut");
                ctx.sleep(2500);
                }
        } else {
            ctx.sleepCondition(() -> ctx.players.getLocal().getAnimation() == -1, 2500);
        }
    }

    @Override
    public String status() {
        return "";
    }
}