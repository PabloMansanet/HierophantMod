package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import hierophant.cards.Doubloon;

public class CursedGoldAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public CursedGoldAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractCard doubloon = p.exhaustPile.findCardById(Doubloon.ID);
        if (doubloon == null) {
            this.isDone = true;
            return;
        }
        AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(doubloon, true, false));
        p.exhaustPile.removeCard(doubloon);
        this.isDone = true;
    }
}
