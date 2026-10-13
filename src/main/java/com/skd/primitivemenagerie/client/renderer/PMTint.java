// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Helper that wraps a {@link VertexConsumer} and overrides the per-vertex color, replacing the 1.12
 * {@code GlStateManager.color} calls of the original renderers and layers.
 *
 * @author Daveyx0
 */
public final class PMTint {

    private PMTint() {}

    /** @return a consumer that forces the given color (0-1 range per channel) on every vertex */
    public static VertexConsumer color(VertexConsumer delegate, float r, float g, float b, float a) {
        return new Tinted(delegate, r, g, b, a);
    }

    private static final class Tinted implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float r;
        private final float g;
        private final float b;
        private final float a;

        private Tinted(VertexConsumer delegate, float r, float g, float b, float a) {
            this.delegate = delegate;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            this.delegate.addVertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            this.delegate.setColor((int) (this.r * 255.0F), (int) (this.g * 255.0F), (int) (this.b * 255.0F),
                    (int) (this.a * 255.0F));
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            this.delegate.setUv(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            this.delegate.setUv1(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            this.delegate.setUv2(u, v);
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            this.delegate.setNormal(x, y, z);
            return this;
        }
    }
}
