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

public class DisplayOfPowerAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    public static final String CHOOSE_TEXT = "Choose";
    public int damage;
    public int damageMultiplier;
    private AbstractMonster m;

    public DisplayOfPowerAction(AbstractMonster m, int damage) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damage = damage;
        this.damageMultiplier = 0;
        this.m = m;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.duration == com.megacrit.cardcrawl.core.Settings.ACTION_DUR_FAST) {
            if (p.hand.isEmpty()) {
                this.isDone = true;
                return; 
            }
            if (p.hand.size() == 1) {
                damageMultiplier = p.hand.getBottomCard().costForTurn;
                tickDuration();
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(CHOOSE_TEXT, 1, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                damageMultiplier = c.costForTurn;
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage * damageMultiplier, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        this.isDone = true;
    }
}
