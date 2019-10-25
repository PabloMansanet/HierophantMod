package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class AuricShield extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(AuricShield.class.getSimpleName());
    public static final String IMG = makeCardPath("AuricShield.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 3;
    private static final int COST = 1;

    public AuricShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        block = baseBlock = 0;
    }

    @Override
     public void use(AbstractPlayer p, AbstractMonster m) {
        baseBlock = (p.gold * magicNumber) / 100;
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        baseBlock = (p.gold * magicNumber) / 100;
        super.applyPowers();
        rawDescription = DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}
