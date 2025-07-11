package AE;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Texture2D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

import AE.Math.AEMath;
import AE.PaintCanvas.AEGraphics3D;

public final class ParticleSystemMesh extends AbstractMesh {
	private static Transform calcTransform = new Transform();
	private static float[] transformValues = new float[16];
	private Appearance appearance;
	private final int quadCount;
	private Mesh mesh;
	private VertexBuffer vertexBuffer;
	private VertexArray vertexArray;
	private VertexArray uvArray;
	private TriangleStripArray tStrips;
	private short[] vertices;
	private byte[] uvs;
	private static float[] zeroBias3D = {0.0F, 0.0F, 0.0F};
	private static float[] zeroBias2D = {0.0F, 0.0F};
	private int textureWidth;
	private final int color = 0xffffffff;

	public ParticleSystemMesh(final int resourceId, int quadCount, final byte blending) {
		this.resourceId = resourceId;
		if (this.appearance == null) {
			this.appearance = new Appearance();
			if (blending != 0) {
				final CompositingMode var5 = new CompositingMode();
				switch(blending) {
				case 1:
				case 3:
					var5.setBlending(64);
					break;
				case 2:
					var5.setBlending(65);
				}

				var5.setDepthWriteEnable(false);
				var5.setDepthTestEnable(true);
				this.appearance.setCompositingMode(var5);
			}

			PolygonMode var6;
			(var6 = new PolygonMode()).setCulling(162);
			var6.setShading(164);
			var6.setPerspectiveCorrectionEnable(false);
			this.appearance.setPolygonMode(var6);
		}

		this.quadCount = quadCount;
		this.vertices = new short[quadCount * 12];
		this.uvs = new byte[quadCount * 8];
		this.vertexBuffer = new VertexBuffer();
		this.vertexArray = new VertexArray(quadCount * 4, 3, 2);
		this.uvArray = new VertexArray(quadCount * 4, 2, 1);
		this.vertexArray.set(0, quadCount * 4, this.vertices);
		this.uvArray.set(0, quadCount * 4, this.uvs);
		this.vertexBuffer.setPositions(this.vertexArray, 1.0F, zeroBias3D);
		this.vertexBuffer.setTexCoords(0, this.uvArray, 0.00390625F, zeroBias2D);
		final int[] var7 = new int[quadCount * 6];
		int var8 = 0;

		for(int var4 = 0; var4 < var7.length; var8 += 4) {
			var7[var4] = var8;
			var7[var4 + 1] = var8 + 2;
			var7[var4 + 2] = var8 + 1;
			var7[var4 + 3] = var8 + 3;
			var7[var4 + 4] = var8 + 2;
			var7[var4 + 5] = var8;
			var4 += 6;
		}

		final int[] var9 = new int[quadCount * 2];

		for(quadCount = 0; quadCount < var9.length; ++quadCount) {
			var9[quadCount] = 3;
		}

		this.tStrips = new TriangleStripArray(var7, var9);
		this.mesh = new Mesh(this.vertexBuffer, this.tStrips, this.appearance);
	}

	private ParticleSystemMesh(final ParticleSystemMesh var1) {
		this.quadCount = var1.quadCount;
		this.mesh = var1.mesh;
		this.vertexBuffer = var1.vertexBuffer;
		this.vertexArray = var1.vertexArray;
		this.uvArray = var1.uvArray;
		this.tStrips = var1.tStrips;
		this.vertices = var1.vertices;
		this.uvs = var1.uvs;
		this.textureWidth = var1.textureWidth;
		this.radius = var1.radius;
		this.draw = var1.draw;
		this.renderLayer = var1.renderLayer;
	}

	public final GraphNode clone() {
		return new ParticleSystemMesh(this);
	}

	public final void OnRelease() {
		this.mesh = null;
		this.vertexBuffer = null;
		this.vertexArray = null;
		this.uvArray = null;
		this.tStrips = null;
		this.vertices = null;
		this.uvs = null;
	}

	public final void render() {
		this.matrix.toFloatArray(transformValues);
		calcTransform.set(transformValues);
		AEGraphics3D.graphics3D.render(this.mesh, calcTransform);
	}

	public final void appendToRender(final Camera var1, final Renderer var2) {
		if (this.draw) {
			this.matrix = var1.localTransformation.getInverse(this.matrix);
			this.matrix.multiply(this.localTransformation);
			var2.drawNode(this.renderLayer, this);
		}

	}

	public final void setMeshData_(final int[] vc, final int[] var2) {
		int i;
		for(i = 0; i < vc.length; ++i) {
			this.vertices[i] = (short)vc[i];
		}

		this.vertexArray.set(0, 4 * this.quadCount, this.vertices);
		this.mesh.getVertexBuffer().setPositions(this.vertexArray, 1.0F, zeroBias3D);

		for(i = 0; i < var2.length; ++i) {
			this.uvs[i] = (byte)var2[i];
		}

		this.uvArray.set(0, 4 * this.quadCount, this.uvs);
		this.mesh.getVertexBuffer().setTexCoords(0, this.uvArray, 1.0F / this.textureWidth, zeroBias2D);
		this.mesh.getVertexBuffer().setDefaultColor(this.color);
		this.radius = 0;

		for(i = 0; i < vc.length; i += 3) {
			final int var4 = vc[i]     * vc[i]
					+ vc[i + 1] * vc[i + 1]
							+ vc[i + 2] * vc[i + 2];
			if (this.radius < var4) {
				this.radius = var4;
			}
		}

		this.radius = AEMath.sqrt(this.radius);
	}

	public final void setTexture(final ITexture texture) {
		final Texture2D var2 = new Texture2D(((JSRTexture)texture).getTexturesArray()[0].getImage());
		var2.setBlending(Texture2D.FUNC_MODULATE);
		var2.setFiltering(Texture2D.FILTER_BASE_LEVEL, Texture2D.FILTER_NEAREST);
		var2.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
		this.appearance.setTexture(0, var2);
		this.textureWidth = var2.getImage().getWidth();
	}
}
