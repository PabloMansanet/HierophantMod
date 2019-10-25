package hierophant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static hierophant.HierophantMod.makePowerPath;

public class DeathKnellPower extends AbstractPower implements CloneablePowerInterface, NonStackablePower {
    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());
    private static final int FERVOR = 40;

    public static final String POWER_ID = HierophantMod.makeID("DeathKnellPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("death_knell_big.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("death_knell_small.png"));

    public DeathKnellPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those txtures here.
        region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        description = DESCRIPTIONS[0];

        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL) {
            return 0;
        }
        return damage;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        fontScale = 8.0F;
        amount += stackAmount;
        if (amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
       }
   }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new FervorPower(p, p, FERVOR), FERVOR));
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ReducePowerAction(owner, owner, POWER_ID, 1));
        } if (amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, POWER_ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ReducePowerAction(owner, owner, POWER_ID, 1));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DeathKnellPower(owner, amount);
    }
}
