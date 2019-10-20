package hierophant.cards;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class AbstractTitheCard extends AbstractDynamicCard {
    public static final int GOLD_PER_ENERGY = 20;

    public AbstractTitheCard(final String id,
                             final String img,
                             final int cost,
                             final CardType type,
                             final CardColor color,
                             final CardRarity rarity,
                             final CardTarget target) {

        super(id, img, cost, type, color, rarity, target);
    }

    protected void payTithe()
    {
        if (!this.freeToPlayOnce) {
            AbstractDungeon.player.loseGold(GOLD_PER_ENERGY * (this.costForTurn - EnergyPanel.totalCount));
        }
    }

    public boolean hasEnoughEnergy()
    {
        return super.hasEnoughEnergy()
            || (AbstractDungeon.player.gold >= GOLD_PER_ENERGY * (this.costForTurn - EnergyPanel.totalCount));
    }

}
