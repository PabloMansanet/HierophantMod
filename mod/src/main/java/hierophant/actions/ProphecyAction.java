package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

import static java.lang.Integer.min;

public class ProphecyAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private boolean freeToPlayOnce = false;
    boolean upgraded;
    private AbstractPlayer p;
    private int energyOnUse = -1;

    public ProphecyAction(AbstractPlayer p, boolean freeToPlayOnce, boolean upgraded, int energyOnUse) {
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
    }

    public void update()
    {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.upgraded) {
            effect++;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect < 1 || p.drawPile.isEmpty()) {
            this.isDone = true;
            return;
        }

        int index = 0;
        int maxCost = 0;
        for (int i = 0; i < min(effect, p.drawPile.size()); i++) {
            if (p.drawPile.getNCardFromTop(i).costForTurn > maxCost) {
                index = i;
                maxCost = p.drawPile.getNCardFromTop(i).costForTurn;
            }
        }
        AbstractCard cardToPlay = p.drawPile.getNCardFromTop(index);
        p.drawPile.group.remove(cardToPlay);
        AbstractDungeon.getCurrRoom().souls.remove(cardToPlay);
        p.drawPile.addToTop(cardToPlay);
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.PlayTopCardAction(
                AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));


        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
