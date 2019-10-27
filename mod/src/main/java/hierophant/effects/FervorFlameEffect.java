
package hierophant.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.lang.Math;

public class FervorFlameEffect extends AbstractGameEffect {
    private AtlasRegion img;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float vY2;
    private float vS;
    private float startingDuration;
    private boolean flipX = MathUtils.randomBoolean();
    private float delayTimer = MathUtils.random(0.15F);

    public FervorFlameEffect(float x, float y, float radiusMultiplier) {
        this.setImg();
        this.startingDuration = MathUtils.random(0.6F, 1.5F);
        this.duration = this.startingDuration;
        float r = radiusMultiplier * MathUtils.random(-13.0F, 13.0F) * MathUtils.random(-13.0F, 13.0F);
        this.x = x + r * Settings.scale - (float)this.img.packedWidth / 2.0F;
        this.y = y + MathUtils.random(-180.0F, 0.0F) * Settings.scale - (float)this.img.packedHeight / 2.0F;
        this.vX = MathUtils.random(-45.0F, 45.0F) * Settings.scale;
        r = MathUtils.random(3.0F, 30.0F);
        this.vY = 0.2F * r * r / this.startingDuration * Settings.scale;
        this.vY2 = MathUtils.random(-100.0F, 100.0F) * Settings.scale;
        this.vS = MathUtils.random(-0.5F, 0.5F) * Settings.scale;
        this.color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        Color var10000 = this.color;
        var10000.g -= MathUtils.random(0.5F);
        var10000 = this.color;
        var10000.b -= this.color.g - MathUtils.random(0.5F);
        this.rotation = MathUtils.random(-10.0F, 10.0F);
        this.scale = Math.min(radiusMultiplier, 1.5F) * Settings.scale * MathUtils.random(0.1F, 0.8F);
        this.renderBehind = MathUtils.randomBoolean(0.5F);
    }

    public void update() {
        if (this.delayTimer > 0.0F) {
            this.delayTimer -= Gdx.graphics.getDeltaTime();
        } else {
            this.x += this.vX * Gdx.graphics.getDeltaTime();
            this.y += this.vY * Gdx.graphics.getDeltaTime();
            this.vY += this.vY2 * Gdx.graphics.getDeltaTime();
            this.vY *= 59.0F * Gdx.graphics.getDeltaTime();
            this.scale += this.vS * Gdx.graphics.getDeltaTime();
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.isDone = true;
            } else if (this.duration > this.startingDuration / 2.0F) {
                this.color.a = Interpolation.fade.apply(0.0F, 0.5F, (this.startingDuration - this.duration) / (this.startingDuration / 2.0F));
            } else if (this.duration < this.startingDuration / 2.0F) {
                this.color.a = Interpolation.fade.apply(0.0F, 0.5F, this.duration / (this.startingDuration / 2.0F));
            }

        }
    }

    private void setImg() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            this.img = ImageMaster.FLAME_1;
        } else if (roll == 1) {
            this.img = ImageMaster.FLAME_2;
        } else {
            this.img = ImageMaster.FLAME_3;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        if (this.flipX && !this.img.isFlipX()) {
            this.img.flip(true, false);
        } else if (!this.flipX && this.img.isFlipX()) {
            this.img.flip(true, false);
        }

        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}

