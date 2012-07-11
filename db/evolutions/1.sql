 # --- !Ups
 
    create table Carte (
        id bigint not null auto_increment,
        attachee bit not null,
        engagee bit not null,
        gauche integer,
        proprietaire integer not null,
        retournee bit not null,
        token bit not null,
        top integer not null,
        transformee bit not null,
        carteModele_id bigint,
        primary key (id)
    );

    create table CarteDeck (
        id bigint not null auto_increment,
        carte tinyblob,
        nombre integer,
        deck_id bigint,
        primary key (id)
    );

    create table CarteModele (
        id bigint not null auto_increment,
        imageUrl varchar(255),
        nom varchar(255),
        primary key (id)
    );

    create table DeckModele (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table Partie (
        id bigint not null auto_increment,
        compteursPoisonJoueur1 integer not null,
        compteursPoisonJoueur2 integer not null,
        date datetime,
        etape integer not null,
        nom varchar(255),
        pointsVieJoueur1 integer not null,
        pointsVieJoueur2 integer not null,
        tour integer not null,
        joueur1_id bigint,
        joueur2_id bigint,
        partieModele_id bigint,
        primary key (id)
    );

    create table PartieModele (
        id bigint not null auto_increment,
        imageBaseUrl varchar(255),
        nom varchar(255),
        primary key (id)
    );

    create table PartieModele_ZoneModele (
        PartieModele_id bigint not null,
        listeZones_id bigint not null,
        unique (listeZones_id)
    );

    create table User (
        id bigint not null auto_increment,
        email varchar(255),
        isAdmin bit not null,
        password varchar(255),
        username varchar(255),
        primary key (id)
    );

    create table ZONE_CARTE (
        Zone_id bigint not null,
        listeCarte_id bigint not null,
        listeCarte_ORDER integer not null,
        primary key (Zone_id, listeCarte_ORDER)
    );

    create table Zone (
        id bigint not null auto_increment,
        nom varchar(255),
        partie_id bigint,
        primary key (id)
    );

    create table ZoneModele (
        id bigint not null auto_increment,
        gauche integer,
        height integer,
        image varchar(255),
        isImage bit,
        isPublique bit,
        nom varchar(255),
        top integer,
        width integer,
        primary key (id)
    );

    alter table Carte 
        add index FK3DDF9259BAF4096 (carteModele_id), 
        add constraint FK3DDF9259BAF4096 
        foreign key (carteModele_id) 
        references CarteModele (id);

    alter table CarteDeck 
        add index FK84634F6EC26402FA (deck_id), 
        add constraint FK84634F6EC26402FA 
        foreign key (deck_id) 
        references DeckModele (id);

    alter table Partie 
        add index FK8E102C6FE90AF31E (partieModele_id), 
        add constraint FK8E102C6FE90AF31E 
        foreign key (partieModele_id) 
        references PartieModele (id);

    alter table Partie 
        add index FK8E102C6FE6B22BCA (joueur1_id), 
        add constraint FK8E102C6FE6B22BCA 
        foreign key (joueur1_id) 
        references User (id);

    alter table Partie 
        add index FK8E102C6FE6B2A029 (joueur2_id), 
        add constraint FK8E102C6FE6B2A029 
        foreign key (joueur2_id) 
        references User (id);

    alter table PartieModele_ZoneModele 
        add index FK99B9E5CE90AF31E (PartieModele_id), 
        add constraint FK99B9E5CE90AF31E 
        foreign key (PartieModele_id) 
        references PartieModele (id);

    alter table PartieModele_ZoneModele 
        add index FK99B9E5C757DE5E6 (listeZones_id), 
        add constraint FK99B9E5C757DE5E6 
        foreign key (listeZones_id) 
        references ZoneModele (id);

    alter table ZONE_CARTE 
        add index FK3184FED2493AF75E (Zone_id), 
        add constraint FK3184FED2493AF75E 
        foreign key (Zone_id) 
        references Zone (id);

    alter table ZONE_CARTE 
        add index FK3184FED284D78B7D (listeCarte_id), 
        add constraint FK3184FED284D78B7D 
        foreign key (listeCarte_id) 
        references Carte (id);

    alter table Zone 
        add index FK2A97CC4946D77E (partie_id), 
        add constraint FK2A97CC4946D77E 
        foreign key (partie_id) 
        references Partie (id);

# --- !Downs