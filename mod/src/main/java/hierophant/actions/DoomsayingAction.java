package com.megacrit.cardcrawl.actions.defect;

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

public class DoomsayingAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public int cards;
    private AbstractPlayer p;

    public DoomsayingAction(AbstractPlayer p, int cards) {
        this.actionType = AbstractGameAction.ActionType.ENERGY;
        this.duration = Settings.ACTION_DUR_MED;
        this.cards = cards;
        this.p = p;
    }

    public void update()
    {
        int energyToGain = 0;
        for (int i = 0; i < min(cards, p.drawPile.size(), i++) {
            energyToGain = max(energyToGain, p.drawPile.getNCardFromTop(i).costForTurn);
        }
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(c.costForTurn));
        this.isDone = true;
    }
}
