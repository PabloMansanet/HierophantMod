package hierophant.relics;

import static hierophant.HierophantMod.makeRelicOutlinePath;
import static hierophant.HierophantMod.makeRelicPath;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import basemod.abstracts.CustomRelic;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

public class BeggarsRobeRelic extends CustomRelic {
    public static final String ID = HierophantMod.makeID("BeggarsRobeRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("donation.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("donation.png"));
    private static final int GOLD_THRESHOLD = 10;
    private static final int POWER_AMOUNT = 4;
    public boolean isActive = false;
    public BeggarsRobeRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                if (!BeggarsRobeRelic.this.isActive && AbstractDungeon.player.gold < GOLD_THRESHOLD) {
                    BeggarsRobeRelic.this.flash();
                    BeggarsRobeRelic.this.pulse = true;
                    AbstractDungeon.player.addPower(
                            new StrengthPower(AbstractDungeon.player, POWER_AMOUNT));
                    AbstractDungeon.player.addPower(
                            new DexterityPower(AbstractDungeon.player, POWER_AMOUNT));
                    AbstractDungeon.actionManager.addToTop(
                            new RelicAboveCreatureAction(AbstractDungeon.player, BeggarsRobeRelic.this));
                    BeggarsRobeRelic.this.isActive = true;
                    AbstractDungeon.onModifyPower();
            }

            this.isDone = true;
        }
        });
    }

    @Override
    public void onLoseGold() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.gold < GOLD_THRESHOLD && !this.isActive && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            this.flash();
            this.pulse = true;
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(p, p, new StrengthPower(p, POWER_AMOUNT), POWER_AMOUNT));
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(p, p, new DexterityPower(p, POWER_AMOUNT), POWER_AMOUNT));
            AbstractDungeon.actionManager.addToTop(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            AbstractDungeon.player.hand.applyPowers();
        }
    }

    @Override
    public void onGainGold() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.gold >= GOLD_THRESHOLD && this.isActive && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            this.pulse = false;
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(p, p, new StrengthPower(p, -POWER_AMOUNT), -POWER_AMOUNT));
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(p, p, new DexterityPower(p, -POWER_AMOUNT), -POWER_AMOUNT));
            this.isActive = false;
            AbstractDungeon.player.hand.applyPowers();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BeggarsRobeRelic();
    }
}
