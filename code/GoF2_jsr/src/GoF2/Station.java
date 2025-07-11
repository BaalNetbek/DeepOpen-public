package GoF2;

public final class Station {
    private final String name;
    private final int id;
    private final int systemId;
    private final int planetTextureId;
    private final int tecLevel;
    private Item[] hangarItems;
    private Ship[] hangarShips;
    private Agent[] barAgents;

    public Station(final String name, final int id, final int systemId, final int tecLevel, final int planetTextureId) {
        this.name = name;
        this.id = id;
        this.systemId = systemId;
        this.tecLevel = tecLevel;
        this.planetTextureId = planetTextureId;
        this.hangarItems = null;
        this.hangarShips = null;
        this.barAgents = null;
    }

    public Station() {
        this("", -1, -1, 0, 0);
    }

    public final int getSystemIndex() {
        return this.systemId;
    }

    public final String getName() {
        return this.name;
    }

    public final int getId() {
        return this.id;
    }

    public final boolean isAttackedByAliens() {
        return this.id == Status.wormholeStation;
    }

    public final int getPlanetTextureId() {
        return this.planetTextureId;
    }

    public final boolean isDiscovered() {
        return Galaxy.getVisitedStations()[this.id];
    }

    public final void visit() {
        if (!isDiscovered()) {
            Status.incStationsVisited();
            Galaxy.visitStation(this.id);
        }

    }

    public final int getTecLevel() {
        return this.tecLevel;
    }

    public final Ship[] getShopShips() {
        return this.hangarShips;
    }

    public final Item[] getShopItems() {
        return this.hangarItems;
    }

    public final void setShopItems(final Item[] var1) {
        this.hangarItems = var1;
    }

    public final void setShopShips(final Ship[] var1) {
        this.hangarShips = var1;
    }

    public final void setBarAgents(final Agent[] var1) {
        this.barAgents = var1;
    }

    public final void addItem(final Item var1) {
        if (this.hangarItems == null) {
            this.hangarItems = new Item[]{var1};
        } else {
            for(int var2 = 0; var2 < this.hangarItems.length; ++var2) {
                if (this.hangarItems[var2].equals(var1)) {
                    this.hangarItems[var2].changeAmount(var1.getAmount());
                    return;
                }
            }

            final Item[] var3 = new Item[this.hangarItems.length + 1];
            System.arraycopy(this.hangarItems, 0, var3, 0, this.hangarItems.length);
            var3[var3.length - 1] = var1;
            this.hangarItems = var3;
        }
    }

    public final Agent[] getBarAgents() {
        return this.barAgents;
    }

    public final String toString() {
        return this.name;
    }

    public final boolean equals(final Station var1) {
        return this.name.equals(var1.name);
    }
}
