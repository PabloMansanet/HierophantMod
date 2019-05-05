package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

public class CornucopiaAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public static final String DOUBLOON_ID = HierophantMod.makeID(Doubloon.class.getSimpleName());
    private boolean freeToPlayOnce = false;
    boolean upgraded;
    private AbstractPlayer p;
    private int energyOnUse = -1;

    public CornucopiaAction(AbstractPlayer p, boolean freeToPlayOnce, boolean upgraded, int energyOnUse) {
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.p = p;
        this.duration = Settings.ACTION_DUR_FAST;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
    }

    public void update()
    {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Doubloon(upgraded), effect));
        }

        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
