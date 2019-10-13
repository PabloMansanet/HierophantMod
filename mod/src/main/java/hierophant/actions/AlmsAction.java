package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

import static java.lang.Integer.min;
import java.util.Random;

public class AlmsAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private boolean freeToPlayOnce = false;
    boolean upgraded;
    private AbstractPlayer p;

    public AlmsAction(AbstractPlayer p, boolean freeToPlayOnce, boolean upgraded, int energyOnUse) {
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
    }

    public void update()
    {
        if (p.hand.isEmpty()) {
            isDone = true;
            return;
        }

        AbstractCard cardToPlay = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
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
