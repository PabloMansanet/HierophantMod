package hierophant.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hierophant.DonationReward;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

import static hierophant.HierophantMod.makeRelicOutlinePath;
import static hierophant.HierophantMod.makeRelicPath;

public class DonationBoxRelic extends CustomRelic {
    // ID, images, text.
    public static final String ID = HierophantMod.makeID("DonationBoxRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("donation.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("donation.png"));

    public DonationBoxRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void onVictory() {
        int reward = (100 - AbstractDungeon.player.gold);
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
