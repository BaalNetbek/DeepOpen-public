package GoF2;

/**
 * Contains ship parameters and items equipped and in cargo. Doesnt't contain 3D model or anything visual. 
 * @author fishlabs
 */
public final class Ship {
    public static final short[] SHIP_PREVIEW_SCALING = {
          622, 1338, 1066, 783, 1181, 912, 939, 1471, 1199,
          1192, 763, 1633, 1103, 2000, 2000, 2000, 1802, 1930,
          1590, 1205, 1024, 1623, 1266, 1038, 1370, 1370, 1110,
          1276, 1722, 1553, 975, 1175, 627, 1185, 916, 767, 1738
    };
    public static final short[] SHIP_HANGAR_OFFSETS = { 
          24, -51, -37, 78, -197, 8, -14, 37, -104, 121,
          -100, -381, -132, 0, 0, 0, -520, -137, -206, 
          0, -182, -347, 2, -61, -116, -254, -316, -40,
          -182, -372, -68, -160, -142, 131, -102, -72, -402
    };
    private final int id;
    private final int baseHP;
    private int basePrice;
    private final int baseCargo;
    private int cargo;
    private int price;
    private final int[] itemTypeSlots;
    private final float handling;
    private int shield;
    private int additionalArmour;
    private int shieldRegenTime;
    private int extendedCargo;
    private int boostPercent;
    private int boostRegenTime;
    private int boostDuration;
    private int extendedHandlingPercent;
    private int passengersCapacity;
    private int firePower;
    private boolean repairBot;
    private boolean jumpDrive;
    private boolean cloak;
    private int race;
    private Item[] equipped;
    private Item[] cargoHold;
    
    /**
     * Constructor
     * @param id - ship id 0..37 in original
     * @param baseHP - hull
     * @param baseCargo - cargo hold in tons
     * @param basePrice - price in credits
     * @param primarySlots - 0..4
     * @param secondarySlots - 0..4
     * @param turretSlots - 0..1
     * @param equipmentSlots - in game max is 15
     * @param handling - in percent
     */
    public Ship(final int id, final int baseHP, final int baseCargo, final int basePrice, final int primarySlots, final int secondarySlots, final int turretSlots, final int equipmentSlots, final float handling) {
        this.id = id;
        this.baseHP = baseHP;
        this.baseCargo = baseCargo;
        this.cargo = 0;
        this.price = basePrice;
        this.basePrice = basePrice;
        this.itemTypeSlots = new int[4];
        this.itemTypeSlots[0] = primarySlots;
        this.itemTypeSlots[1] = secondarySlots;
        this.itemTypeSlots[2] = turretSlots;
        this.itemTypeSlots[3] = equipmentSlots;
        this.handling = handling / 100.0f;
        this.equipped = new Item[primarySlots + secondarySlots + turretSlots + equipmentSlots];
        this.cargoHold = null;
        this.race = 0;
        this.refreshValue();
    }

    public final void setRace(final int race) {
        this.race = race;
    }

    public final int getRace() {
        return this.race;
    }

    public final int getIndex() {
        return this.id;
    }

    public final int getPrice() {
        return this.id == 10 && Achievements.gotAllMedals() ? 50000 : this.price;
    }

    public final float getHandling() {
        return this.handling;
    }

    public final int getBaseHP() {
        return this.baseHP;
    }

    public final int getCombinedHP() {
        return this.baseHP + this.shield + this.additionalArmour;
    }

    public final int getAdditionalArmour() {
        return this.additionalArmour;
    }

    public final int getBaseArmour() {
        return this.baseHP;
    }

    public final int getShield() {
        return this.shield;
    }

    public final int getShieldRegenTime() {
        return this.shieldRegenTime;
    }

    public final int getBoostPercent() {
        return this.boostPercent;
    }

    public final int getBoostDuration() {
        return this.boostDuration;
    }

    public final int getBoostDelay() {
        return this.boostRegenTime;
    }

    public final int getAddHandlingPercent() {
        return this.extendedHandlingPercent;
    }

    public final boolean hasBooster() {
        return this.boostPercent > 0;
    }

    public final boolean hasJumpDrive() {
        return this.jumpDrive;
    }

    public final boolean hasCloak() {
        return this.cloak;
    }

    public final boolean hasRepairBot() {
        return this.repairBot;
    }

    public final int getFirePower() {
        return this.firePower;
    }

    public final int getBaseLoad() {
        return this.baseCargo;
    }

    public final int getCargoPlus() {
        return this.baseCargo + this.extendedCargo;
    }

    public final int getCurrentLoad() {
        return this.cargo;
    }

    public final Item[] getEquipment() {
        return this.equipped;
    }

    public final Item[] getCargo() {
        return this.cargoHold;
    }

    public final void refreshCargoSpaceUnsafe(final Item[] var1) {
        this.cargoHold = var1;
        this.cargo = 0;

        for(int var2 = 0; var2 < this.cargoHold.length; ++var2) {
            this.cargo += this.cargoHold[var2].getAmount();
        }

        refreshValue();
        if (Status.maxFreeCargo < getFreeCargo()) {
            Status.maxFreeCargo = getFreeCargo();
        }

    }

    public final Item getFirstEquipmentOfSort(final int var1) {
        for(int var2 = 0; var2 < this.equipped.length; ++var2) {
            if (this.equipped[var2] != null && this.equipped[var2].getSort() == var1) {
                return this.equipped[var2];
            }
        }

        return null;
    }

    public final boolean hasEquipment(final int var1) {
        if (this.equipped == null) {
            return false;
        }
        for(int var2 = 0; var2 < this.equipped.length; ++var2) {
            if (this.equipped[var2] != null && this.equipped[var2].getIndex() == var1) {
                return true;
            }
        }

        return false;
    }

    public final boolean hasWeaponInCargo() {
        if (this.cargoHold == null) {
            return false;
        }
        for(int i = 0; i < this.cargoHold.length; ++i) {
          // this is next level inlining (the method is used only when ship.id = 0 - Betty)
          //if (this.cargoHold[i] != null && this.cargoHold[i].getType() == this.id) { <- original decomp
            if (this.cargoHold[i] != null && this.cargoHold[i].getType() == 0) { 
                return true;
            }
        }

        return false;
    }

    public final void removeCargo(final Item var1) {
        this.removeCargo(var1.getIndex(), var1.getAmount());
    }

    public final void removeAllCargo() {
        this.cargoHold = null;
    }

    public final boolean removeCargo(final int id) {
        return this.removeCargo(id, 9999999);
    }

    public final boolean removeCargo(final int var1, final int var2) {
        if (this.cargoHold == null) {
            return false;
        }
        boolean var3 = false;

        for(int var4 = 0; var4 < this.cargoHold.length; ++var4) {
            if (this.cargoHold[var4].getIndex() == var1) {
                this.cargoHold[var4].changeAmount(-var2);
                if (this.cargoHold[var4].getAmount() <= 0) {
                    var3 = true;
                }
                break;
            }
        }

        if (var3) {
            setCargo(Item.extractItems(this.cargoHold, true));
        } else {
            setCargo(this.cargoHold);
        }

        return var3;
    }

    public final void setCargo(final Item[] var1) {
        this.cargoHold = var1;
        this.cargo = 0;
        if (var1 != null) {
            for(int var2 = 0; var2 < this.cargoHold.length; ++var2) {
                this.cargo += this.cargoHold[var2].getAmount();
            }
        }

        refreshValue();
        if (Status.maxFreeCargo < getFreeCargo()) {
            Status.maxFreeCargo = getFreeCargo();
        }

    }

    public final boolean spaceAvailable(final int var1) {
        return this.cargo + var1 <= this.baseCargo + this.extendedCargo;
    }

    public final int getFreeCargo() {
        return getCargoPlus() - this.cargo;
    }

    public final void addCargo(final Item var1) {
        final Item[] var2 = {var1};
        setCargo(Item.mixItems(this.cargoHold, var2));
    }

    public final void replaceEquipment(final Item[] var1) {
        this.equipped = var1;
        refreshValue();
    }

    public final void removeEquipment(final Item var1) {
        if (this.equipped != null) {
            for(int var2 = 0; var2 < this.equipped.length; ++var2) {
                if (this.equipped[var2] != null && this.equipped[var2].equals(var1)) {
                    this.equipped[var2] = null;
                    return;
                }
            }

        }
    }
    
    /**
     * @param type equipable category (0-primary, 1-secondary, 2-turret, 3-equipment)                
     * @return equipped items of given type in array
     */
    public final Item[] getEquipment(final int type) {
        if ((type >= this.itemTypeSlots.length) || (this.itemTypeSlots[type] == 0)) {
            return null;
        }
        final Item[] typeEq = new Item[this.itemTypeSlots[type]];
        
        int offset = 0;
        for(int i = 0; i < type; ++i) {
            offset += this.itemTypeSlots[i];
        }

        int j = 0;
        for(int i = offset; i < offset + this.itemTypeSlots[type]; ++i) {
            if (j < typeEq.length) {
                typeEq[j] = this.equipped[i];
                j++;
            } else {
                System.err.println("Ship.getEquipment() failed");
            }
        }

        return typeEq;
    }

    public final void setEquipment(final Item var1, int var2) {

        for(int var3 = 0; var3 < var1.getType(); ++var3) {
            var2 += this.itemTypeSlots[var3];
        }

        if (var2 < this.equipped.length) {
            this.equipped[var2] = var1;
            refreshValue();
        } else {
            System.out.println("Ship.setEquipment() Array Index out of bounds");
        }
    }

    private void refreshValue() {
        this.repairBot = false;
        this.jumpDrive = false;
        this.cloak = false;
        this.shield = 0;
        this.shieldRegenTime = 0;
        this.extendedCargo = 0;
        this.boostRegenTime = 0;
        this.boostPercent = 0;
        this.boostDuration = 0;
        this.extendedHandlingPercent = 0;
        this.additionalArmour = 0;
        this.firePower = 0;
        this.passengersCapacity = 0;
        this.basePrice = this.price;

        for(int i = 0; i < this.equipped.length; ++i) {
            if (this.equipped[i] != null) {
                switch(this.equipped[i].getSort()) {
                case Item.LASER:
                case Item.BLASTER:
                case Item.AUTOCANNON:
                case Item.THERMO:
                case Item.TURRET_SUB:
                    this.firePower += this.equipped[i].getAttribute(Item.DAMAGE);
                    break;
                case Item.SHIELD:
                    this.shield = this.equipped[i].getAttribute(Item.SHIELD_VALUE);
                    this.shieldRegenTime = this.equipped[i].getAttribute(Item.SHIELD_REGEN_TIME);
                    break;
                case Item.ARMOR:
                    this.additionalArmour = this.equipped[i].getAttribute(Item.ARMOR_VALUE);
                    break;
                case Item.COMPRESSION:
                    this.extendedCargo += this.equipped[i].getAttribute(Item.CARGO_EXPANSION);
                    break;
                case Item.BOOSTER:
                    this.boostPercent = this.equipped[i].getAttribute(Item.SPEED_BOOST);
                    this.boostDuration = this.equipped[i].getAttribute(Item.BOOST_LENGTH);
                    this.boostRegenTime = this.equipped[i].getAttribute(Item.BOOST_LOAD_TIME);
                    break;
                case Item.REPAIR_BOT:
                    this.repairBot = true;
                    break;
                case Item.STEERING_NOZZLE:
                    this.extendedHandlingPercent = this.equipped[i].getAttribute(Item.HANDLING_BOOST);
                    break;
                case Item.JUMP_DRIVE:
                    this.jumpDrive = true;
                    break;
                case Item.CABIN:
                    this.passengersCapacity += this.equipped[i].getAttribute(Item.CABIN_SIZE);
                    break;
                case Item.CLOAK:
                    this.cloak = true;
                    break;
                default:
               	  break; 
                }

                this.basePrice += this.equipped[i].getTotalPrice();
            }
        }

        this.extendedCargo = (int)((float)this.baseCargo * (float)this.extendedCargo / 100.0F);
        int var2 = 0;
        if (this.cargoHold != null) {
            for(int i = 0; i < this.cargoHold.length; ++i) {
                if (this.cargoHold[i] != null) {
                    var2 += this.cargoHold[i].getTotalPrice();
                }
            }
        }

        this.basePrice = this.basePrice + var2;
    }

    public final int getMaxPassengers() {
        return this.passengersCapacity;
    }

    public final void freeSlot(final Item var1, final int var2) {
        for(int i = 0; i < this.equipped.length; ++i) {
            if (this.equipped[i] != null && this.equipped[i].equals(var1) && i == var2) {
                this.equipped[i] = null;
                break;
            }
        }

        refreshValue();
    }

    public final void freeSlot(final Item var1) {
        for(int i = 0; i < this.equipped.length; ++i) {
            if (this.equipped[i] != null && this.equipped[i].equals(var1)) {
                this.equipped[i] = null;
                break;
            }
        }

        refreshValue();
    }

    public final int getSlots(final int type) {
        return this.itemTypeSlots[type];
    }
    /**
     * For example ships has primary and equipment slots then returns 2.
     * @return number of types of items mountable in the ship
     */
    public final int getSlotTypes() {
        int var1 = 0;

        for(int i = 0; i < this.itemTypeSlots.length; ++i) {
            if (this.itemTypeSlots[i] > 0) {
                ++var1;
            }
        }

        return var1;
    }

    public final int getUsedSlots(final int type) {
        int var2 = 0;

        for(int i = 0; i < this.equipped.length; ++i) {
            if (this.equipped[i] != null && this.equipped[i].getType() == type) {
                ++var2;
            }
        }

        return var2;
    }

    public final Ship cloneBase() {
        return new Ship(this.id, this.baseHP, this.baseCargo, this.price, this.itemTypeSlots[0], this.itemTypeSlots[1], this.itemTypeSlots[2], this.itemTypeSlots[3], this.handling * 100.0F);
    }

    public final void setSellingPrice() {
        this.price = (int)(Globals.getShips()[this.id].getPrice() / 1.25F);
    }

    public final void adjustPrice() {
        if (Status.getStation() != null) {
            this.price = Globals.getShips()[this.id].getPrice() - (int)(Status.getStation().getTecLevel() / 100.0F * Globals.getShips()[this.id].getPrice());
        }

    }
}
