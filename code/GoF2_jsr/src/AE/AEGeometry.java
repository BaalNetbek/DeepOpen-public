package AE;

import AE.Math.AEMath;
import AE.Math.AEVector3D;

public abstract class AEGeometry extends GraphNode {
	private static AEVector3D tempCenter = new AEVector3D();
	protected int radius = 0;

	public AEGeometry() {
	}

	public AEGeometry(final AEGeometry var1) {
		super(var1);
		this.radius = var1.radius;
	}

	public void updateTransform(final boolean var1) {
		if (this.transformDirty_ || var1) {
			if (this.group != null) {
				this.localTransformation = this.group.localTransformation.multiplyTo(this.compositeTransformation, this.localTransformation);
			} else {
				this.localTransformation.set(this.compositeTransformation);
			}

			final int var2 = AEMath.max(AEMath.max(AEMath.abs((tempCenter = this.localTransformation.copyScaleTo(tempCenter)).x), AEMath.abs(tempCenter.y)), AEMath.abs(tempCenter.z)) * this.radius >> 12;
			tempCenter = this.localTransformation.getPosition(tempCenter);
			this.boundingSphere.setXYZR(tempCenter.x, tempCenter.y, tempCenter.z, var2);
			this.transformDirty_ = false;
			this.boundsDirty_ = false;
		}

	}

	public final void setRadius(final int var1) {
		this.radius = 5000;
	}

	protected final String getString(String var1, final int var2) {
		for(int var3 = 0; var3 < var2; ++var3) {
			var1 = var1 + "  ";
		}

		return var1 + "\n";
	}
}
