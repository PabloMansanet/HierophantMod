package hierophant.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.powers.EnlightenedPower;

public abstract class AbstractHierophantCard extends CustomCard {
    public int piety;
    public int basePiety;
    public boolean upgradedPiety;
    public boolean isPietyModified;

    public AbstractHierophantCard(final String id,
                                  final String name,
                                  final String img,
                                  final int cost,
                                  final String rawDescription,
                                  final CardType type,
                                  final CardColor color,
                                  final CardRarity rarity,
                                  final CardTarget target) {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isPietyModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedPiety) {
            piety = basePiety;
            isPietyModified = true; 
        }

    }

    public void upgradePiety(int amount) { 
        basePiety += amount;
        piety = basePiety;
        upgradedPiety = true;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(EnlightenedPower.POWER_ID))
        {
            piety = basePiety * (EnlightenedPower.PIETY_BONUS) / 100;
            isPietyModified = true;
        } else {
            piety = basePiety;
            isPietyModified = false;
        }
    }

}
