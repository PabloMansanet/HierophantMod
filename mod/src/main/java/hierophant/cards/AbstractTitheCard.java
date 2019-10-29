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
    public static final int GOLD_PER_ENERGY = 10;

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
        if (!this.freeToPlayOnce && (this.costForTurn > EnergyPanel.totalCount)) {
            AbstractDungeon.player.loseGold(GOLD_PER_ENERGY * (this.costForTurn - EnergyPanel.totalCount));
        }
    }

    public boolean hasEnoughEnergy()
    {
        boolean playable = super.hasEnoughEnergy();

        // Very hacky, but hasEnoughEnergy() doesn't make this easy. This text means that
        // the specific reason why it's failing is because of lack of energy, and not
        // other reasons such as Entangle
        if ((this.cantUseMessage == AbstractCard.TEXT[11]) &&
                (AbstractDungeon.player.gold >=
                    GOLD_PER_ENERGY * (this.costForTurn - EnergyPanel.totalCount)))
        {
            this.cantUseMessage = null;
            return true;
        }
        return playable;
    }

}
