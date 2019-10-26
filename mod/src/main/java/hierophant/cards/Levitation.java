
package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class Levitation extends AbstractTitheCard {
    public static final String ID = HierophantMod.makeID(Levitation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Levitation.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 5;
    private static final int MAGIC = 1;

    public Levitation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        payTithe();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, this.magicNumber), this.magicNumber));
}

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.exhaust = false;
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
