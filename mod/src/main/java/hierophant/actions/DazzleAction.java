package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

public class DazzleAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public static final String DOUBLOON_ID = HierophantMod.makeID(Doubloon.class.getSimpleName());
    public int block;
    public int doubloonBonus;
    private AbstractPlayer p;

    public DazzleAction(AbstractPlayer p, int block, int doubloonBonus) {
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        this.p = p;
        this.duration = Settings.ACTION_DUR_FAST;
        this.block = block;
        this.doubloonBonus = doubloonBonus;
    }

    public void update()
    {
        int totalBlock = block;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(DOUBLOON_ID)) {
                    totalBlock += doubloonBonus;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, totalBlock));
        this.isDone = true;
    }
}
