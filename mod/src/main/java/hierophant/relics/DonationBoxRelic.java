package hierophant.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.rewards.DonationReward;
import hierophant.HierophantMod;
import hierophant.powers.EmbezzlePower;
import hierophant.util.TextureLoader;

import static hierophant.HierophantMod.makeRelicOutlinePath;
import static hierophant.HierophantMod.makeRelicPath;

public class DonationBoxRelic extends CustomRelic {
    // ID, images, text.
    public static final String ID = HierophantMod.makeID("DonationBoxRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("donation.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("donation.png"));

    public DonationBoxRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onVictory() {
        // Don't count embezzled gold
        int reward = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(EmbezzlePower.POWER_ID)) {
            reward = (80 - AbstractDungeon.player.gold + p.getPower(EmbezzlePower.POWER_ID).amount);
        } else {
            reward = (80 - AbstractDungeon.player.gold);
        }
        if (reward > 0) {
            AbstractDungeon.getCurrRoom().rewards.add(new DonationReward(reward));
            flash();
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
