package edu.polytech.filrouge_tp3;

public class AccidentFactoryDetailMode {
    public static final int VOITURE = 1;
    
    public static final int COLLISION_MOTO = 2;
    public static final int COLLISION_VOITURE = 3;
    public static final int VEHICULE_LOURD = 4;
    public static final int OBSTACLE = 5;
    public static final int SORTIE_ROUTE = 6;

    public static Accident build(int type) throws Throwable {
        Accident accident;
        switch (type) {
            case VOITURE:
            case COLLISION_VOITURE:
                accident = new AccidentVoiture();
                break;
            case COLLISION_MOTO:
                accident = new AccidentMoto();
                break;
            default:
                throw new Throwable("Type d'accident inconnu");
        }
        return accident;
    }
}
