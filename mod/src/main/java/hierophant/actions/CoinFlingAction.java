package hierophant.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

public class CoinFlingAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public static final String DISCARD_TEXT = "Discard";
    public static final String DOUBLOON_ID = HierophantMod.makeID(Doubloon.class.getSimpleName());
    public static final int DOUBLOON_BONUS = 2;
    public int damage;
    private AbstractPlayer p;
    private AbstractMonster m;

    public CoinFlingAction(AbstractMonster m, int damage) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damage = damage;
        this.m = m;
    }

    public void update()
    {
        int damageMultiplier = 1;
        if (this.duration == com.megacrit.cardcrawl.core.Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                damageMultiplier = 1;
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() == 1) {
                if (this.p.hand.getBottomCard().cardID.equals(DOUBLOON_ID)) {
                    damageMultiplier = DOUBLOON_BONUS;
                }
                this.p.hand.moveToDiscardPile(this.p.hand.getBottomCard());
                tickDuration();
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(DISCARD_TEXT, 1, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c.cardID.equals(DOUBLOON_ID)) {
                    damageMultiplier = DOUBLOON_BONUS;
                }
                this.p.hand.moveToDiscardPile(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage * damageMultiplier, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.isDone = true;
    }
}
