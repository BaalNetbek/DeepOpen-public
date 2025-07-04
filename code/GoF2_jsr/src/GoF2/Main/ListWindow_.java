package GoF2.Main;

import GoF2.Layout;

public final class ListWindow_ extends TabbedWindow_ {
	private final boolean unused_37;

	public ListWindow_(final int var1, final int var2, final int var3, final int var4, final String[] var5) {
		super(var1, var2, var3, var4, (String[])null, "");
		this.highlightSelection = false;
		this.innerLeftMargin = var1;
		this.unused_37 = false;
	}

	public final void draw() {
		drawItems();
		drawScroll();
	}

	protected final void drawBG() {
	}

	public final void scrollDown() {
		if (this.perTabEntries[this.selectedTab][this.selectedEntry + 1] != null) {
			++this.selectedEntry;
		} else {
			this.scrollPos = 0;
			this.selectedEntry = 0;
		}

		if (this.selectedEntry > this.scrollPos + this.displayedEntriesCount) {
			++this.scrollPos;
		}

		updateScroll();
	}

	public final void scrollUp() {
		if (this.selectedEntry > 0) {
			--this.selectedEntry;
		} else {
			int var1 = 1;

			while(this.perTabEntries[this.selectedTab][var1++] != null) {
			}

			this.selectedEntry = var1 - 2;
			this.scrollPos = var1 - 1 - this.displayedEntriesCount;
		}

		if (this.scrollPos > 0) {
			--this.scrollPos;
		}

		updateScroll();
	}

	public final void drawItems() {
		for(int var1 = this.scrollPos; var1 < this.perTabEntries[this.selectedTab].length && this.perTabEntries[this.selectedTab][var1] != null && var1 < this.scrollPos + this.displayedEntriesCount + 1; ++var1) {
			drawItem(this.perTabEntries[this.selectedTab][var1], var1);
		}

	}

	protected final void drawItem(final Object var1, final int var2) {
		Layout.drawTextItem(var1.toString(), this.innerLeftMargin, this.itemListPosY + (var2 - this.scrollPos) * this.rowHeight, this.scrollThumbSize > 0 ? this.width - 7 : this.width, var2 == this.selectedEntry && this.highlightSelection);
	}

	public final int getFirstListLen_() {
		return this.perTabEntries != null && this.perTabEntries[0] != null ? this.listsLengths[0] : 0;
	}
}
