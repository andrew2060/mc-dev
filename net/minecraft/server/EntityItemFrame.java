package net.minecraft.server;

public class EntityItemFrame extends EntityHanging {

    private float e = 1.0F;

    public EntityItemFrame(World world) {
        super(world);
    }

    public EntityItemFrame(World world, int i, int j, int k, int l) {
        super(world, i, j, k, l);
        this.setDirection(l);
    }

    protected void a() {
        this.getDataWatcher().a(2, 5);
        this.getDataWatcher().a(3, Byte.valueOf((byte) 0));
    }

    public int d() {
        return 9;
    }

    public int e() {
        return 9;
    }

    public void b(Entity entity) {
        ItemStack itemstack = this.h();

        if (entity instanceof EntityHuman) {
            EntityHuman entityhuman = (EntityHuman) entity;

            if (entityhuman.abilities.canInstantlyBuild) {
                this.b(itemstack);
                return;
            }
        }

        this.a(new ItemStack(Item.ITEM_FRAME), 0.0F);
        if (itemstack != null && this.random.nextFloat() < this.e) {
            itemstack = itemstack.cloneItemStack();
            this.b(itemstack);
            this.a(itemstack, 0.0F);
        }
    }

    private void b(ItemStack itemstack) {
        if (itemstack != null) {
            if (itemstack.id == Item.MAP.id) {
                WorldMap worldmap = ((ItemWorldMap) itemstack.getItem()).getSavedMap(itemstack, this.world);

                worldmap.g.remove("frame-" + this.id);
            }

            itemstack.a((EntityItemFrame) null);
        }
    }

    public ItemStack h() {
        return this.getDataWatcher().getItemStack(2);
    }

    public void a(ItemStack itemstack) {
        itemstack = itemstack.cloneItemStack();
        itemstack.count = 1;
        itemstack.a(this);
        this.getDataWatcher().watch(2, itemstack);
        this.getDataWatcher().h(2);
    }

    public int i() {
        return this.getDataWatcher().getByte(3);
    }

    public void setRotation(int i) {
        this.getDataWatcher().watch(3, Byte.valueOf((byte) (i % 4)));
    }

    public void b(NBTTagCompound nbttagcompound) {
        if (this.h() != null) {
            nbttagcompound.setCompound("Item", this.h().save(new NBTTagCompound()));
            nbttagcompound.setByte("ItemRotation", (byte) this.i());
            nbttagcompound.setFloat("ItemDropChance", this.e);
        }

        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.isEmpty()) {
            this.a(ItemStack.createStack(nbttagcompound1));
            this.setRotation(nbttagcompound.getByte("ItemRotation"));
            if (nbttagcompound.hasKey("ItemDropChance")) {
                this.e = nbttagcompound.getFloat("ItemDropChance");
            }
        }

        super.a(nbttagcompound);
    }

    public boolean c(EntityHuman entityhuman) {
        if (this.h() == null) {
            ItemStack itemstack = entityhuman.aY();

            if (itemstack != null && !this.world.isStatic) {
                this.a(itemstack);
                if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }
            }
        } else if (!this.world.isStatic) {
            this.setRotation(this.i() + 1);
        }

        return true;
    }
}
