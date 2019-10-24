package hierophant.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.powers.PietyPower;

public class FaithHealingAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private int piety;
    private AbstractPlayer p;
    public FaithHealingAction(AbstractPlayer p, int piety) {
        this.actionType = AbstractGameAction.ActionType.EXHAUST;
        this.p = p;
        this.piety = piety;
    }

    public void update()
    {
        int pietyGain = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type == AbstractCard.CardType.STATUS) {
                pietyGain += this.piety;
                AbstractDungeon.actionManager.addToTop(
                        new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
            }
        }

        if (pietyGain > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new PietyPower(p, p, pietyGain), pietyGain));
        }
        this.isDone = true;
    }
}
