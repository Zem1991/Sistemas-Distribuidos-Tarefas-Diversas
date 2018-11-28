package entities;

import entities.Locais;
import entities.Quartoshotel;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-05T17:09:38")
@StaticMetamodel(Hoteis.class)
public class Hoteis_ { 

    public static volatile CollectionAttribute<Hoteis, Quartoshotel> quartoshotelCollection;
    public static volatile SingularAttribute<Hoteis, Integer> idLocal;
    public static volatile SingularAttribute<Hoteis, String> nome;
    public static volatile SingularAttribute<Hoteis, Integer> id;
    public static volatile SingularAttribute<Hoteis, Locais> locais;

}