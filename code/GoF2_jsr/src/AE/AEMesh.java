package AE;

import java.io.DataInputStream;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Fog;
import javax.microedition.m3g.Material;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Texture2D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

import AE.PaintCanvas.AEGraphics3D;

public final class AEMesh extends AbstractMesh {
	private static float[] transformArray = new float[16];
	private DataInputStream aemFile;
	private VertexBuffer vertexBuffer;
	private Appearance appearance;
	private TriangleStripArray triangleStripArray;
	private Transform transform;
	private boolean isTransparent;
	private static PolygonMode opaquePMode;
	private static PolygonMode transparentPMode;
	private static CompositingMode compositingMode;

	public AEMesh(final String var1, final int var2) {
		setupMaterial();
		final String var3 = var1;
		try {
			if (!var3.endsWith(".aem")) {
				this.aemFile = new DataInputStream(this.getClass().getResourceAsStream(var3 + ".aem"));
			} else {
				this.aemFile = new DataInputStream(this.getClass().getResourceAsStream(var3));
			}

			final byte[] var4 = new byte[7];
			this.aemFile.read(var4, 0, 7);
			int var5;
			int var7;
			int var12 = this.aemFile.readUnsignedByte();
			if ((var12 & 16) != 0) {
				final int[] var6 = new int[var5 = swapBytes(this.aemFile.readUnsignedShort())];

				for(var7 = 0; var7 < var5; ++var7) {
					var6[var7] = swapBytes(this.aemFile.readUnsignedShort());
				}

				final int[] var13 = new int[var7 = swapBytes(this.aemFile.readUnsignedShort())];

				for(int var8 = 0; var8 < var7; ++var8) {
					var13[var8] = swapBytes(this.aemFile.readUnsignedShort());
				}

				this.triangleStripArray = new TriangleStripArray(var6, var13);
			}

			final short[] var15 = new short[(var5 = swapBytes(this.aemFile.readUnsignedShort())) * 3];

			for(var7 = 0; var7 < var5 * 3; ++var7) {
				var15[var7] = (short)swapBytes(this.aemFile.readShort());
			}

			VertexArray var16;
			(var16 = new VertexArray(var15.length / 3, 3, 2)).set(0, var15.length / 3, var15);
			VertexArray var14 = null;
			if ((var12 & 2) != 0) {
				final short[] var17 = new short[var15.length / 3 << 1];

				for(int var9 = 0; var9 < var17.length; var9 += 2) {
					var17[var9] = (short)swapBytes(this.aemFile.readShort());
					var17[var9 + 1] = (short)(255 - (short)swapBytes(this.aemFile.readShort()));
				}

				(var14 = new VertexArray(var17.length / 2, 2, 2)).set(0, var17.length / 2, var17);
			}

			if ((var12 & 4) != 0) {
				final short[] var19 = new short[var15.length];

				for(var12 = 0; var12 < var19.length; ++var12) {
					var19[var12] = (short)swapBytes(this.aemFile.readShort());
				}

				new VertexArray(var19.length / 3, 3, 2).set(0, var19.length / 3, var19);
			}

			this.vertexBuffer = new VertexBuffer();
			this.vertexBuffer.setPositions(var16, 1.0F, (float[])null);
			this.vertexBuffer.setNormals((VertexArray)null);
			this.vertexBuffer.setTexCoords(0, var14, 0.003921569F, (float[])null);
			this.appearance = new Appearance();
			this.isTransparent = this.aemFile.read() != 0;
			if (this.isTransparent) {
				this.appearance.setCompositingMode(compositingMode);
				this.appearance.setPolygonMode(transparentPMode);
			} else {
				this.appearance.setCompositingMode((CompositingMode)null);
				this.appearance.setPolygonMode(opaquePMode);
			}

			this.transform = new Transform();
			this.transform.setIdentity();
			this.aemFile.close();
			System.gc();
		} catch (final Exception var10) {
			System.out.println("Error loading aemesh = " + var1);
			var10.printStackTrace();
		}

		this.radius = var2;
	}

	private AEMesh(final AEMesh var1) {
		super(var1);
		setupMaterial();
		this.vertexBuffer = var1.vertexBuffer;
		this.triangleStripArray = var1.triangleStripArray;
		this.transform = var1.transform;
		this.appearance = var1.appearance;
		this.isTransparent = var1.isTransparent;
		this.renderLayer = var1.renderLayer;
		this.draw = var1.draw;
		this.radius = var1.radius;
		this.resourceId = var1.resourceId;
	}

	private static void setupMaterial() {
		if (opaquePMode == null) {
			(opaquePMode = new PolygonMode()).setCulling(160);
			opaquePMode.setShading(164);
			opaquePMode.setPerspectiveCorrectionEnable(false);
			opaquePMode.setLocalCameraLightingEnable(false);
			opaquePMode.setTwoSidedLightingEnable(false);
			opaquePMode.setWinding(168);
		}

		if (transparentPMode == null) {
			(transparentPMode = new PolygonMode()).setCulling(162);
			transparentPMode.setShading(164);
			transparentPMode.setPerspectiveCorrectionEnable(false);
		}

		if (compositingMode == null) {
			(compositingMode = new CompositingMode()).setBlending(64);
			compositingMode.setDepthTestEnable(true);
			compositingMode.setDepthWriteEnable(false);
		}

	}

	public final void setTexture(final ITexture var1) {
		final Texture2D[] var2 = ((JSRTexture)var1).getTexturesArray();
		this.appearance.setTexture(0, var2[0]);
	}

	public final GraphNode clone() {
		return new AEMesh(this);
	}

	public final void OnRelease() {
		this.aemFile = null;
		this.matrix = null;
		this.group = null;
		this.parent = null;
		this.compositeTransformation = null;
		this.localTransformation = null;
		this.boundingSphere = null;
		if (this.appearance != null) {
			this.appearance.setCompositingMode((CompositingMode)null);
			this.appearance.setFog((Fog)null);
			this.appearance.setMaterial((Material)null);
			this.appearance.setPolygonMode((PolygonMode)null);
			this.appearance.setTexture(0, (Texture2D)null);
			this.appearance.setUserID(0);
			this.appearance = null;
		}

		this.vertexBuffer = null;
		this.appearance = null;
		this.triangleStripArray = null;
		this.transform = null;
		System.gc();
	}

	public final void render() {
		if (!this.isTransparent) {
			this.matrix.toFloatArray(transformArray);
			this.transform.set(transformArray);
			if (this.vertexBuffer != null && this.triangleStripArray != null && this.appearance != null && this.transform != null && AEGraphics3D.graphics3D != null) {
				AEGraphics3D.graphics3D.render(this.vertexBuffer, this.triangleStripArray, this.appearance, this.transform);
			}
		}

	}

	public final void renderTransparent() {
		if (this.isTransparent) {
			this.matrix.toFloatArray(transformArray);
			this.transform.set(transformArray);
			if (this.vertexBuffer != null && this.triangleStripArray != null && this.appearance != null && this.transform != null && AEGraphics3D.graphics3D != null) {
				AEGraphics3D.graphics3D.render(this.vertexBuffer, this.triangleStripArray, this.appearance, this.transform);
			}
		}

	}

	private static int swapBytes(final int var0) {
		return (var0 & 255) << 8 | var0 >> 8 & 255;
	}
}
