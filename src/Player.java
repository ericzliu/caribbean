import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class CubicCoord {

    private int x;
    private int y;
    private int z;

    public CubicCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int distance(CubicCoord t) {
        return (Math.abs(x - t.x) + Math.abs(y - t.y) + Math.abs(z - t.z)) / 2;
    }
}

class OffsetCoord {

    private int col;
    private int row;

    public OffsetCoord(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OffsetCoord that = (OffsetCoord) o;

        if (col != that.col) return false;
        return row == that.row;
    }

    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        return result;
    }
}

class Entity {

    private int id;
    private int col;
    private int row;

    public Entity(int id, int col, int row) {
        this.id = id;
        this.col = col;
        this.row = row;
    }

    public int getId() {
        return id;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int distance(Entity t) {
        return toCub().distance(t.toCub());
    }

    public OffsetCoord toCoord() {
        return new OffsetCoord(getCol(), getRow());
    }

    private CubicCoord toCub() {

        int x = col - (row - (row & 1)) / 2;
        int z = row;
        int y = -x - z;

        return new CubicCoord(x, y, z);
    }

}

class Rum extends Entity {

    private int quant;

    public Rum(int id, int col, int row, int quant) {
        super(id, col, row);
        this.quant = quant;
    }

    public int getQuant() {
        return quant;
    }
}

class Ship extends Entity {

    private int owner;
    private int quant;
    private int speed;
    private int direction;

    public Ship(int id, int col, int row, int owner, int quant, int speed, int direction) {
        super(id, col, row);
        this.owner = owner;
        this.quant = quant;
        this.speed = speed;
        this.direction = direction;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<OffsetCoord> getPositions() {
        int row = getRow();
        int offOdd = (row % 2 == 1) ? 1 : 0;
        List<OffsetCoord> positions = new ArrayList<>();
        positions.add(this.toCoord());
        switch (direction) {
            case 0:
            case 3: {
                positions.add(new OffsetCoord(getCol() + 1, getRow()));
                positions.add(new OffsetCoord(getCol() - 1, getRow()));
                break;
            }
            case 1:
            case 4: {
                positions.add(new OffsetCoord(getCol() + offOdd, getRow() - 1));
                positions.add(new OffsetCoord(getCol() - 1 + offOdd, getRow() + 1));
                break;
            }
            case 2:
            case 5: {
                positions.add(new OffsetCoord(getCol() - 1 + offOdd, getRow() - 1));
                positions.add(new OffsetCoord(getCol() + offOdd, getRow() + 1));
                break;
            }
            default:
                break;
        }
        return positions;
    }

    public boolean overlap(Entity entity) {
        List<OffsetCoord> coords = getPositions();
        return coords.contains(entity.toCoord());
    }

    public boolean overlap(Ship entity) {
        List<OffsetCoord> positions = entity.getPositions();
        for (OffsetCoord coord : getPositions()) {
            if (positions.contains(coord)) {
                return true;
            }
        }
        return false;
    }
}


class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            // status
            List<Rum> rums = new ArrayList<>();
            List<Ship> ourship = new ArrayList<>();

            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();
                switch (entityType) {
                    case "SHIP":
                        if (arg4 == 1) {
                            ourship.add(new Ship(entityId, x, y, arg4, arg3, arg2, arg1));
                        }
                        break;
                    case "BARREL":
                        rums.add(new Rum(entityId, x, y, arg1));
                        break;
                    case "MINE":
                        break;
                    case "CANNONBALL":
                        break;
                }
            }

            for (int i = 0; i < myShipCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                int min = Integer.MAX_VALUE;
                Ship ship = ourship.get(i);
                Rum nearest = null;
                for (Rum rum : rums) {
                    int distance = ship.distance(rum);
                    if (distance < min) {
                        min = distance;
                        nearest = rum;
                    }
                }
                if (nearest != null)
                    System.out.println("MOVE " + nearest.getCol() + " " + nearest.getRow()); // Any valid action, such as "WAIT" or "MOVE x y"
                else
                    System.out.println("WAIT");
            }
        }
    }
}