package hierophant.powers;

import static hierophant.HierophantMod.makePowerPath;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.interfaces.CloneablePowerInterface;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

// Defeat any enemies with lower or equal HP than your PietyBar at end of turn.
public class PietyBarPower extends AbstractPower implements CloneablePowerInterface, InvisiblePower, HealthBarRenderPower, NonStackablePower {
    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());
    public AbstractCreature source;

    public static final String POWER_ID = HierophantMod.makeID("PietyBarPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("enlightened_big.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("enlightened_small.png"));

    public PietyBarPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.type = PowerType.BUFF;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.description = DESCRIPTIONS[0];
        updateDescription();
    }

    @Override
    public int getHealthBarAmount()
    {
        int amount = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(PietyPower.POWER_ID)) {
            amount = p.getPower(PietyPower.POWER_ID).amount;
        }
        return amount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new PietyBarPower(owner, source, amount);
    }

    public Color getColor()
    {
      return Color.YELLOW;
    }

}
