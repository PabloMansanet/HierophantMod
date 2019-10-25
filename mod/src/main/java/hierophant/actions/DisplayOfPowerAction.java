package hierophant.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DisplayOfPowerAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private AbstractPlayer p;
    public static final String CHOOSE_TEXT = "place on top of your Draw Pile.";
    public int damage;
    public int damageMultiplier;
    private AbstractMonster m;

    public DisplayOfPowerAction(AbstractMonster m, int damage) {
        this.p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damage = damage;
        this.damageMultiplier = 0;
        this.m = m;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
            } else if (this.p.hand.size() == 1) {
                AbstractCard c = this.p.hand.getTopCard();
                this.damageMultiplier = c.costForTurn;
                this.p.hand.moveToDeck(c, false);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, damage * damageMultiplier, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));

                this.isDone = true;
            } else {
                AbstractDungeon.handCardSelectScreen.open(CHOOSE_TEXT, 1, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard c;
                for(Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); this.p.hand.moveToDeck(c, false)) {
                    c = (AbstractCard)var1.next();
                    this.damageMultiplier = c.costForTurn;

                }
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, damage * damageMultiplier, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                this.isDone = true;
            }

            this.tickDuration();
        }
    }

}
