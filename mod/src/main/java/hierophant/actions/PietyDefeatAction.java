package hierophant.actions;

import java.util.HashMap;
import java.util.Map;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.random.Random;

import hierophant.patches.EscapeFieldPatch;

public class PietyDefeatAction extends AbstractGameAction {
    private AbstractMonster m;
    private static final Map<String, String[]> dialogs = new HashMap<String, String[]>();

    public PietyDefeatAction(AbstractMonster target) {
        this(target, true);
        dialogs.put("Cultist", new String[]{"CA-CAW! HAIL THE NEW GODS! CA-CAW!", "CA-CAW! THE LIGHT SHINES!"});
        dialogs.put("SlaverBlue", new String[]{"Slavery isn't my thing anyway..."});
        dialogs.put("SlaverRed", new String[]{"I was the slave all along!"});
        dialogs.put("Champ", new String[]{"ALL THIS VIOLENCE! ALL FOR NOTHING!", "SKY IS THE LIMIT!"});
        dialogs.put("GremlinLeader", new String[]{"My children, what have I done?"});
        dialogs.put("Chosen", new String[]{"Will I ever atone for my sins?"});
        dialogs.put("Mystic", new String[]{"Your Gods and mine are all but the same."});
        dialogs.put("Centurion", new String[]{"I should've listened to her..."});
        dialogs.put("GiantHead", new String[]{"Hello... Theism..."});
        dialogs.put("Mugger", new String[]{"I think I will donate this gold to charity."});
        dialogs.put("Looter", new String[]{"This money will feed many hungry mouths."});
    }

    public void pietyDialog(AbstractMonster mo) {
        Random rand = new Random();
        int chance = 30;

        if (rand.random(100) > chance)  {
            return;
        }

        String[] dialogStrings = dialogs.get(mo.id);
        if (dialogStrings != null) {
            String dialog = dialogStrings[rand.random(0, dialogStrings.length-1)];
            AbstractDungeon.actionManager.addToBottom(new TalkAction(mo, dialog, 1.0F, 2.0F));
        }
    }

    public PietyDefeatAction(AbstractMonster target, boolean triggerRelics) {
        this.duration = 0.0F;
        this.actionType = ActionType.DAMAGE;
        this.m = target;
    }

    public void monsterSpecificPietyTriggers(AbstractMonster mo) {
        if (mo.hasPower(SporeCloudPower.POWER_ID)) {
            mo.powers.remove(mo.getPower(SporeCloudPower.POWER_ID));
        }

        if (mo instanceof Darkling) {
            Darkling darkling = (Darkling)mo;
            darkling.halfDead = true;
        } else if ((mo instanceof GremlinLeader)
                || (mo instanceof Reptomancer)
                || (mo instanceof TheCollector))
        {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new PietyDefeatAction(m));
                }
            }
        }
    }

	public void update() {
        if (this.duration == 0.0F) {
            this.m.currentHealth = 0;
            monsterSpecificPietyTriggers(m);
            pietyDialog(m);
            m.die(true);
            m.healthBarUpdatedEvent();
            EscapeFieldPatch.escapingPiety.set(m, true);
        }

        this.tickDuration();
    }
}
