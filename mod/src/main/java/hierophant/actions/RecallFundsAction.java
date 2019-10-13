package hierophant.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import hierophant.cards.Doubloon;

public class RecallFundsAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private AbstractPlayer p;
    private int amount;

    public RecallFundsAction(AbstractPlayer p, int amount) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = p;
        this.amount = amount;
    }

    public void update()
    {
        for (int i = 0; i < amount; i++) {
            if (AbstractDungeon.player.hand.size() == 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
                return;
            }
            AbstractCard doubloon = p.exhaustPile.findCardById(Doubloon.ID);
            if (doubloon == null) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(doubloon));
            p.exhaustPile.removeCard(doubloon);
        }
        this.isDone = true;
    }
}
