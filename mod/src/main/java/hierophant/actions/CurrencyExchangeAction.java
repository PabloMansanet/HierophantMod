package com.megacrit.cardcrawl.actions.defect;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.HierophantMod;
import hierophant.cards.Doubloon;

public class CurrencyExchangeAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public static final String EXHAUST_TEXT = "Exhaust";
    public static final String DOUBLOON_ID = HierophantMod.makeID(Doubloon.class.getSimpleName());
    public static final int DOUBLOON_BONUS = 2;
    public int amount;
    private AbstractPlayer p;

    public CurrencyExchangeAction(int amount) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
    }

    public void update()
    {
        if (this.duration == com.megacrit.cardcrawl.core.Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return; 
            }
            if (this.p.hand.size() == 1) {
                if (this.p.hand.getBottomCard().cardID.equals(DOUBLOON_ID)) {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(this.amount + DOUBLOON_BONUS));
                } else {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(this.amount));
                }
                this.p.hand.moveToExhaustPile(this.p.hand.getBottomCard());
                tickDuration();
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(EXHAUST_TEXT, 1, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c.cardID.equals(DOUBLOON_ID)) {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(this.amount + DOUBLOON_BONUS));
                } else {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(this.amount));
                }
                this.p.hand.moveToExhaustPile(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }

        tickDuration();
    }
}
